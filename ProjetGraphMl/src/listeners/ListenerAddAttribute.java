/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import graph.MatchGraph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import view.ViewAddAttribute;
import view.AddSeveralAttribute;
import view.DataStructure;

/**
 *
 * @author Thomas
 */
public class ListenerAddAttribute implements ActionListener
{
    private ViewAddAttribute a;
    private static MatchGraph g;
    private static AddSeveralAttribute s;
    
    public ListenerAddAttribute(ViewAddAttribute b)
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
        
       /*Collection<String> suivants = g.getSuccessors(s.getListOfAttribute().get(0).getListOfElement().get(0));
       System.out.println("suivants :" +suivants);*/
        DataStructure d = new DataStructure();
        //d.setData(s.getAllElements());
        /*System.out.println("0" +d.getLine(0));
        System.out.println("1" +d.getLine(1));*/
        ArrayList<String> yo = new ArrayList<>();
        yo.add("France");
        yo.add("Allemagne");
        
        //d.addLine(0,yo );
        System.out.println("here");
        /*
        
        d.addLine(0, yo);
        d.addLine(1, yo);
            System.out.println("and here");
            for (String s : g.getSuccessors(d.getLine(0).get(0)))
                if (s.equals( d.getLine(0).get(1)))
                    System.out.println("Ligne 0 Checked and no problem");
                else
                    System.out.println("Incoherent Ligne 0");*/
        
    }
}
