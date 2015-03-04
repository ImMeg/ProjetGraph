/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import graph.MatchGraph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import view.ViewAddAttribute;
import view.ViewAddDimension;
import view.DataStructure;

/**
 *
 * @author Thomas
 */
public class ListenerAddAttribute implements ActionListener
{
    private ViewAddAttribute a;
    private static MatchGraph g;
    private static ViewAddDimension s;
    private static DataStructure<String> data = new DataStructure<>();
    
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
        
        if (!data.attributeExist(a.getNameAttribute()))
            data.addAttribute(a.getNameAttribute());
        
        Iterator<String> objects = g.getSelectedVertex().iterator();

         for (String it =""; objects.hasNext() ;  ){
             it = objects.next();
             data.addElementToAttribute(a.getNameAttribute(), it);
        }
        a.addElementToJPanel(data.getElementOfAttribute(a.getNameAttribute()));
        g.matcherGraph();
        checkLink();
    }
    
    public static void setMatchGraph (MatchGraph ga)
    {
        g = ga;
    }
    
    public static void setSeveralAttribute(ViewAddDimension sa)
    {
        s = sa;
    }

    public static void setDataStructure(DataStructure<String> data) {
        ListenerAddAttribute.data = data;
    }

    
    
    private void checkLink() {

        //System.out.println(d.getData());
        // Nombre d'elements dans un attribut (le premier)
        //int nbrElement = ((ArrayList<String>)d.getElementOfAttribute(d.getData().keySet().iterator().next())).size();
        
        for ( int i = 0 ; i < 2 ; i++ )
        {
            ArrayList<String> line = data.getLine(i);
            boolean correct = true;
            for ( int j = 0 ; j+1 < data.getAttributeCount() ;j++)
            {
                if ( !(g.getSuccessors(line.get(j)).contains(line.get(j+1))))
                    correct = false;
            }
            System.out.println(line +" est " +correct);
                
        }
       
    }
}
