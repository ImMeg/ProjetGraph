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
import view.DataStructure;
import view.ViewAddDimension;
import view.ViewAddFact;

/**
 *
 * @author Thomas
 */
public class ListenerAddSelectedDimension implements ActionListener
{
    private ViewAddFact vaf;
    
    private static MatchGraph g;
    private static DataStructure<ComplexVertex> data = new DataStructure<>();
    
    @Override
    public void actionPerformed(ActionEvent e) {
        vaf.addElementToJPanel(g.getSelectedVertex());
        System.out.println("YO");
        vaf.revalidate();
        //vaf.addElementToJPanel(null);
    }

    public ListenerAddSelectedDimension(ViewAddFact vaf) {
        this.vaf = vaf;
    }
    
    
    
    
    public static void setMatchGraph(MatchGraph g) {
        ListenerAddSelectedDimension.g = g;
    }
    
    public static void setDataStructure(DataStructure d) {
        ListenerAddSelectedDimension.data = d;
    }
    
}
