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
import graph.LinearProgram.LinearProgramOptimal;
import ilog.concert.IloException;
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
    private static DirectedGraph<ComplexVertex, String> graph = new DirectedSparseMultigraph<ComplexVertex, String>();
    static HashMap<Integer, Collection<ComplexVertex>> verticesSets = new HashMap<>();
    static HashMap<Integer, HashMap<Integer, ComplexVertex>> verticesPredecessorsSets = new HashMap<>();

    //Factory<Vertex> vertexFactory = new VertexFactory();
    // Factory<Edge> edgeFactory = new EdgeFactory() ;
    Graph collapsedGraph;
    TreeBuilder treeBuilder;
    static Collection<Collection<ComplexVertex>> CollGoupOfNodes = new HashSet();
    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer vv;

    Layout layout;

    GraphCollapser collapser;

    Factory<ComplexVertex> vertexFactory = new VertexFactory();
    Factory<String> edgeFactory = new EdgeFactory();

    public MatchingGraphs() {

        // create a simple graph for the demo
        // graph = TestGraphs.getOneComponentGraph();
        collapsedGraph = graph;
        collapser = new GraphCollapser(graph);
        treeBuilder = new TreeBuilder(graph);
        layout = new TreeLayout<ComplexVertex, String>(treeBuilder.getTree());

        Dimension preferredSize = new Dimension(400, 400);
        final VisualizationModel visualizationModel
                = new DefaultVisualizationModel(layout, preferredSize);

        vv = new VisualizationViewer(visualizationModel, preferredSize);

        vv.getRenderContext().setVertexShapeTransformer(new ClusterVertexShapeFunction());
        vv.getRenderContext().setVertexFillPaintTransformer(new ClusterVertexColorFunction());
        vv.getRenderContext().setVertexLabelTransformer(MapTransformer.<ComplexVertex, String>getInstance(
                LazyMap.<ComplexVertex, String>decorate(new HashMap<ComplexVertex, String>(), new ToStringLabeller<ComplexVertex>())));

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
        final EditingModalGraphMouse<ComplexVertex, String> graphMouse
                = new EditingModalGraphMouse<ComplexVertex, String>(vv.getRenderContext(), vertexFactory, edgeFactory);

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

        JButton openFile = new JButton("Open File");
        openFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                GraphmlFilter filter = new GraphmlFilter();
                jFileChooserOpen.setFileFilter(filter);
                jFileChooserOpen.setMultiSelectionEnabled(true);
                int returnVal = jFileChooserOpen.showOpenDialog(content);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File[] filesList = jFileChooserOpen.getSelectedFiles();
                        openGraphML(filesList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

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

        JButton collapse = new JButton("Merge");
        collapse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Collection picked = new HashSet(vv.getPickedVertexState().getPicked());

                if (!layout.getClass().equals(TreeLayout.class)) {
                    Graph inGraph = layout.getGraph();
                    Graph clusterGraph = collapser.getClusterGraph(inGraph, picked);

                    Graph g = collapser.collapse(layout.getGraph(), clusterGraph);
                    collapsedGraph = g;
                    double sumx = 0;
                    double sumy = 0;
                    for (Object v : picked) {
                        Point2D p = (Point2D) layout.transform(v);
                        sumx += p.getX();
                        sumy += p.getY();
                    }
                    Point2D cp = new Point2D.Double(sumx / picked.size(), sumy / picked.size());
                    vv.getRenderContext().getParallelEdgeIndexFunction().reset();
                    layout.setGraph(g);
                    layout.setLocation(clusterGraph, cp);
                    vv.getPickedVertexState().clear();
                    vv.repaint();
                } else {

                    TreeCollapser collapser = new TreeCollapser();
                    Forest inGraph = (Forest) layout.getGraph();
                    Forest clusterTree = collapser.getClusterTree(inGraph, picked);
                    Forest g = collapser.collapseFromGraph(vv.getGraphLayout(), inGraph, picked);

                    double sumx = 0;
                    double sumy = 0;
                    for (Object v : picked) {
                        Point2D p = (Point2D) layout.transform(v.toString());
                        sumx += p.getX();
                        sumy += p.getY();
                    }
                    Point2D cp = new Point2D.Double(sumx / picked.size(), sumy / picked.size());
                    vv.getRenderContext().getParallelEdgeIndexFunction().reset();
                    layout.setGraph(g);
                    layout.setLocation(clusterTree.toString(), cp);
                    vv.getPickedVertexState().clear();
                    vv.repaint();
                }

                //}
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

        JButton expand = new JButton("Expand");
        expand.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Collection picked = new HashSet(vv.getPickedVertexState().getPicked());
                for (Object v : picked) {
                    if (v instanceof Graph) {

                        Graph g = collapser.expand(layout.getGraph(), (Graph) v);
                        vv.getRenderContext().getParallelEdgeIndexFunction().reset();
                        if (!layout.getClass().equals(TreeLayout.class)) {
                            layout.setGraph(g);
                        } else {
                            // il faut transformer le graphe  g en forest pour le donner au treeLayout
                            treeBuilder = new TreeBuilder((DirectedGraph) g);
                            layout.setGraph(treeBuilder.getTree());
                        }

                    }
                    vv.getPickedVertexState().clear();
                    vv.repaint();
                }
            }
        });

        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                layout.setGraph(null);
