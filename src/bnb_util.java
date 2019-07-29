import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class bnb_util {
	//Database credentials
	public static final String dbClassName = "com.mysql.cj.jdbc.Driver";
	public static final String CONNECTION = "jdbc:mysql://127.0.0.1/mybnb";
	private static final String USER = "root";
	private static final String PASS = "";
	public static Connection connection = null;
	
	public static class tuple {
		  public static String x; 
		  public static int y; 
		  
		  public tuple(String x, int y) { 
		    tuple.x = x;
		    tuple.y = y; 
		  } 
	}
	
	public static String date_to_string(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		String todayAsString = df.format(date);
		return todayAsString;
	}
	
	public static Connection getConnection() throws ClassNotFoundException {
		Class.forName(bnb_util.dbClassName);
		try {
			//Establish connection
			connection = DriverManager.getConnection(bnb_util.CONNECTION,USER,PASS);
			System.out.println("Successfully connected to 'mybnb' as user: root");
			return connection;
		} catch (SQLException e) {
			System.err.println(e);
		}
		return null;
	}
	
	public static boolean closeConnection() throws ClassNotFoundException {
		try {
			connection.close();
			return true;
		} catch (SQLException e) {
			System.err.println(e);
		}
		return false;
	}
	
	public static ResultSet execute_query(String query) throws ClassNotFoundException {
		Class.forName(bnb_util.dbClassName);
		//Database credentials
		ResultSet rs = null;
		try {
			//Establish connection
			Statement stmt = connection.createStatement();
			 System.out.println(query);
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			System.err.println(e);
		}
		return rs;
	}
	
	// get adjacent postal codes
	public static List<String> get_adj(String postal_code) {
		String regionCode = postal_code.substring(0,3);
		int adj_num = Integer.parseInt(postal_code.substring(1,2));
		List<String> result = new ArrayList<String>();
		result.add(regionCode);
		if (adj_num == 9) {
			adj_num = 8;
			result.add(regionCode.substring(0,1) + Integer.toString(adj_num) + regionCode.substring(3,3));
		} else if (adj_num == 1) {
			adj_num = 2;
			result.add(regionCode.substring(0,1) + Integer.toString(adj_num) + regionCode.substring(3,3));
		} else {
			result.add(regionCode.substring(0,1) + Integer.toString(adj_num + 1) + regionCode.substring(2));
			result.add(regionCode.substring(0,1) + Integer.toString(adj_num - 1) + regionCode.substring(2));
		}
		return result;
	}
	
//execute query with connection already being set
 public static ResultSet execute_query2(String query, Statement stmt) throws ClassNotFoundException {
   try {
     return stmt.executeQuery(query);
   } catch (SQLException e) {
     System.out.println("Error with executing the following statement: ");
     System.out.println(query);
     e.printStackTrace();
     return null;
   }
 }
 
	public static boolean executeUpdate(String query, Statement stmt) {
	  try {
      stmt.executeUpdate(query);
      return true;
    } catch (SQLException e) {
      System.out.println("Error with executing the following statement: ");
      System.out.println(query);
      e.printStackTrace();
      return false;
    }
	}
}
