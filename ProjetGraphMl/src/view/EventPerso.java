/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 *
 * @author Thomas
 */
public class EventPerso
{
    Graph graph;
    
    public EventPerso(Graph g)
    {
        graph = g;
    }
    
    public void signalementOKAddDimension()
    {
        graph.showAddSeveralAttribute();
    }
}