//                layout.setGraph(graph);
//                exclusions.clear();
//                vv.repaint();
            }
        });

        JButton help = new JButton("Help");
        help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog((JComponent) e.getSource(), instructions, "Help", JOptionPane.PLAIN_MESSAGE);
            }
        });

        JTextArea MessageLabel = new JTextArea(10, 10);
        //MessageLabel.setSize( MessageLabel.getPreferredSize() );
//		PrintStream printStream = new PrintStream(new CustonOutputStream(MessageLabel));
//		System.setOut(printStream);
//		System.setErr(printStream);
        JScrollPane scroll = new JScrollPane(MessageLabel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JButton linearProgram = new JButton("Apply linear program");
        linearProgram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LinearProgramOptimal lp = new LinearProgramOptimal(graph, verticesSets, verticesPredecessorsSets);
                content.setCursor(waitCursor);
                try {
                    lp.constructAllCorrelationMatrices();
                } catch (Exception ex) {
                    Logger.getLogger(MatchingGraphs.class.getName()).log(Level.SEVERE, null, ex);
                }
                lp.constructAllIndicentMatrices();
//                lp.afficherMatricesIncidence();
                lp.afficherMatricesCorrelation();
                content.setCursor(defaultCursor);
                try {
                    lp.generateLinearProgram();
                    CollGoupOfNodes = lp.groupOfNodes;
                } catch (IloException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JButton collapsedGraphFromLinearProgram = new JButton("Linear Program Proposed Merged Graph");
        collapsedGraphFromLinearProgram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Collection<Collection<ComplexVertex>> picked = CollGoupOfNodes;
                int nombreVertexMatche = 0;
                for (Collection<ComplexVertex> tmpGroupOfMatchedNodes : CollGoupOfNodes) {
                    nombreVertexMatche += tmpGroupOfMatchedNodes.size();
                    if (!layout.getClass().equals(TreeLayout.class)) {
                        Graph inGraph = layout.getGraph();
                        Graph clusterGraph = collapser.getClusterGraph(inGraph, tmpGroupOfMatchedNodes);

                        Graph g = collapser.collapse(layout.getGraph(), clusterGraph);
                        try {
                            serializeOutputGraph(g);
                        } catch (IOException ex) {
                            Logger.getLogger(MatchingGraphs.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        collapsedGraph = g;
                        double sumx = 0;
                        double sumy = 0;
                        for (Object v : tmpGroupOfMatchedNodes) {
                            Point2D p = (Point2D) layout.transform(v);
                            sumx += p.getX();
                            sumy += p.getY();
                        }
                        Point2D cp = new Point2D.Double(sumx / tmpGroupOfMatchedNodes.size(), sumy / tmpGroupOfMatchedNodes.size());
                        vv.getRenderContext().getParallelEdgeIndexFunction().reset();
                        layout.setGraph(g);
                        layout.setLocation(clusterGraph, cp);
                        vv.getPickedVertexState().clear();
//                        vv.repaint();
                    } else {

                        TreeCollapser collapser = new TreeCollapser();
                        Forest inGraph = (Forest) layout.getGraph();
                        Forest clusterTree = collapser.getClusterTree(inGraph, tmpGroupOfMatchedNodes);
                        Forest g = collapser.collapseFromGraph(vv.getGraphLayout(), inGraph, tmpGroupOfMatchedNodes);

                        double sumx = 0;
                        double sumy = 0;
                        for (Object v : tmpGroupOfMatchedNodes) {
                            Point2D p = (Point2D) layout.transform(v.toString());
                            sumx += p.getX();
                            sumy += p.getY();
                        }
                        Point2D cp = new Point2D.Double(sumx / tmpGroupOfMatchedNodes.size(), sumy / tmpGroupOfMatchedNodes.size());
                        vv.getRenderContext().getParallelEdgeIndexFunction().reset();
                        layout.setGraph(g);
                        layout.setLocation(clusterTree.toString(), cp);
                        vv.getPickedVertexState().clear();
                        vv.repaint();
                    }
                }

                System.out.println("nombre de vertex matche est " + nombreVertexMatche);

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
        jcb.addActionListener(new LayoutChooser(jcb, vv));
        jcb.setSelectedItem(TreeLayout.class);

        JPanel controls = new JPanel();
        JPanel zoomControls = new JPanel(new GridLayout(2, 1));
        zoomControls.setBorder(BorderFactory.createTitledBorder("Zoom"));
        zoomControls.add(plus);
        zoomControls.add(minus);
        controls.add(zoomControls);
        JPanel collapseControls = new JPanel(new GridLayout(3, 1));
        collapseControls.setBorder(BorderFactory.createTitledBorder("Picked"));
        collapseControls.add(collapse);
        collapseControls.add(expand);
        collapseControls.add(compressEdges);
        collapseControls.add(expandEdges);
        collapseControls.add(reset);
        controls.add(collapseControls);
        JPanel modeControls = new JPanel(new GridLayout(2, 1));
        modeControls.setBorder(BorderFactory.createTitledBorder("Mode"));
        modeControls.add(modeBox);
        modeControls.add(jcb);
        controls.add(modeControls);
        JPanel ConfigControls = new JPanel(new GridLayout(4, 1));
        ConfigControls.setBorder(BorderFactory.createTitledBorder("Config"));
        ConfigControls.add(openFile);
        ConfigControls.add(linearProgram);
        ConfigControls.add(collapsedGraphFromLinearProgram);
        ConfigControls.add(help);
        controls.add(ConfigControls);

        JPanel MessageControls = new JPanel(new GridLayout(1, 1));
        MessageControls.setBorder(BorderFactory.createTitledBorder("Messages"));
        MessageControls.add(scroll);
//        content.add(controls, BorderLayout.SOUTH);
////		content.add(new JSeparator(JSeparator.VERTICAL),BorderLayout.);
//		content.add(MessageControls, BorderLayout.EAST);

        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        splitPane.setTopComponent(controls);
        splitPane.setBottomComponent(MessageControls);
        splitPane.setDividerLocation(0.7);
        content.add(splitPane, BorderLayout.SOUTH);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
    }

    public void serializeOutputGraph(Graph g) throws IOException {

        GraphMLWriter<Object, String> graphWriter = new GraphMLWriter<Object, String>();

        String filename = "C:\\Users\\Megdiche\\Desktop\\test.graphml";
        PrintWriter out = new PrintWriter(
                new BufferedWriter(
                        new FileWriter(filename)));

        
        graphWriter.setVertexIDs(new Transformer<Object, String>() {
            public String transform(Object v) {
                if(v.getClass().equals(ComplexVertex.class)) 
                    return ((ComplexVertex)v).get_ID();
               
                else if(v.getClass().equals(DirectedSparseMultigraph.class))
                    return v.toString();
                else 
                    return "iddd";
            }
        }
        );
        graphWriter.addVertexData("DisplayValue", null, "0",
                new Transformer<Object, String>() {
                    public String transform(Object v) {
                     if(v.getClass().equals(ComplexVertex.class)) 
                        return ((ComplexVertex)v).getDisplayValue();
                     else if(v.getClass().equals(DirectedSparseMultigraph.class))
                     { 
                        try {
                            return  OutputCollapsedGraph((Graph)v).toString();
                     } catch (IOException ex) {
                         Logger.getLogger(MatchingGraphs.class.getName()).log(Level.SEVERE, null, ex);
                     }
                     }
                     
                        return null;
                     
                    }
                }
        );
        graphWriter.addVertexData("idSrcOrigin", null, "0",
                new Transformer<Object, String>() {
                    public String transform(Object v) {
                        if(v.getClass().equals(ComplexVertex.class)) 
                             return ((ComplexVertex)v).getFileName(); 
                        else            
                             return "sourcesOrgin"; 
                    }
                }
        );

        graphWriter.save(g, out);
    }

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

    private class LayoutChooser implements ActionListener {

        private final JComboBox jcb;
        private final VisualizationViewer vv;

        private LayoutChooser(JComboBox jcb, VisualizationViewer vv) {
            super();
            this.jcb = jcb;
            this.vv = vv;
        }

        public void actionPerformed(ActionEvent arg0) {
            Object[] constructorArgs
                    = {collapsedGraph};

            Class<? extends Layout> layoutC
                    = (Class<? extends Layout>) jcb.getSelectedItem();
//            Class lay = layoutC;
            try {
                Layout l = null;
                Object o = null;

                if (!layoutC.equals(TreeLayout.class)) {
                    Constructor<? extends Layout> constructor = layoutC
                            .getConstructor(new Class[]{Graph.class});
                    o = constructor.newInstance(constructorArgs);
                    l = (Layout) o;
                    l.setInitializer(vv.getGraphLayout());
                    l.setSize(vv.getSize());

                } else {
                    TreeBuilder treeBuilder = new TreeBuilder((DirectedGraph) collapsedGraph);
                    l = new TreeLayout<ComplexVertex, String>(treeBuilder.getTree());
                }
                layout = l;
                LayoutTransition lt = new LayoutTransition(vv, vv.getGraphLayout(), l);
                Animator animator = new Animator(lt);
                animator.start();
                vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
                vv.repaint();

            } catch (Exception e) {
                e.printStackTrace();
            }
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

        GraphObject[] inputGraphs = new GraphObject[fileList.length];
        GraphObject go;
        for (int i = 0; i <= fileList.length - 1; i++) {
            // ce graphe je le crée pour qu il prenne uniquement les vertx structurelle 
            DirectedGraph<ComplexVertex, String> graphOfFile = new DirectedSparseMultigraph<ComplexVertex, String>();

            GraphMLReader<DirectedGraph<ComplexVertex, String>, ComplexVertex, String> graphMLReader
                    = new GraphMLReader<>(vertexFactory, edgeFactory);
            DirectedGraph<ComplexVertex, String> graphTmp = new DirectedSparseMultigraph<ComplexVertex, String>();
            graphMLReader.load(fileList[i].getPath(), graphTmp);
            Collection<ComplexVertex> graphCrtVerticesSet = new HashSet<ComplexVertex>();
            HashMap<Integer, ComplexVertex> graphVerticePredecessors = new HashMap<>();
            Map<String, GraphMLMetadata<ComplexVertex>> vertex_data = graphMLReader.getVertexMetadata(); //Our vertex Metadata is stored in a map.
            int orderInsertionVertex = 0;
            Object[] tabVertex = graphTmp.getVertices().toArray();
            Iterator itIdVertices = graphMLReader.getVertexIDs().values().iterator();
            ComplexVertex cv;
            String idVertex;

            for (int j = 0; j < tabVertex.length; j++) {
                cv = (ComplexVertex) tabVertex[j];
                idVertex = (String) itIdVertices.next();
//				System.out.println(vertex_data.get("DisplayedValue").transformer.transform(cv));
                if (vertex_data.get("Visualized").transformer.transform(cv).equals("true")) {
                    // We get the display_value <data key="DisplayedValue">display_value</data>
                    cv.set_ID(idVertex);
                    cv.setDisplayValue(vertex_data.get("DisplayedValue").transformer.transform(cv));
                    cv.setFileName(vertex_data.get("idSrcOrigin").transformer.transform(cv));
                    cv.setNumGraphSrc(i);
                    cv.setColor(colors[i]);
                    graph.addVertex(cv);
                    graphOfFile.addVertex(cv);
                    nombreVertex++;
                }
            }

            Object[] tabVertexNew = graphOfFile.getVertices().toArray();

            // imen : je dois remettre ca non commente  
            //Arrays.sort(tabVertex);	
            ComplexVertex pred;
            Iterator it1;

            for (int j = 0; j < tabVertex.length; j++) {
                cv = (ComplexVertex) tabVertex[j];
                // verifier si ce vertex existe dans le graph de structure courant 
                if (searchVertexInTable(cv, tabVertexNew)) {

                    graphCrtVerticesSet.add(cv);
                    Collection<ComplexVertex> predecessors = graphTmp.getPredecessors(cv);
//                    Dans cette collection on ajoute les predecesssurs de chaque vertex dans le meme ordre que de l ajout du vertex
                    pred = null;
                    it1 = predecessors.iterator();
                    while (it1.hasNext()) {
                        pred = (ComplexVertex) it1.next();
                    }
                    // je pense ici j avais prevu que chaque noeud a un seul predecesseur puisque l histoire des hierarchies stricte en phase 1 donc
                    // je n ai garde que le dernier pred
                    if (pred != null) {
                        cv.setPredecessor(pred);
                        graphVerticePredecessors.put(j, pred);
                    }
                }
            }

            //go = new GraphObject(graphTmp, graphTmp.getVertexCount(), graphCrtVerticesSet, graphVerticePredecessors);
            go = new GraphObject(graphOfFile, graphOfFile.getVertexCount(), graphCrtVerticesSet, graphVerticePredecessors);
            inputGraphs[i] = go;
            //inputGraphs[i] = go;

            // Selects the edges from graphTmp related to the nodes existing in _graph
            Boolean source0k, destOk;
            for (String e : graphTmp.getEdges()) {
                source0k = false;
                destOk = false;
                for (ComplexVertex n : graph.getVertices()) {
                    if (n.equals(graphTmp.getSource(e))) {
                        source0k = true;
                    }
                    if (n.equals(graphTmp.getDest(e))) {
                        destOk = true;
                    }
                    if (source0k && destOk) {
                        graph.addEdge(e, graphTmp.getSource(e), graphTmp.getDest(e));
                        break;
                    }
                }
            }
        }

        /**
         * * j ordonne ici les graphes ***
         */
        CollectionOfGraph cg = new CollectionOfGraph(inputGraphs);
        cg.sortDescSizeOfGraph();
//       System.out.println(Arrays.toString(cg.getGraphs()));
        for (int i = 0; i < cg.getGraphs().length; i++) {
            verticesPredecessorsSets.put(i, cg.getGraphs()[i].getGraphVerticePredecessors());
            verticesSets.put(i, cg.getGraphs()[i].getVertices());

        }
        System.out.println("***********le nombre de vertex est " + nombreVertex);
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

    class VertexFactory implements Factory<ComplexVertex> {

        int i = 0;

        public ComplexVertex create() {
            int j = i++;
            //return "V"+(j);
            return new ComplexVertex("" + j + "", true);
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
        f.getContentPane().add(new MatchingGraphs());
        f.pack();
        f.setVisible(true);
    }
}

