/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import listeners.ListenerAddAttribute;
import graph.MatchGraph;

/**
 *
 * @author Thomas
 */
public class Lanceur
{
    public static void main(String args[])
    {
        MatchGraph mg = new MatchGraph();
        ViewGraph g = new ViewGraph(mg);
        g.setVisible(true);
        ViewAdd a = new ViewAdd();
        a.setVisible(true);
        
        ListenerAddAttribute.setMatchGraph(mg);
    }
    
        
}
