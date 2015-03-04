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
import javax.swing.JOptionPane;
import view.ViewAddAttribute;
import view.ViewAddSeveralAttribute;
import view.DataStructure;

/**
 *
 * @author Thomas
 */
public class ListenerAddAttribute implements ActionListener
{
    private ViewAddAttribute a;
    private static MatchGraph g;
    private static ViewAddSeveralAttribute s;
    
    public ListenerAddAttribute(ViewAddAttribute b)
    {
        a = b;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (a.getNameAttribute().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Please name the attribute before adding elements", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        System.out.println("Jesuislelistenner");
        a.addElementToJPanel(g.getSelectedVertex());
        g.matcherGraph();
        checkLink();
    }
    
    public static void setMatchGraph (MatchGraph ga)
    {
        g = ga;
    }
    
    public static void setSeveralAttribute(ViewAddSeveralAttribute sa)
    {
        s = sa;
    }

    private void checkLink() {
        DataStructure<String> d = new DataStructure<>();
        d.setData(s.getAllElements());
        //System.out.println(d.getData());
        // Nombre d'elements dans un attribut (le premier)
        //int nbrElement = ((ArrayList<String>)d.getElementOfAttribute(d.getData().keySet().iterator().next())).size();
        
        for ( int i = 0 ; i < 2 ; i++ )
        {
            ArrayList<String> line = d.getLine(i);
            boolean correct = true;
            for ( int j = 0 ; j+1 < d.getAttributeCount() ;j++)
            {
                if ( !(g.getSuccessors(line.get(j)).contains(line.get(j+1))))
                    correct = false;
            }
            System.out.println(line +" est " +correct);
                
        }
       
    }
}
