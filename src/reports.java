import java.sql.*;
import java.util.*;

public class reports {
	private final String dbClassName = "com.mysql.cj.jdbc.Driver";
	private final String CONNECTION = "jdbc:mysql://127.0.0.1/mybnb";
	final String USER = "root";
	final String PASS = "";
	
	private List<String> get_cities() throws ClassNotFoundException, SQLException {
		String query = "SELECT DISTINCT City from Listing;";
		List<String> cities = new ArrayList<String>();
		ResultSet rs = this.execute_query(query);
		while(rs.next()){
			//Retrieve by column name
			cities.add(rs.getString("city"));
		}
		return cities;
	}

	private ResultSet execute_query(String query) throws ClassNotFoundException {
		Class.forName(this.dbClassName);
		//Database credentials
		final String USER = "root";
		final String PASS = "";
		ResultSet rs = null;
		try {
			//Establish connection
			Connection conn = DriverManager.getConnection(this.CONNECTION,USER,PASS);
			System.out.println("Successfully connected to 'mybnb' as user: root");
			Statement stmt = conn.createStatement();
			System.out.println(query);
			rs = stmt.executeQuery(query);
			conn.close();
		} catch (SQLException e) {
			System.err.println(e);
		}
		return rs;
	}
	
	public static void main(String[] args) {
		
	}
}
