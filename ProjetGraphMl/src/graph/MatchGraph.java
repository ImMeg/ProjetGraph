/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
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
import java.util.ArrayList;
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
    PickedState<String> pickedState ;
    ArrayList<String> selectedobjects = new ArrayList<>();
    
    
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
                    System.out.println("ICI"+v.toString());
                    return ((edu.uci.ics.jung.graph.Graph) v).getVertices().toString();
                }
                System.out.println(v.toString());
                return super.transform(v);
            }
        });
        
        final EditingModalGraphMouse<String, String> graphMouse
                = new EditingModalGraphMouse<String, String>(vv.getRenderContext(), vertexFactory, edgeFactory);

        vv.setGraphMouse(graphMouse);
        
        pickedState = vv.getPickedVertexState();
        pickedState.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    String selected = (String) e.getItem();
                    if(pickedState.isPicked(selected))
                        selectedobjects.add(selected);
                }
            });
        
        vv.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    System.out.println(selectedobjects.toString());
                    selectedobjects.clear();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
        
        final Container content = getContentPane();
        GraphZoomScrollPane gzsp = new GraphZoomScrollPane(vv);
        content.add(gzsp);
        
        JComboBox modeBox = graphMouse.getModeComboBox();
        modeBox.addItemListener(graphMouse.getModeListener());
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
        
        
        
        
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

}
