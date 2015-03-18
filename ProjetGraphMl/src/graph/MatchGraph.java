/*/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 **/
package graph;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.GraphMLMetadata;
import edu.uci.ics.jung.io.GraphMLReader;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;
import edu.uci.ics.jung.visualization.util.PredicatedParallelEdgeIndexFunction;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;
import org.xml.sax.SAXException;

/**
 *
 * @author loicleger
 */
public class MatchGraph extends JApplet {
        private static DirectedGraph<ComplexVertex, String> graph = new DirectedSparseMultigraph<ComplexVertex, String>();

    VisualizationViewer vv;

    Layout layout;

    GraphCollapser collapser;

    Factory<ComplexVertex> vertexFactory = new MatchGraph.VertexFactory();
    Factory<String> edgeFactory = new MatchGraph.EdgeFactory();
    
    public MatchGraph(File filetoCharge) {
            try {
                this.openGraphML(filetoCharge);
//            graph.addVertex("YEAR");
//            graph.addVertex("2010");
//            graph.addVertex("2011");
//            graph.addVertex("2012");
//            graph.addEdge("edge1", "YEAR", "2010");
//            graph.addEdge("edge2", "YEAR", "2011");
//            graph.addEdge("edge3", "YEAR", "2012");
//            graph.addVertex("COUNTRY");
//            graph.addVertex("Allemagne");
//            graph.addVertex("Chine");
//            graph.addVertex("France");
//            graph.addEdge("edge4", "COUNTRY", "Allemagne");
//            graph.addEdge("edge5", "COUNTRY", "Chine");
//            graph.addEdge("edge6", "COUNTRY", "France");
//            graph.addVertex("QUANTITY");
//            graph.addVertex("90");
//            graph.addVertex("65");
//            graph.addVertex("1300");
//            graph.addEdge("Q->90", "QUANTITY", "90");
//            graph.addEdge("Fr->90", "Allemagne", "90");
//            graph.addEdge("Q->65", "QUANTITY", "65");
//            graph.addEdge("2012->65", "2012", "65");
//            graph.addEdge("Q->1300", "QUANTITY", "1300");
//            graph.addEdge("Fr->1300", "Chine", "1300");
//            graph.addEdge("Fr->65", "France", "65");
//            graph.addEdge("2012->90", "2012", "90");
//            graph.addEdge("2012->1300", "2012", "1300");
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(MatchGraph.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(MatchGraph.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MatchGraph.class.getName()).log(Level.SEVERE, null, ex);
            }

        Dimension preferredSize = new Dimension(500, 500);
        //layout = new StaticLayout(graph,preferredSize);
        layout = new CircleLayout(graph);
        layout.setSize(preferredSize);
        //layout.setGraph(graph);
        final VisualizationModel visualizationModel
                = new DefaultVisualizationModel(layout, preferredSize);

        vv = new VisualizationViewer(visualizationModel, preferredSize);
        
         vv.getRenderContext().setVertexShapeTransformer(new MatchGraph.ClusterVertexShapeFunction());
        vv.getRenderContext().setVertexFillPaintTransformer(new MatchGraph.ClusterVertexColorFunction());
        
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
                if (v instanceof edu.uci.ics.jung.graph.Graph) {
                    return ((edu.uci.ics.jung.graph.Graph) v).getVertices().toString();
                }
                return super.transform(v);
            }
        });
        
        
