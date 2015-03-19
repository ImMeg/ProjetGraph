package jtree;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class Configurations 
{
	// Ent�tes des fen�tres
	public static final String _TTL = "M.A.D.";
	public static final String _LNG_TTL = "Multidimensional Analysis of Data";
	
	// Aspects des fen�tres
	public static final String[] _TXT_LOOK = {"Windows", "Metal", "Motif", "GTK", "Liquid"};
	public static final String[] _LOOK = {"com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
	                                "javax.swing.plaf.metal.MetalLookAndFeel",
	                                "com.sun.java.swing.plaf.motif.MotifLookAndFeel",
	                                "com.sun.java.swing.plaf.gtk.GTKLookAndFeel",
	                                "com.birosoft.liquid.LiquidLookAndFeel"
	                                };
	public static int _NO_LOOK = 0;

	// Param�trages d'acc�s � la base Oracle
	public static String _HST = "ntelline.cict.fr";
	public static String _PRT = "1522";
	public static String _SID = "etupre";
	public static String _LOG = "teste_mad";
	public static String _PWD = "dsrt";
	public static String _URL = "jdbc:oracle:thin:@"+_HST+":"+_PRT+":"+_SID;
	public static Connection _CNX = null;
}
