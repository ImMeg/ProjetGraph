/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Thomas
 */
public class DataStructure <Type>
{
        
    Map<String,ArrayList<Type>> attributs;
    private String tableName;

    public DataStructure() {
        attributs = new LinkedHashMap<>();
    }
    
    public ArrayList<Type> getElementOfAttribute( String s )
    {
        return attributs.get(s);
    }
    
    public boolean attributeContains (String attribut, Type s)
    {
        if ( attributs.get(attribut) != null )
            return attributs.get(attribut).contains(s);
        
        return false;
    }
    
    public void addAttribute (String s)
    {
        attributs.put(s, new ArrayList<Type>());
    }
    
    public boolean attributeExist ( String s )
    {
        return attributs.containsKey(s);
    }
    
    public void addElementToAttribute (String attribute, Type s)
    {
        attributs.get(attribute).add(s);
    }
    
    public void addElementToAttribute (int attributeNumber, Type s)
    {
        attributs.get(getAttributeName(attributeNumber)).add(s);
    }
    
    public ArrayList<Type> getLine ( int number )
    {
        ArrayList<Type> line = new ArrayList<>();
        for ( Object o : attributs.values().toArray())
        {
            ArrayList<Type> a = (ArrayList<Type>)o;
            //System.out.println("A est :" +a);
            if ( a.size() > number )
                line.add(a.get(number));
            else
                line.add(null);
        }
        
        //System.out.println(line);
        return line;
    }
    
    public void addLine ( int index , ArrayList<Type> elements )
    {
        int i = 0;
        for ( Object o : attributs.values().toArray())
        {
            ArrayList<Type> a = (ArrayList<Type>)o;
            a.add(index,elements.get(i));
            i++;
        }
    }
    
    public void setData (Map<String,ArrayList<Type>> at)
    {
        attributs = at;
        /*System.out.println(at);
        System.out.println(attributs);*/
    }

    public Map<String,ArrayList<Type>> getData() {
        return (HashMap < String, ArrayList<Type>>) attributs;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public int getAttributeCount()
    {
        //System.out.println("Il y a actuellement" + attributs.size() +"Attributes");
        return attributs.size();
    }
    
    public String getNextAttribute (String actual )
    {
        boolean found = false;
        for ( String s : attributs.keySet())
        {
            if ( found )
                return s;
                
            if (s.equals(actual))
                found = true;
        }
        
        return null;
    }
    
    public String getAttributeName ( int attributeNumber )
    {
        int i = 0;
        for ( String s : attributs.keySet())
        {     
            if (i == attributeNumber)
                return s;
            
            i++;
        }
        
        return null;
    }
    
    public int getAttributeIndex ( String name )
    {
        int i = 0;
        for ( String s : attributs.keySet())
        {     
            if (s.equals(name))
                return i;
            i++;
        }
        return -1;
    }
    
    public boolean nextAttributeContainsElement (String actual, Type t )
    {
        return ( attributeContains(getNextAttribute(actual),t) );
    }
    
    public boolean nextAttributeContainsElement ( int attributeNumber, Type t )
    {
        return ( attributeContains(getAttributeName(attributeNumber+1),t) );
    }
    
    public int getNumberOfElementInAttribute ( String attribute )
    {
        return attributs.get(attribute).size();
    }
    
    public int getNumberOfElementInAttribute ( int number )
    {
        return attributs.get(getAttributeName(number)).size();
    }
}
