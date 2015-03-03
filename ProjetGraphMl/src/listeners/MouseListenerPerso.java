/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Thomas
 */
public class MouseListenerPerso implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int buttonDown = e.getButton();
 
        if (buttonDown == MouseEvent.BUTTON1) {
               // Bouton GAUCHE enfoncé
            System.out.println("Clic Gauche");
        } else if(buttonDown == MouseEvent.BUTTON2) {
               // Bouton du MILIEU enfoncé
            System.out.println("Clic Milieu");
        } else if(buttonDown == MouseEvent.BUTTON3) {
               // Bouton DROIT enfoncé
            System.out.println("Clic Droit");
            
            
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Yo");

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
    

