/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Thomas
 */
public class DataStructure 
{
    private HashMap<String,ArrayList<String>> attributs;
    private String tableName;

    public DataStructure() {
        attributs = new HashMap<>();
    }
    
    public ArrayList<String> getElementOfAttribute( String s )
    {
        return attributs.get(s);
    }
    
    public boolean attributeContains (String attribut, String s)
    {
        if ( attributs.get(attribut) != null )
            return attributs.get(attribut).contains(s);
        
        return false;
    }
    
    public void addAttribute (String s)
    {
        attributs.put(s, new ArrayList<String>());
    }
    
    public void addElementToAttribute (String attribute, String s)
    {
        attributs.get(attribute).add(s);
    }
    
    
    public ArrayList<String> getLine ( int number )
    {
        ArrayList<String> line = new ArrayList<>();
        for ( Object o : attributs.values().toArray())
        {
            ArrayList<String> a = (ArrayList<String>)o;
            //System.out.println("A est :" +a);
            if ( a.size() > number )
                line.add(a.get(number));
            else
                line.add(" ");
        }
                
        return line;
    }
    
    public void addLine ( int index , ArrayList<String> elements )
    {
        int i = 0;
        for ( Object o : attributs.values().toArray())
        {
            ArrayList<String> a = (ArrayList<String>)o;
            a.add(index,elements.get(i));
            i++;
        }
    }
    
    public void setData (HashMap<String,ArrayList<String>> at)
    {
        attributs = at;
        /*System.out.println(at);
        System.out.println(attributs);*/
    }

    public HashMap<String,ArrayList<String>> getData() {
        return attributs;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public int getAttributeCount()
    {
        System.out.println("Il y a actuellement" + attributs.size() +"Attributes");
        return attributs.size();
    }
    
}
