/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import graph.ComplexVertex;
import listeners.ListenerAddAttribute;
import graph.MatchGraph;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import listeners.ListenerBoutonOkPrincipal;
import org.xml.sax.SAXException;

/**
 *
 * @author Thomas
 */
public class Lanceur
{
    static MatchGraph mg;
    public DataStructure<ComplexVertex> data;
    static ViewGraph g = new ViewGraph();
    
    public static void setMatchGraph( MatchGraph mg)
    {
        Lanceur.mg = mg;
        ListenerAddAttribute.setMatchGraph(mg);
        g.setMg(Lanceur.mg);
    }
    
    public static void main(String args[])
    {
        
            
           
            g.setVisible(true);
            
            //ListenerAddAttribute.setMatchGraph(mg);
            DataStructure<ComplexVertex> data = new DataStructure<>();

            ListenerAddAttribute.setDataStructure(data);
            ListenerBoutonOkPrincipal.setDataStructure(data);
            

        
//        
        
        
    }        
}
