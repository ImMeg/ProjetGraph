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
        
        if (!data.attributeExist(a.getNameAttribute()))
            data.addAttribute(a.getNameAttribute());
        
        Iterator<String> objects = g.getSelectedVertex().iterator();

         for (String it =""; objects.hasNext() ;  ){
             it = objects.next();
             data.addElementToAttribute(a.getNameAttribute(), it);
        }
        a.addElementToJPanel(g.getSelectedVertex());
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
        
        DataStructure<String> d = new DataStructure<>();
        for ( int i = 0 ; i < data.getAttributeCount() ; i++ )
             d.addAttribute(data.getAttributeName(i));
        
         
        int cpt = 0;
        for ( int i = 0 ; i < data.getNumberOfElementInAttribute(0) ; i++ )
        {
            for ( String s : g.getSuccessors(data.getLine(i).get(0)))
            {
                if ( data.nextAttributeContainsElement(0, s))
                {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(data.getLine(i).get(0));
                    list.add(s);
                    d.addLine(cpt,list );
                }
            }
        }
        
        System.out.println("Table ordonné :" +d.getData());
       
    }
}
