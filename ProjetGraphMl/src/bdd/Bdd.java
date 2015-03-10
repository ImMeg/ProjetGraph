/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdd;

import graph.ComplexVertex;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author loicleger
 */
public class Bdd {
    
    public static void createTable(String tableName,Map<String,ArrayList<ComplexVertex>> attributes) {
        FileWriter fw = null;
        try {
            String nomFichier = tableName+".sql";
            File fichier = new File(nomFichier);
            fw = new FileWriter(fichier.getAbsolutePath(), true);
            // le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
            BufferedWriter output = new BufferedWriter(fw);
            String requete = "create table "+tableName+" {\n "
                    + " id INT PRIMARY KEY NOT NULL AUTOINCREMENT,\n ";
            int nbAttr = 0;
            Set<String> nameAttrbutes = attributes.keySet();
            for(String attr :nameAttrbutes ) {
                
                requete += ""+attr+" VARCHAR(100)";
                nbAttr++;
                if(nameAttrbutes.size() == nbAttr) 
                    requete+= "\n}\n";
                else 
                    requete += ",\n ";
            }
            Collection<ArrayList<ComplexVertex>> values =  attributes.values();
            Iterator<ArrayList<ComplexVertex>> it = values.iterator();
            ArrayList<ArrayList<ComplexVertex>> mesValues = new ArrayList<ArrayList<ComplexVertex>>();
            ArrayList<ComplexVertex> temp;
            int k=0;
            for(;it.hasNext();) {
                temp = it.next();
                mesValues.add(k, temp);
                k++;
                
            }
            for(int i = 0 ;i< mesValues.get(0).size() ;i++) {
                
                requete += "INSERT INTO "+tableName+" VALUES (";
                for(int j =0;j<mesValues.size();j++) {
                    requete += mesValues.get(j).get(i).getDisplayValue();
                    if(j == (mesValues.size()-1))
                        requete += ")";
                     else
                        requete+= ",";
                }
                requete += ";\n";       
            }
            
            
            //on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
            output.write(requete);
            //on peut utiliser plusieurs fois methode write
            output.flush();
            //ensuite flush envoie dans le fichier, ne pas oublier cette methode pour le BufferedWriter
            output.close();
            //et on le ferme
            System.out.println("fichier créé");
        } catch (IOException ex) {
            Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
