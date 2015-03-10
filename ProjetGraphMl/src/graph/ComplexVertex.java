/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

/**
 *
 * @author loicleger
 */
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

import java.awt.*;

public class ComplexVertex extends DirectedSparseMultigraph implements Comparable  {
    // Absolute id primary
    private String _ID;
    private String _SourceFileHashCode = "" ;
    // Display String
    private String _DisplayValue;
    private Color _mColor;
    private Shape _shape;
    private int numGraphSrc;
    private ComplexVertex predecessor; 

	

    public ComplexVertex(){
        set_ID("-1");
    }
    public ComplexVertex(String x) {
        set_ID(x);
        // _mColor = new Color(50, 50, 200); // THEME
    }
    public ComplexVertex(String x,boolean b ) {
        set_ID(x);
        // _mColor = new Color(50, 50, 200); // THEME
    }

    public ComplexVertex(String x, String mValue, String fileName) {
         set_ID(x);
        _DisplayValue = mValue;
        _SourceFileHashCode = fileName; //TODO + id exit file;
    }

	public ComplexVertex(String x, String mValue) {
         set_ID(x);
        _DisplayValue = mValue;
    }
	
    public String getFileName() {
        return _SourceFileHashCode;
    }

    public void setFileName(String _FileName) {
        this._SourceFileHashCode = _FileName;
    }


    public void setDisplayValue(String value) {
       _DisplayValue = value;
    }

    public String getDisplayValue()
    {
        return (_DisplayValue);
    }

    public void setColor(Color color)
    {
        _mColor = color;
    }

    public Color getColor()
    {
        return _mColor;
    }

    @Override
    public String toString()
    {
        return(_DisplayValue +"(G:"+numGraphSrc+")");
//        String vertex  = "";
//        vertex += "id" + this._ID + "\n"
//                + "displayValue" + this._DisplayValue + "\n";
//        return (vertex);
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public int getNumGraphSrc() {
        return numGraphSrc;
    }

    public void setNumGraphSrc(int numGraphSrc) {
        this.numGraphSrc = numGraphSrc;
    }

	@Override
	public int compareTo(Object t) {
           
          return _DisplayValue.toString().compareTo( ((ComplexVertex)t)._DisplayValue.toString()); 
	}

	/**
	 * @return the predecessor
	 */
	public ComplexVertex getPredecessor() {
		return predecessor;
	}

	/**
	 * @param predecessor the predecessor to set
	 */
	public void setPredecessor(ComplexVertex predecessor) {
		this.predecessor = predecessor;
	}
}