//        final EditingModalGraphMouse<String, String> graphMouse
//                = new EditingModalGraphMouse<String, String>(vv.getRenderContext(), vertexFactory, edgeFactory);
//        final EditingModalGraphMouse<String, String> graphMouse
//                = new EditingModalGraphMouse<String, String>(vv.getRenderContext(), vertexFactory, edgeFactory);
        AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<String, String>();

        graphMouse.add(new MyModelGraphMouse());
        vv.setGraphMouse(graphMouse);
        

        //vv.addMouseListener(new MouseListenerPerso());
        
        final Container content = getContentPane();
        GraphZoomScrollPane gzsp = new GraphZoomScrollPane(vv);
        content.add(gzsp);
        
        JComboBox modeBox = graphMouse.getModeComboBox();
        modeBox.addItemListener(graphMouse.getModeListener());
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
        
        
        
        
    }
    
    public HashSet<ComplexVertex> getSelectedVertex()
    {
        HashSet<ComplexVertex> selectedObject = new HashSet(vv.getPickedVertexState().getPicked());
        return selectedObject;
    }
    
    public void matcherGraph() {

        
        Collection picked = new HashSet(vv.getPickedVertexState().getPicked());

        GraphCollapser mycollapser = new GraphCollapser(graph);
        Graph inGraph = layout.getGraph();
        Graph clusterGraph = mycollapser.getClusterGraph(inGraph, picked);
        Graph g = mycollapser.collapse(layout.getGraph(), clusterGraph);

        double sumx = 0;
        double sumy = 0;
        // tu vois ce code récupère la position de tous les vertex comme étant des object donc il te suffit de lire les éléments de picked :) 
        for (Object v : picked) {
            Point2D p = (Point2D) layout.transform(v.toString());
            sumx += p.getX();
            sumy += p.getY();
        }
        Point2D cp = new Point2D.Double(sumx / picked.size(), sumy / picked.size());
        vv.getRenderContext().getParallelEdgeIndexFunction().reset();
        layout.setGraph(g);
        layout.setLocation(clusterGraph, cp);
        vv.getPickedVertexState().clear();
        vv.repaint();
               
    } 

    
    public void matcherGraph( ArrayList<ComplexVertex> picked ) {

//        GraphCollapser mycollapser = new GraphCollapser(graph);
//        Graph inGraph = layout.getGraph();
//        Graph clusterGraph = mycollapser.getClusterGraph(inGraph, picked);
//        Graph g = mycollapser.collapse(layout.getGraph(), clusterGraph);
//
//        double sumx = 0;
//        double sumy = 0;
//        // tu vois ce code récupère la position de tous les vertex comme étant des object donc il te suffit de lire les éléments de picked :) 
//        for (Object v : picked) {
//            Point2D p = (Point2D) layout.transform(v.toString());
//            sumx += p.getX();
//            sumy += p.getY();
//        }
//        Point2D cp = new Point2D.Double(sumx / picked.size(), sumy / picked.size());
//        vv.getRenderContext().getParallelEdgeIndexFunction().reset();
//        layout.setGraph(g);
//        layout.setLocation(clusterGraph, cp);
//        vv.getPickedVertexState().clear();
//        System.out.println(layout.getGraph());
//        vv.repaint();
        ComplexVertex c1 = new ComplexVertex("1","MATCH");
        graph.addVertex(c1);
        for(ComplexVertex c : picked ) {
            //Gestion des predecesseur
            for (ComplexVertex pred : graph.getPredecessors(c)) {
                if(!picked.contains(pred))                
                    graph.addEdge(pred.toString()+"->"+"MATCH", pred,c1);
                    graph.removeEdge(graph.findEdge(pred, c));
                }

             //Gestion des successeurs
            for (ComplexVertex succ : graph.getSuccessors(c)) {
                if(!picked.contains(succ))                
                     graph.addEdge(c1.toString()+"->"+succ.toString(), c1, succ);
                 graph.removeEdge(graph.findEdge(c, succ));
                }
            graph.removeVertex(c);
        }
        System.out.println(graph.toString());
        vv.repaint();       
    } 
    
    
    
    
     class VertexFactory implements Factory<ComplexVertex> {

        int i = 0;

        public ComplexVertex create() {
            int j = i++;
            return new ComplexVertex("" + j + "", true);
        }
    }

    class EdgeFactory implements Factory<String> {

        int i = 0;

        public String create() {

            return "V" + (i++);
        }
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
        if (v instanceof edu.uci.ics.jung.graph.Graph) {
            int size = ((edu.uci.ics.jung.graph.Graph) v).getVertexCount();
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
            if (v instanceof edu.uci.ics.jung.graph.Graph) {
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
            if (v instanceof edu.uci.ics.jung.graph.Graph) {
                return 30;
            }
            return size;
        }
    }
    
    public String searchVertexInTable( Object[] tableOfVertex) {
        Object[] tabVertex = graph.getVertices().toArray();
        
        
        for ( int i = 0 ; i < tableOfVertex.length ; i++ )
        for (Object s : tabVertex) {
            if (s.equals(tableOfVertex[i])) {
                return s.toString();
            }

        }
        System.out.println("Vertex non trouvé");
        return null;
        
    }
    
        /* Ajout de Thomas */
    public Collection<String> getAllEdges ()
    {
        return graph.getEdges();
    }
    
    public boolean edgeExistBetween( ComplexVertex c1 ,ComplexVertex c2)
    {
        return (graph.findEdge(c1, c2) != null || graph.findEdge(c1, c1) != null);
    }
    
    public Collection<ComplexVertex> getPredecessors (ComplexVertex c)
    {
        return (graph.getPredecessors(c));
    }
    
    public Collection<ComplexVertex> getSuccessors (ComplexVertex c)
    {
        return (graph.getSuccessors(c));
    }
    
     public void openGraphML(File file) throws ParserConfigurationException, SAXException, IOException {

        Color color;
        Random rand = new Random();
        float r, g, b;
        r = rand.nextFloat();
        g = rand.nextFloat();
        b = rand.nextFloat();
         color = new Color(r, g, b);
        int nombreVertex = 0;

        //ce graphe je le crée pour qu il prenne uniquement les vertx structurelle 
       GraphMLReader<DirectedGraph<ComplexVertex, String>, ComplexVertex, String> graphMLReader
               = new GraphMLReader<>(vertexFactory, edgeFactory);
       DirectedGraph<ComplexVertex, String> graphTmp = new DirectedSparseMultigraph<ComplexVertex, String>();
       graphMLReader.load(file.getPath(), graphTmp);
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
                cv.setColor(color);
                graph.addVertex(cv);
                //graphOfFile.addVertex(cv);
                nombreVertex++;
                
            }
        }	
            ComplexVertex pred;
            Iterator it1;

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
}
