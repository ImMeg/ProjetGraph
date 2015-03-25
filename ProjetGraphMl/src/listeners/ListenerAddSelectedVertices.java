/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import graph.ComplexVertex;
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
public class ListenerAddSelectedVertices implements ActionListener
{
    private ViewAddAttribute a;
    private static MatchGraph g;
    private static ViewAddDimension s;
    private static DataStructure<ComplexVertex> data = new DataStructure<>();
    
    public ListenerAddSelectedVertices(ViewAddAttribute b)
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
        
        Iterator<ComplexVertex> objects = g.getSelectedVertex().iterator();

        for (ComplexVertex it = null; objects.hasNext() ;  ){
             it = objects.next();
             data.addElementToAttribute(a.getNameAttribute(), it);
        }
        a.addElementToJPanel(g.getSelectedVertex());
        //g.matcherGraph(data.getElementOfAttribute(a.getNameAttribute()));
        //checkLink();
    }
    
    public static void setMatchGraph (MatchGraph ga)
    {
        g = ga;
    }
    
    public static void setSeveralAttribute(ViewAddDimension sa)
    {
        s = sa;
    }

    public static void setDataStructure(DataStructure<ComplexVertex> data) {
        ListenerAddSelectedVertices.data = data;
    }

    /**
     * Pour l'instant valide uniquement avec deux attributs
     */
    
    
    /** Uniquement valable pour une dataStructure ayant maxi deux attributs !!
     * @return retourne une DataStructure qui prend en compte les liens de parentés entre les élements */
    
}
