package jtree.jtree;

import javax.swing.*;
import javax.swing.tree.*;

public class NoeudArbre
       extends DefaultMutableTreeNode
{
	private int type;

	public static final int _DEFAUT      = 1;
	public static final int _FAIT        = 2;
	public static final int _MESURE      = 3;
	public static final int _DIMENSION   = 4;
	public static final int _HIERARCHIE  = 5;
	public static final int _PARAMETRE   = 6;
	public static final int _ATTR_FAIBLE = 7;
	
	public NoeudArbre(String v, int t)
	{
		super(v);
		this.type = t;
	}
	
	public int getType()
	{
		return this.type;
	}
}
