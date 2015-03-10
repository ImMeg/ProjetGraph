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
    public static void main(String args[])
    {
        JFrame frameLance = new JFrame();
        
        JFileChooser fc = new JFileChooser();
        int returnVal=fc.showOpenDialog(frameLance);
        File filetoCharge;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filetoCharge = fc.getSelectedFile().getAbsoluteFile();
            MatchGraph mg = new MatchGraph();
            ViewGraph g = new ViewGraph(mg);
            try {
                mg.openGraphML(filetoCharge);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Lanceur.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(Lanceur.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Lanceur.class.getName()).log(Level.SEVERE, null, ex);
            }
            g.setVisible(true);
            ViewAdd a = new ViewAdd();
            a.setVisible(true);
            ListenerAddAttribute.setMatchGraph(mg);
            DataStructure<ComplexVertex> data = new DataStructure<>();
            ListenerAddAttribute.setDataStructure(data);
            ListenerBoutonOkPrincipal.setDataStructure(data);
        }
       else {
            
        }
        
//        
        
        
    }        
}
