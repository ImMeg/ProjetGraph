package jtree.jtree;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

public class RenduArbre 
	   extends DefaultTreeCellRenderer 
{	
	public RenduArbre() 
	{
		super();
	}
  
	public Component getTreeCellRendererComponent(JTree arbre, Object noeud, boolean selection, 
                                                  boolean ouvert, boolean feuille, int ligne, boolean focus)
	{
		NoeudArbre nde = (NoeudArbre)noeud;
		// Mise en place icone
		switch (nde.getType())
		{
			case NoeudArbre._FAIT :
				this.setIcon(new ImageIcon("./pictures/green.png"));
				break;
			case NoeudArbre._MESURE :
				this.setIcon(new ImageIcon("./pictures/measure.jpg"));
				break;
			case NoeudArbre._DIMENSION :
				this.setIcon(new ImageIcon("./pictures/red.png"));
				break;
			case NoeudArbre._HIERARCHIE :
				this.setIcon(new ImageIcon("./pictures/yellow.png"));
				break;
			case NoeudArbre._PARAMETRE :
				this.setIcon(new ImageIcon("./pictures/param.jpg"));
				break;
			case NoeudArbre._ATTR_FAIBLE :
				this.setIcon(new ImageIcon("./pictures/weak.jpg"));
				break;
			default :
				this.setIcon(new ImageIcon("./pictures/graphite.png"));
				break;
		}
		// Mise en place texte
		String	labelText = (String)nde.getUserObject();
		this.setText(labelText);
		return this;
	}     
}
