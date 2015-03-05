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
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.VisualizationViewer.GraphMouse;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;
import edu.uci.ics.jung.visualization.subLayout.TreeCollapser;
import edu.uci.ics.jung.visualization.util.PredicatedParallelEdgeIndexFunction;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.List;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.event.MenuDragMouseEvent;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;
import listeners.MouseListenerPerso;

/**
 *
 * @author loicleger
 */
public class MatchGraph extends JApplet {
        private static DirectedGraph<String, String> graph = new DirectedSparseMultigraph<String, String>();

    VisualizationViewer vv;

    Layout layout;

    GraphCollapser collapser;

    Factory<String> vertexFactory = new MatchGraph.VertexFactory();
    Factory<String> edgeFactory = new MatchGraph.EdgeFactory();
    
    public MatchGraph() {
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
            graph.addVertex("QUANTITY");
            graph.addVertex("90");
            graph.addVertex("65");
            graph.addVertex("1300");
            graph.addEdge("Q->90", "QUANTITY", "90");
            graph.addEdge("Fr->90", "Allemagne", "90");
            graph.addEdge("Q->65", "QUANTITY", "65");
            graph.addEdge("2012->65", "2012", "65");
            graph.addEdge("Q->1300", "QUANTITY", "1300");
            graph.addEdge("Fr->1300", "Chine", "1300");
            graph.addEdge("Fr->65", "France", "65");
            graph.addEdge("2012->90", "2012", "90");
            graph.addEdge("2012->1300", "2012", "1300");

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
        final EditingModalGraphMouse<String, String> graphMouse
                = new MyModelGraphMouse<String, String>(vv.getRenderContext(), vertexFactory, edgeFactory);

         vv.setGraphMouse(graphMouse);

        vv.addMouseListener(new MouseListenerPerso());
        
        final Container content = getContentPane();
        GraphZoomScrollPane gzsp = new GraphZoomScrollPane(vv);
        content.add(gzsp);
        
        JComboBox modeBox = graphMouse.getModeComboBox();
        modeBox.addItemListener(graphMouse.getModeListener());
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
        
        
        
        
    }
    
    public HashSet<String> getSelectedVertex()
    {
        HashSet<String> selectedObject = new HashSet(vv.getPickedVertexState().getPicked());
        return selectedObject;
    }
    
    public void matcherGraph() {
//       graph.addVertex(title);
//       for(String s : selected ) {
//           //Gestion des predecesseur
//           for (String pred : graph.getPredecessors(s)) {
//               if(!selected.contains(pred))                
//                    graph.addEdge(pred+"->"+title, pred, title);
//                graph.removeEdge(graph.findEdge(pred, s));
//               }
//           
//            //Gestion des successeurs
//           for (String succ : graph.getSuccessors(s)) {
//               if(!selected.contains(succ))                
//                    graph.addEdge(title+"->"+succ, title, succ);
//                graph.removeEdge(graph.findEdge(s, succ));
//               }
//           graph.removeVertex(s);
        
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
    
    public boolean edgeExistBetween( String s1 ,String s2)
    {
        return (graph.findEdge(s1, s2) != null || graph.findEdge(s2, s1) != null);
    }

}
