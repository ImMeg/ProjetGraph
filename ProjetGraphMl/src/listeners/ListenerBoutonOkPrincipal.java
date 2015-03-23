/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import bdd.Bdd;
import graph.ComplexVertex;
import graph.MatchGraph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import view.DataStructure;
import view.ViewAdd;
import view.ViewAddAttribute;
import view.ViewAddDimension;

/**
 *
 * @author Thomas
 */
public class ListenerBoutonOkPrincipal implements ActionListener {

    private static MatchGraph g;
    static DataStructure<ComplexVertex> ds;
    ViewAddDimension vad;
    static ViewAdd view;
    
    public ListenerBoutonOkPrincipal(ViewAddDimension v)
    {
        vad = v;
    }

    public static void setMatchGraph(MatchGraph g) {
        ListenerBoutonOkPrincipal.g = g;
    }

    
    
    public static void setView(ViewAdd view) {
        ListenerBoutonOkPrincipal.view = view;
    }
    
    
    
    static public void setDataStructure(DataStructure<ComplexVertex> d)
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
        ds.setTableName(vad.getTableName());
        Bdd.createTable(vad.getTableName(), checkLink().getData());
        
        g.matcherGraph(ds.allInOneCollection(),ds.getTableName());
        ds.removeAll();
        
        miseAZero();
    }
    
    private ArrayList<ComplexVertex> createLineWith ( ComplexVertex s1 , ComplexVertex s2 )
    {
        ArrayList<ComplexVertex> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);
        return list;
    }
    
    public DataStructure<ComplexVertex> checkLink() {
        
        DataStructure<ComplexVertex> d = new DataStructure<>();
        for ( int i = 0 ; i < ds.getAttributeCount() ; i++ )
             d.addAttribute(ds.getAttributeName(i));
        
        /* Compteur de ligne ajoutée */
        int cpt = 0;
        /* Définit si un element père n'a pas de fils */
        boolean ajoute = false;
        
        /* On ajoute les lignes a chaque fois qu'un element père trouve un de ses fils */
        for ( int i = 0 ; i < ds.getNumberOfElementInAttribute(0) ; i++ )
        {
            ajoute = false;
            for ( ComplexVertex cv : g.getSuccessors(ds.getLine(i).get(0)))
            {
                if ( ds.nextAttributeContainsElement(0, cv))
                {
                    d.addLine(cpt,createLineWith(ds.getLine(i).get(0), cv) );
                    ajoute = true;
                    cpt++;
                }
            }
            
            /* Si un père n'a pas de fils, on l'ajoute en mettant null comme fils */
            if ( !ajoute )
            {
               // if ( ds.getAttributeIndex(a.getNameAttribute()) == 0 )
                    d.addLine(cpt,createLineWith(ds.getLine(i).get(0), null)  );
               /* else
                {
                    d.addLine(cpt,createLineWith(null,ds.getLine(i).get(0)));
                    System.out.println("B");
                }*/
                
                cpt++;
            }
        }
        
        /* Pour les elements fils (Element situé dans l'attribut de droite dans la view )*/
        if ( ds.getAttributeCount() > 1 )
        {
            for ( ComplexVertex i : ds.getElementOfAttribute(ds.getAttributeName(1)))
            {
                if ( !(d.getElementOfAttribute(ds.getAttributeName(1)).contains(i)))
                {
                    d.addLine(cpt,createLineWith(null, i) );
                     System.out.println("C");
                }
            }
        } 
        System.out.println("Table ordonné :" +d.getData());
       

        return d;
    }

    private void miseAZero() {
    }
    
}
