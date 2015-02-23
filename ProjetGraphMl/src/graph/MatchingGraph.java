/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

/**
 *
 * @author loicleger
 */

//import com.tinkerpop.blueprints.util.io.graphml.GraphMLTokens;
//import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.GraphMLMetadata;
import edu.uci.ics.jung.io.GraphMLReader;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;
import edu.uci.ics.jung.visualization.util.Animator;
import edu.uci.ics.jung.visualization.util.PredicatedParallelEdgeIndexFunction;
//import graph.contollers.graphController.*;
//import graph.LinearProgram.LinearProgramOptimal;
//import ilog.concert.IloException;
import org.apache.commons.collections15.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.Graph;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import static java.lang.System.err;
import static java.lang.System.out;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;
import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class MatchingGraph extends JApplet {

    private Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
    private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    String instructions
            = "<html>Use the mouse to select multiple vertices"
            + "<p>either by dragging a region, or by shift-clicking"
            + "<p>on multiple vertices."
            + "<p>After you select vertices, use the Collapse button"
            + "<p>to combine them into a single vertex."
            + "<p>Select a 'collapsed' vertex and use the Expand button"
            + "<p>to restore the collapsed vertices."
            + "<p>The Restore button will restore the original graph."
            + "<p>If you select 2 (and only 2) vertices, then press"
            + "<p>the Compress Edges button, parallel edges between"
            + "<p>those two vertices will no longer be expanded."
            + "<p>If you select 2 (and only 2) vertices, then press"
            + "<p>the Expand Edges button, parallel edges between"
            + "<p>those two vertices will be expanded."
            + "<p>You can drag the vertices with the mouse."
            + "<p>Use the 'Picking'/'Transforming' combo-box to switch"
            + "<p>between picking and transforming mode.</html>";

    // Graph graph;
    private static DirectedGraph<String, String> graph = new DirectedSparseMultigraph<String, String>();
    static HashMap<Integer, Collection<ComplexVertex>> verticesSets = new HashMap<>();
    static HashMap<Integer, HashMap<Integer, ComplexVertex>> verticesPredecessorsSets = new HashMap<>();

    //Factory<Vertex> vertexFactory = new VertexFactory();
    // Factory<Edge> edgeFactory = new EdgeFactory() ;
    Graph collapsedGraph;
    static Collection<Collection<ComplexVertex>> CollGoupOfNodes = new HashSet();
    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer vv;

    Layout layout;

    GraphCollapser collapser;

    Factory<String> vertexFactory = new VertexFactory();
    Factory<String> edgeFactory = new EdgeFactory();

    public MatchingGraph() {

        // create a simple graph for the demo
        // graph = TestGraphs.getOneComponentGraph();
        //DirectedGraph<String, String> graphOfFile = new DirectedSparseMultigraph<String, String>();
            graph.addVertex("YEAR");
            graph.addVertex("2010");
            graph.addVertex("2011");
            graph.addVertex("2012");
            graph.addEdge("edge1", "YEAR", "2010");
            graph.addEdge("edge2", "YEAR", "2011");
            graph.addEdge("edge3", "YEAR", "2012");
            graph.addVertex("COUNTRY");
            graph.addVertex("Allemagne");
            graph.addVertex("Chine");
            graph.addVertex("France");
            graph.addEdge("edge4", "COUNTRY", "Allemagne");
            graph.addEdge("edge5", "COUNTRY", "Chine");
            graph.addEdge("edge6", "COUNTRY", "France");
            
        collapsedGraph = graph;
        collapser = new GraphCollapser(graph);

        Dimension preferredSize = new Dimension(400, 400);
        layout = new StaticLayout(graph,preferredSize);
        //layout.setGraph(graph);
        final VisualizationModel visualizationModel
                = new DefaultVisualizationModel(layout, preferredSize);

        vv = new VisualizationViewer(visualizationModel, preferredSize);

        vv.getRenderContext().setVertexShapeTransformer(new ClusterVertexShapeFunction());
        vv.getRenderContext().setVertexFillPaintTransformer(new ClusterVertexColorFunction());
        vv.getRenderContext().setVertexLabelTransformer(MapTransformer.<String, String>getInstance(
                LazyMap.<String, String>decorate(new HashMap<String, String>(), new ToStringLabeller<String>())));

//        vv.getRenderContext().setEdgeLabelTransformer(MapTransformer.<String,String>getInstance(
//                LazyMap.<String,String>decorate(new HashMap<String,String>(), new ToStringLabeller<String>())));
//        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        // le labels est en dessous du vertex
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.S);

        final PredicatedParallelEdgeIndexFunction eif = PredicatedParallelEdgeIndexFunction.getInstance();
        final Set exclusions = new HashSet();
        eif.setPredicate(new Predicate() {

            public boolean evaluate(Object e) {

                return exclusions.contains(e);
            }
        });

        vv.getRenderContext().setParallelEdgeIndexFunction(eif);

        vv.setBackground(Color.white);

        // add a listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller() {

            @Override
            public String transform(Object v) {
                if (v instanceof Graph) {
                    return ((Graph) v).getVertices().toString();
                }
                return super.transform(v);
            }
        });

        /**
         * the regular graph mouse for the normal view
         */
        //final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        final EditingModalGraphMouse<String, String> graphMouse
                = new EditingModalGraphMouse<String, String>(vv.getRenderContext(), vertexFactory, edgeFactory);

        vv.setGraphMouse(graphMouse);

        final Container content = getContentPane();
        GraphZoomScrollPane gzsp = new GraphZoomScrollPane(vv);
        content.add(gzsp);

        JComboBox modeBox = graphMouse.getModeComboBox();
        modeBox.addItemListener(graphMouse.getModeListener());
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);

        final JFileChooser jFileChooserOpen = new JFileChooser();
        jFileChooserOpen.setControlButtonsAreShown(true);
        jFileChooserOpen.setVisible(true);

        final ScalingControl scaler = new CrossoverScalingControl();

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1 / 1.1f, vv.getCenter());
            }
        });

      
        JButton compressEdges = new JButton("Compress Edges");
        compressEdges.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Collection picked = vv.getPickedVertexState().getPicked();
                if (picked.size() == 2) {
                    Pair pair = new Pair(picked);
                    Graph graph = layout.getGraph();
                    Collection edges = new HashSet(graph.getIncidentEdges(pair.getFirst()));
                    edges.retainAll(graph.getIncidentEdges(pair.getSecond()));
                    exclusions.addAll(edges);
                    vv.repaint();
                }

            }
        });

        JButton expandEdges = new JButton("Expand Edges");
        expandEdges.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Collection picked = vv.getPickedVertexState().getPicked();
                if (picked.size() == 2) {
                    Pair pair = new Pair(picked);
                    Graph graph = layout.getGraph();
                    Collection edges = new HashSet(graph.getIncidentEdges(pair.getFirst()));
                    edges.retainAll(graph.getIncidentEdges(pair.getSecond()));
                    exclusions.removeAll(edges);
                    vv.repaint();
                }

            }
        });


        Class[] combos = getCombos();
        final JComboBox jcb = new JComboBox(combos);
        // use a renderer to shorten the layout name presentation
        jcb.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String valueString = value.toString();
                valueString = valueString.substring(valueString.lastIndexOf('.') + 1);
                return super.getListCellRendererComponent(list, valueString, index, isSelected,
                        cellHasFocus);
            }
        });


        JPanel controls = new JPanel();
        JPanel zoomControls = new JPanel(new GridLayout(2, 1));
        zoomControls.setBorder(BorderFactory.createTitledBorder("Zoom"));
        zoomControls.add(plus);
        zoomControls.add(minus);
        controls.add(zoomControls);
        JPanel collapseControls = new JPanel(new GridLayout(3, 1));
