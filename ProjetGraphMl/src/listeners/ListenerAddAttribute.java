/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import view.DataStructure;
import view.ViewAddAttribute;
import view.ViewAddDimension;

/**
 *
 * @author Thomas
 */
public class ListenerAddAttribute implements ActionListener
{

    private ViewAddDimension vad;
    static private DataStructure d;

    public static void setDataStructure(DataStructure d) {
        ListenerAddAttribute.d = d;
    }

    public ListenerAddAttribute(ViewAddDimension v) {
        this.vad = v;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String nom = JOptionPane.showInputDialog(
                null,
                "Set new attribute name", "New Attribute",
                JOptionPane.QUESTION_MESSAGE
        );
        
        if ( !nom.isEmpty())
        {
            vad.addAttribute(nom);
            d.addAttribute(nom);
        }
        System.out.println("Action : + ");
    }
    
}
