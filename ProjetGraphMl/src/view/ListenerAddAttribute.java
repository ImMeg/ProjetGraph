/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import graph.MatchGraph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Thomas
 */
public class ListenerAddAttribute implements ActionListener
{
    private AddAttribute a;
    private static MatchGraph g;
    
    public ListenerAddAttribute(AddAttribute b)
    {
        a = b;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        System.out.println("Jesuislelistenner");
        a.addElementToJPanel(g.getSelectedVertex());
        g.matcherGraph();
    }
    
    public static void setMatchGraph (MatchGraph ga)
    {
        g = ga;
    }
    
}