//        collapseControls.setBorder(BorderFactory.createTitledBorder("Picked"));
//        collapseControls.add(collapse);
//        collapseControls.add(expand);
//        collapseControls.add(compressEdges);
//        collapseControls.add(expandEdges);
//        collapseControls.add(reset);
//        controls.add(collapseControls);
//        JPanel modeControls = new JPanel(new GridLayout(2, 1));
//        modeControls.setBorder(BorderFactory.createTitledBorder("Mode"));
//        modeControls.add(modeBox);
//        modeControls.add(jcb);
//        controls.add(modeControls);
//        JPanel ConfigControls = new JPanel(new GridLayout(4, 1));
//        ConfigControls.setBorder(BorderFactory.createTitledBorder("Config"));
//        ConfigControls.add(openFile);
//        ConfigControls.add(linearProgram);
//        ConfigControls.add(collapsedGraphFromLinearProgram);
//        ConfigControls.add(help);
//        controls.add(ConfigControls);
//
//        JPanel MessageControls = new JPanel(new GridLayout(1, 1));
//        MessageControls.setBorder(BorderFactory.createTitledBorder("Messages"));
//        MessageControls.add(scroll);
//        content.add(controls, BorderLayout.SOUTH);
////		content.add(new JSeparator(JSeparator.VERTICAL),BorderLayout.);
//		content.add(MessageControls, BorderLayout.EAST);

        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        splitPane.setTopComponent(controls);        splitPane.setDividerLocation(0.7);
        content.add(splitPane, BorderLayout.SOUTH);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
    }
