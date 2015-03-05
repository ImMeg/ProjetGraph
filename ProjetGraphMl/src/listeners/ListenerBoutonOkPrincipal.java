/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import bdd.Bdd;
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
public class ListenerBoutonOkPrincipal implements ActionListener {

    static DataStructure<String> ds;
    ViewAddDimension vad;
    
    public ListenerBoutonOkPrincipal(ViewAddDimension v)
    {
        vad = v;
    }
    
    static public void setDataStructure(DataStructure<String> d)
    {
        ds = d;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       if ( vad.getTableName().isEmpty() )
        {
             JOptionPane.showMessageDialog(null, "Please name the TABLE before creating SQL file", "Information", JOptionPane.INFORMATION_MESSAGE);
             return;
        }

        Bdd.createTable(vad.getTableName(), ds.getData());
    }
    
}
