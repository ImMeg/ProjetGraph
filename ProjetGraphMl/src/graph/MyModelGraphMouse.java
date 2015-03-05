/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import view.PopupAddDimension;

/**
 *
 * @author loicleger
 */
public class MyModelGraphMouse<V,E> extends AbstractPopupGraphMousePlugin implements MouseListener{

    public MyModelGraphMouse() {
        this(MouseEvent.BUTTON3_MASK);
    }
    public MyModelGraphMouse(int modifiers) {
        super(modifiers);
    }
    
    /**
     * If this event is over a station (vertex), pop up a menu to
     * allow the user to perform a few actions; else, pop up a menu over the layout/canvas
     *
     * @param e
     * 
     */
    @Override
    protected void handlePopup(MouseEvent e) {
        final VisualizationViewer<Integer,Number> vv = (VisualizationViewer<Integer,Number>)e.getSource();
        Point2D p = e.getPoint();

        //vv.getRenderContext().getBasicTransformer().inverseViewTransform(e.getPoint());

        GraphElementAccessor<Integer,Number> pickSupport = vv.getPickSupport();
        if(pickSupport != null) {
            final Integer v = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
            JPopupMenu popup = new JPopupMenu();

            popup.add(new AbstractAction("Add Dimension") {
                public void actionPerformed(ActionEvent e) {
                    new PopupAddDimension().setVisible(true);
                }
            });
            popup.show(vv, e.getX(), e.getY());
        }
    }
}
        