//
//    public void serializeOutputGraph(Graph g) throws IOException {
//
//        GraphMLWriter<Object, String> graphWriter = new GraphMLWriter<Object, String>();
//
//        String filename = "C:\\Users\\Megdiche\\Desktop\\test.graphml";
//        PrintWriter out = new PrintWriter(
//                new BufferedWriter(
//                        new FileWriter(filename)));
//
//        
//        graphWriter.setVertexIDs(new Transformer<Object, String>() {
//            public String transform(Object v) {
//                if(v.getClass().equals(ComplexVertex.class)) 
//                    return ((ComplexVertex)v).get_ID();
//               
//                else if(v.getClass().equals(DirectedSparseMultigraph.class))
//                    return v.toString();
//                else 
//                    return "iddd";
//            }
//        }
//        );
//        graphWriter.addVertexData("DisplayValue", null, "0",
//                new Transformer<Object, String>() {
//                    public String transform(Object v) {
//                     if(v.getClass().equals(ComplexVertex.class)) 
//                        return ((ComplexVertex)v).getDisplayValue();
//                     else if(v.getClass().equals(DirectedSparseMultigraph.class))
//                     { 
//                        try {
//                            return  OutputCollapsedGraph((Graph)v).toString();
//                     } catch (IOException ex) {
//                         Logger.getLogger(MatchingGraph.class.getName()).log(Level.SEVERE, null, ex);
//                     }
//                     }
//                     
//                        return null;
//                     
//                    }
//                }
//        );
//        graphWriter.addVertexData("idSrcOrigin", null, "0",
//                new Transformer<Object, String>() {
//                    public String transform(Object v) {
//                        if(v.getClass().equals(ComplexVertex.class)) 
//                             return ((ComplexVertex)v).getFileName(); 
//                        else            
//                             return "sourcesOrgin"; 
//                    }
//                }
//        );
//
//        graphWriter.save(g, out);
//    }

      public StringWriter OutputCollapsedGraph(Graph g) throws IOException {

        GraphMLWriter<ComplexVertex, String> graphWriter = new GraphMLWriter<ComplexVertex, String>();
        
        graphWriter.setVertexIDs(new Transformer<ComplexVertex, String>() {
            public String transform(ComplexVertex v) {
                    return v.get_ID();
            }
           }
        );
        graphWriter.addVertexData("DisplayValue", null, "0",
                new Transformer<ComplexVertex, String>() {
                    public String transform(ComplexVertex v) {
                        return v.getDisplayValue();
                    }
                }
        );
        graphWriter.addVertexData("idSrcOrigin", null, "0",
                new Transformer<ComplexVertex, String>() {
                    public String transform(ComplexVertex v) {
                             return v.getFileName(); 
                    }
                }
        );
     
        StringWriter sr = new StringWriter();
        graphWriter.save(g, sr);
        return sr ; 
    }
    class ClusterVertexShapeFunction<V> extends EllipseVertexShapeTransformer<V> {

        ClusterVertexShapeFunction() {
            setSizeTransformer(new ClusterVertexSizeFunction<V>(20));
        }

        @Override
        public Shape transform(V v) {
            if (v instanceof ComplexVertex) {
                Ellipse2D circle = new Ellipse2D.Double(-10, -10, 20, 20);
                return circle;
            }
            if (v instanceof Graph) {
                int size = ((Graph) v).getVertexCount();
                if (size < 8) {
                    int sides = Math.max(size, 3);
                    return factory.getRegularPolygon(v, sides);
                } else {
                    return factory.getRegularStar(v, size);
                }
            }
            return super.transform(v);
        }
    }

    class ClusterVertexColorFunction<V> implements Transformer<V, Paint> {

        ClusterVertexColorFunction() {
        }

        public Paint transform(V v) {
            if (v instanceof ComplexVertex) {
                return ((ComplexVertex) v).getColor();
            }
            if (v instanceof Graph) {
                return Color.cyan;
            }
            return Color.YELLOW;
        }
    }

    class ClusterVertexSizeFunction<V> implements Transformer<V, Integer> {

        int size;

        public ClusterVertexSizeFunction(Integer size) {
            this.size = size;
        }

        public Integer transform(V v) {
            if (v instanceof Graph) {
                return 30;
            }
            return size;
        }
    }


    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    private Class<? extends Layout>[] getCombos() {
        java.util.List<Class<? extends Layout>> layouts = new ArrayList<Class<? extends Layout>>();
        layouts.add(TreeLayout.class);
        layouts.add(KKLayout.class);
        layouts.add(FRLayout.class);
        layouts.add(CircleLayout.class);
        layouts.add(edu.uci.ics.jung.algorithms.layout.SpringLayout.class);
        layouts.add(SpringLayout2.class);
        layouts.add(ISOMLayout.class);
        return layouts.toArray(new Class[0]);
    }

    public void openGraphML(File[] fileList) throws ParserConfigurationException, SAXException, IOException {

        Color[] colors = new Color[fileList.length];
        Random rand = new Random();
        float r, g, b;
        for (int j = 0; j <= colors.length - 1; j++) {
            r = rand.nextFloat();
            g = rand.nextFloat();
            b = rand.nextFloat();
            colors[j] = new Color(r, g, b);
        }
        int nombreVertex = 0;

        //GraphObject[] inputGraphs = new GraphObject[fileList.length];
        //GraphObject go;
        //for (int i = 0; i <= fileList.length - 1; i++) {
            // ce graphe je le crée pour qu il prenne uniquement les vertx structurelle 
//            GraphMLReader<DirectedGraph<ComplexVertex, String>, ComplexVertex, String> graphMLReader
//                    = new GraphMLReader<>(vertexFactory, edgeFactory);
//            DirectedGraph<ComplexVertex, String> graphTmp = new DirectedSparseMultigraph<ComplexVertex, String>();
//            graphMLReader.load(fileList[i].getPath(), graphTmp);
//            Collection<ComplexVertex> graphCrtVerticesSet = new HashSet<ComplexVertex>();
//            HashMap<Integer, ComplexVertex> graphVerticePredecessors = new HashMap<>();
//            Map<String, GraphMLMetadata<ComplexVertex>> vertex_data = graphMLReader.getVertexMetadata(); //Our vertex Metadata is stored in a map.
//            int orderInsertionVertex = 0;
//            Object[] tabVertex = graphTmp.getVertices().toArray();
//            Iterator itIdVertices = graphMLReader.getVertexIDs().values().iterator();
//            ComplexVertex cv;
//            String idVertex;
//
//            for (int j = 0; j < tabVertex.length; j++) {
//                cv = (ComplexVertex) tabVertex[j];
//                idVertex = (String) itIdVertices.next();
////				System.out.println(vertex_data.get("DisplayedValue").transformer.transform(cv));
//                if (vertex_data.get("Visualized").transformer.transform(cv).equals("true")) {
//                    // We get the display_value <data key="DisplayedValue">display_value</data>
//                    cv.set_ID(idVertex);
//                    cv.setDisplayValue(vertex_data.get("DisplayedValue").transformer.transform(cv));
//                    cv.setFileName(vertex_data.get("idSrcOrigin").transformer.transform(cv));
//                    cv.setNumGraphSrc(i);
//                    cv.setColor(colors[i]);
//                    graph.addVertex(cv);
//                    graphOfFile.addVertex(cv);
//                    nombreVertex++;
//                }
//            }
//
//            Object[] tabVertexNew = graphOfFile.getVertices().toArray();
//
//            // imen : je dois remettre ca non commente  
//            //Arrays.sort(tabVertex);	
//            ComplexVertex pred;
//            Iterator it1;
//
//            for (int j = 0; j < tabVertex.length; j++) {
//                cv = (ComplexVertex) tabVertex[j];
//                // verifier si ce vertex existe dans le graph de structure courant 
//                if (searchVertexInTable(cv, tabVertexNew)) {
//
//                    graphCrtVerticesSet.add(cv);
//                    Collection<ComplexVertex> predecessors = graphTmp.getPredecessors(cv);
////                    Dans cette collection on ajoute les predecesssurs de chaque vertex dans le meme ordre que de l ajout du vertex
//                    pred = null;
//                    it1 = predecessors.iterator();
//                    while (it1.hasNext()) {
//                        pred = (ComplexVertex) it1.next();
//                    }
//                    // je pense ici j avais prevu que chaque noeud a un seul predecesseur puisque l histoire des hierarchies stricte en phase 1 donc
//                    // je n ai garde que le dernier pred
//                    if (pred != null) {
//                        cv.setPredecessor(pred);
//                        graphVerticePredecessors.put(j, pred);
//                    }
//                }
//            }
//
//            //go = new GraphObject(graphTmp, graphTmp.getVertexCount(), graphCrtVerticesSet, graphVerticePredecessors);
//            go = new GraphObject(graphOfFile, graphOfFile.getVertexCount(), graphCrtVerticesSet, graphVerticePredecessors);
//            inputGraphs[i] = go;
//            //inputGraphs[i] = go;
//
//            // Selects the edges from graphTmp related to the nodes existing in _graph
//            Boolean source0k, destOk;
//            for (String e : graphTmp.getEdges()) {
//                source0k = false;
//                destOk = false;
//                for (ComplexVertex n : graph.getVertices()) {
//                    if (n.equals(graphTmp.getSource(e))) {
//                        source0k = true;
//                    }
//                    if (n.equals(graphTmp.getDest(e))) {
//                        destOk = true;
//                    }
//                    if (source0k && destOk) {
//                        graph.addEdge(e, graphTmp.getSource(e), graphTmp.getDest(e));
//                        break;
//                    }
//                }
//            }
//        }
//
//        /**
//         * * j ordonne ici les graphes ***
//         */
//        CollectionOfGraph cg = new CollectionOfGraph(inputGraphs);
//        cg.sortDescSizeOfGraph();
////       System.out.println(Arrays.toString(cg.getGraphs()));
//        for (int i = 0; i < cg.getGraphs().length; i++) {
//            verticesPredecessorsSets.put(i, cg.getGraphs()[i].getGraphVerticePredecessors());
//            verticesSets.put(i, cg.getGraphs()[i].getVertices());
//
//        }
//        System.out.println("***********le nombre de vertex est " + nombreVertex);
    }

    public boolean searchVertexInTable(ComplexVertex vertex, Object[] tableOfVertex) {
        boolean exist = false;
        for (int i = 0; i < tableOfVertex.length; i++) {
            if (vertex.equals(tableOfVertex[i])) {
                exist = true;
                break;
            }

        }
        return exist;
    }

    class VertexFactory implements Factory<String> {

        int i = 0;

        public String create() {
            int j = i++;
            return "V"+(j);
            //return new ComplexVertex("" + j + "", true);
        }
    }

    class EdgeFactory implements Factory<String> {

        int i = 0;

        public String create() {

            return "V" + (i++);
        }
    }

    public static void main(String[] args) {
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new MatchingGraph());
        f.pack();
        f.setVisible(true);
    }
}

