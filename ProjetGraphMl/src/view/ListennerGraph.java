/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Thomas
 */
public class ListennerGraph implements ActionListener
{
    ViewGraph graph;
    
    public ListennerGraph(ViewGraph g)
    {
        graph = g;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //graph.showViewAddSeveralAttribute();
    }
}
