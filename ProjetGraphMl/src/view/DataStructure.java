/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import graph.ComplexVertex;
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
        
    private Map<String,ArrayList<Type>> data;
    private String tableName;

    public DataStructure() {
        data = new LinkedHashMap<>();
    }
    
    public ArrayList<Type> getElementOfAttribute( String s )
    {
        return data.get(s);
    }
    
    public ArrayList<Type> getElementOfAttribute( int attributeNumber )
    {
        return data.get(getAttributeName(attributeNumber));
    }
    
    public boolean attributeContains (String attribut, Type s)
    {
        if ( data.get(attribut) != null )
            return data.get(attribut).contains(s);
        
        return false;
    }
    
    public void addAttribute (String s)
    {
        data.put(s, new ArrayList<Type>());
    }
    
    public boolean attributeExist ( String s )
    {
        return data.containsKey(s);
    }
    
    public void addElementToAttribute (String attribute, Type s)
    {
        data.get(attribute).add(s);
    }
    
    public void addElementToAttribute (int attributeNumber, Type s)
    {
        data.get(getAttributeName(attributeNumber)).add(s);
    }
    
    public ArrayList<Type> getLine ( int number )
    {
        ArrayList<Type> line = new ArrayList<>();
        for ( Object o : data.values().toArray())
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
        for ( Object o : data.values().toArray())
        {
            ArrayList<Type> a = (ArrayList<Type>)o;
            a.add(index,elements.get(i));
            i++;
        }
    }
    
    public void setData (Map<String,ArrayList<Type>> at)
    {
        data = at;
        /*System.out.println(at);
        System.out.println(attributs);*/
    }

    public Map<String,ArrayList<Type>> getData() {
        return (HashMap < String, ArrayList<Type>>) data;
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
        return data.size();
    }
    
    public String getNextAttribute (String actual )
    {
        boolean found = false;
        for ( String s : data.keySet())
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
        for ( String s : data.keySet())
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
        for ( String s : data.keySet())
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
        return data.get(attribute).size();
    }
    
    public int getNumberOfElementInAttribute ( int number )
    {
        return data.get(getAttributeName(number)).size();
    }

    public int getMaxNumberOfElementInAttribute() {
        int max = getNumberOfElementInAttribute(0);
        
        for ( int i = 1 ; i < getAttributeCount() ; i++ )
        {
            int temp = getNumberOfElementInAttribute(i);
            if ( temp > max )
                temp = max;
        }
        
        return max;
        
    }

    public void removeAll() {
        data.clear();
    }
    
    public ArrayList<Type> allInOneCollection()
    {
        ArrayList<Type> list = new ArrayList<Type>();
        for ( int i = 0 ; i < getAttributeCount() ; i++ )
        {
            for ( Type t : getElementOfAttribute(i))
            {
                if ( !list.contains(t))
                    list.add(t);
            }
        }
        return list;
    }
}
