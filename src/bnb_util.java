import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
			// System.out.println(query);
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			System.err.println(e);
		}
		return rs;
	}
}

