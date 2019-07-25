import java.sql.*;

public class bnb_util {
	public static final String dbClassName = "com.mysql.cj.jdbc.Driver";
	public static final String CONNECTION = "jdbc:mysql://127.0.0.1/mybnb";
	public static final String USER = "root";
	public static final String PASS = "";
	public static Connection connection = null;
	
	public static Connection getConnection() throws ClassNotFoundException {
		Class.forName(bnb_util.dbClassName);
		//Database credentials
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
}

