/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdd;

/**
 *
 * @author loicleger
 */
import java.sql.*;
import java.util.*;

public class DataBaseHandler {
	
	private static Connection laConnection;
	private static Statement laTransmission;
	
	public static void initConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			laConnection = DriverManager.getConnection("jdbc:mysql://localhost/mydb","loicleger","jauzion");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ResultSet select (String what,String from,String where) {
		try {
			laTransmission = laConnection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		ResultSet result = null;
		
		try{
			
			if(what == "")
				what = "*"; 
			
			if(from == "")
				return null;
			
			String query = "SELECT " + what + " FROM " + from;
			if(where != null)
				query += " WHERE " + where;
			
			result = laTransmission.executeQuery(query);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static int update(String from,String where,Map<String,String> values) {

		try {
			laTransmission = laConnection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		int result = 0;
		
		try{
			
			if(from == "")
				return 0;
			
			if(where == "")
				return 0;
			
			String set = "";
			Iterator<String> it = values.keySet().iterator();
			String s = "";
			for( ; it.hasNext() ;  ) {
				s = it.next();
				set += s + " = '" + values.get(s) + "'";
				if(it.hasNext())
					set += ", ";
			}
			if(set == "")
				return 0;
			
			String query = "UPDATE " + from + " SET " +  set + " WHERE " + where;
			result = laTransmission.executeUpdate(query);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
			
	}
	
	public static int insert(String from,Map<String,String> values) {
		try {
			laTransmission = laConnection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		int result = 0;
		
		try{
			
			if(from == "")
				return 0;
	
			Iterator<String> it = values.keySet().iterator();
			String into = "(";
			String val = "(";
			String s = "";
			for( ; it.hasNext() ;  ) {
				s = it.next();
				into += s;
				val += "'"+values.get(s)+"'";
				if(it.hasNext())
				{
					into += ",";
					val += ",";
				}					
			}
			into += ")";
			val += ")";
			if(into == "()")
				return 0;
			
			String query = "INSERT INTO " + from + into + " VALUES " +  val;

			result = laTransmission.executeUpdate(query);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
			
	}

	public static boolean delete(String from,String where) {
		boolean result = false;
		try {
			laTransmission = laConnection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try{
			
			if(from == "" || from == null)
				return false;
			
			if(where == "" || where == null)
				return false;
			
			String query = "DELETE FROM " + from + " WHERE " + where;
			result = laTransmission.execute(query);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}

