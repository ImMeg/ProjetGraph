package jtree.jtree;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.sql.*;
import jtree.Configurations;

public class JArbreBD
       extends JPanel
{
	public JArbreBD()
	{
		// Constructeur parent
		super(new GridLayout(1,0));
		
		// Construction arbre
		if (Configurations._CNX != null)
		{
			// Cr�ation noeuds
			NoeudArbre racine = new NoeudArbre("@"+Configurations._SID, NoeudArbre._DEFAUT);
			creerNoeuds(racine);
			JTree arbre = new JTree(racine);
			this.add(new JScrollPane(arbre, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			                                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
			// Mise en forme des noeuds (apparence)
			arbre.setCellRenderer(new RenduArbre());
		}
		else
		{
			System.err.println("ERREUR");
			System.err.println("--> Exception   : Test d�clench�");
			System.err.println("--> Classe      : JArbreBD");
			System.err.println("--> M�thode     : JArbreBD()");
			System.err.print  ("--> Message     : "); System.err.println("Configurations._CNX is null");
			System.exit(-1);
		}
	}

	private void creerNoeuds(NoeudArbre racine)
	{
		// Cr�ation de la meta-structure
		NoeudArbre faits = new NoeudArbre("Facts", NoeudArbre._DEFAUT);
		racine.add(faits);
		NoeudArbre dimensions = new NoeudArbre("Dimensions", NoeudArbre._DEFAUT);
		racine.add(dimensions);
		NoeudArbre etoiles = new NoeudArbre("Stars", NoeudArbre._DEFAUT);
		racine.add(etoiles);
		creerNoeudsFaits(faits);
		creerNoeudsDimensions(dimensions);
		creerNoeudsEtoiles(etoiles);
	}


	private void creerNoeudsFaits(NoeudArbre racine)
	{
		try
		{
			Statement st1 = Configurations._CNX.createStatement();
			ResultSet rs1 = st1.executeQuery(
				"select FACT_NAME " +
				"from USER_FACT"
				);
			while (rs1.next())
			{
				String txt_f = rs1.getString("FACT_NAME");
				NoeudArbre f = new NoeudArbre(txt_f, NoeudArbre._FAIT);
				racine.add(f);
				Statement st2 = Configurations._CNX.createStatement();
				ResultSet rs2 = st2.executeQuery(
					"select MEASURE_NAME " +
					"from USER_MEASURE " +
					"where FACT_NAME = '"+txt_f+"'"
					);
				while (rs2.next())
				{
					NoeudArbre m = new NoeudArbre(rs2.getString("MEASURE_NAME"), NoeudArbre._MESURE);
					f.add(m);
				}
				rs2.close();
				st2.close();
			}
			rs1.close();
			st1.close();
		}
		catch (SQLException ex)
		{
			System.err.println("ERREUR");
			System.err.println("--> Exception   : SQLException");
			System.err.println("--> Classe      : JArbreBD");
			System.err.println("--> M�thode     : creerNoeudsFaits");
			System.err.print  ("--> Message JVM : "); System.err.println(ex.getMessage());
			System.err.print  ("--> Code    JVM : "); System.err.println(ex.getErrorCode());
			System.exit(-1);
		}
	}

	private void creerNoeudsDimensions(NoeudArbre racine)
	{
		try
		{
			Statement st1 = Configurations._CNX.createStatement();
			ResultSet rs1 = st1.executeQuery(
				"select DIMENSION_NAME " +
				"from USER_DIMENSION"
				);
			while (rs1.next())
			{
				String txt_d = rs1.getString("DIMENSION_NAME");
				NoeudArbre d = new NoeudArbre(txt_d, NoeudArbre._DIMENSION);
				racine.add(d);
				Statement st2 = Configurations._CNX.createStatement();
				ResultSet rs2 = st2.executeQuery(
					"select HIERARCHY_NAME " +
					"from USER_HIERARCHY " +
					"where DIMENSION_NAME = '"+txt_d+"'"
					);
				while (rs2.next())
				{
					String txt_h = rs2.getString("HIERARCHY_NAME");
					NoeudArbre h = new NoeudArbre(txt_h, NoeudArbre._HIERARCHIE);
					d.add(h);
					creerNoeudsParametres(h, txt_d, txt_h);
				}
				rs2.close();
				st2.close();
			}
			rs1.close();
			st1.close();
		}
		catch (SQLException ex)
		{
			System.err.println("ERREUR");
			System.err.println("--> Exception   : SQLException");
			System.err.println("--> Classe      : JArbreBD");
			System.err.println("--> M�thode     : creerNoeudsDimensions");
			System.err.print  ("--> Message JVM : "); System.err.println(ex.getMessage());
			System.err.print  ("--> Code    JVM : "); System.err.println(ex.getErrorCode());
			System.exit(-1);
		}
	}
	
	private void creerNoeudsParametres(NoeudArbre racine, String txt_d, String txt_h)
	{
		try
		{
			Statement st1 = Configurations._CNX.createStatement();
			ResultSet rs1 = st1.executeQuery(
				"select PARAMETER_NAME " +
				"from USER_LEVEL " +
				"where DIMENSION_NAME = '"+txt_d+"' " +
				"  and HIERARCHY_NAME = '"+txt_h+"' " +
				"order by POSITION"
				);
			while (rs1.next())
			{
				String txt_p = rs1.getString("PARAMETER_NAME");
				NoeudArbre p = new NoeudArbre(txt_p, NoeudArbre._PARAMETRE);
				racine.add(p);
				Statement st2 = Configurations._CNX.createStatement();
				ResultSet rs2 = st2.executeQuery(
					"select WEAK_ATTRIBUTE_NAME " +
					"from USER_WEAK_ATTRIBUTE "+
					"where DIMENSION_NAME = '"+txt_d+"' " +
					"  and HIERARCHY_NAME = '"+txt_h+"' " +
					"  and PARAMETER_NAME = '"+txt_p+"' "
					);
				while (rs2.next())
				{
					NoeudArbre w = new NoeudArbre(rs2.getString("WEAK_ATTRIBUTE_NAME"), NoeudArbre._ATTR_FAIBLE);
					p.add(w);
				}
				rs2.close();
				st2.close();
			}
			rs1.close();
			st1.close();
		}
		catch (SQLException ex)
		{
			System.err.println("ERREUR");
			System.err.println("--> Exception   : SQLException");
			System.err.println("--> Classe      : JArbreBD");
			System.err.println("--> M�thode     : creerNoeudsParametres");
			System.err.print  ("--> Message JVM : "); System.err.println(ex.getMessage());
			System.err.print  ("--> Code    JVM : "); System.err.println(ex.getErrorCode());
			System.exit(-1);
		}
	}

	private void creerNoeudsEtoiles(NoeudArbre racine)
	{
		try
		{
			Statement st1 = Configurations._CNX.createStatement();
			ResultSet rs1 = st1.executeQuery(
				"select FACT_NAME " +
				"from USER_FACT"
				);
			while (rs1.next())
			{
				String txt_f = rs1.getString("FACT_NAME");
				NoeudArbre f = new NoeudArbre(txt_f, NoeudArbre._FAIT);
				racine.add(f);
				Statement st2 = Configurations._CNX.createStatement();
				ResultSet rs2 = st2.executeQuery(
					"select DIMENSION_NAME " +
					"from USER_STAR " +
					" where FACT_NAME = '"+txt_f+"'"
					);
				while (rs2.next())
				{
					NoeudArbre d = new NoeudArbre(rs2.getString("DIMENSION_NAME"), NoeudArbre._DIMENSION);
					f.add(d);
				}
				rs2.close();
				st2.close();
			}
			rs1.close();
			st1.close();
		}
		catch (SQLException ex)
		{
			System.err.println("ERREUR");
			System.err.println("--> Exception   : SQLException");
			System.err.println("--> Classe      : JArbreBD");
			System.err.println("--> M�thode     : creerNoeudsEtoiles");
			System.err.print  ("--> Message JVM : "); System.err.println(ex.getMessage());
			System.err.print  ("--> Code    JVM : "); System.err.println(ex.getErrorCode());
			System.exit(-1);
		}
	}

}
