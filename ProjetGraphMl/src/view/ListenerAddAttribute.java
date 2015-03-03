/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import graph.MatchGraph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Thomas
 */
public class ListenerAddAttribute implements ActionListener
{
    private AddAttribute a;
    private static MatchGraph g;
    private static AddSeveralAttribute s;
    
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
        checkLink();
    }
    
    public static void setMatchGraph (MatchGraph ga)
    {
        g = ga;
    }
    
    public static void setSeveralAttribute(AddSeveralAttribute sa)
    {
        s = sa;
    }

    private void checkLink() {
       /* if ( s.getAttributeCompt() > 1 )
        {
            System.out.println(s.getAllElements());
            Collection<ArrayList<String>> liste = s.getAllElements().values();
            System.out.println(s.getAllElements().values());
            Iterator i = liste.iterator();
            for ( ; i.hasNext() ; )
            {
                ArrayList<String> al = (ArrayList<String>) i.next();
                System.out.println("test" +al);
                System.out.println(g.edgeExistBetween("France", "65"));
            }
        }*/
    }
}
