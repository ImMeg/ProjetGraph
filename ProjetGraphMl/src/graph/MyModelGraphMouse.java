/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import java.awt.PopupMenu;
import java.awt.event.MouseEvent;
import org.apache.commons.collections15.Factory;

/**
 *
 * @author loicleger
 */
public class MyModelGraphMouse extends EditingModalGraphMouse<String, String>{

    public MyModelGraphMouse(RenderContext<String,String> r,Factory<String> vertexFactory, Factory<String> edgeFactory) {
        super(r,vertexFactory, edgeFactory);
    }
    
    protected void handlePopUp(MouseEvent e) {
        final VisualizationViewer vv =
        (VisualizationViewer)e.getSource();
        vv.add(new PopupMenu("Salut"));
    }
}
