import java.sql.*;

public class bnb_Util {
	public static final String dbClassName = "com.mysql.cj.jdbc.Driver";
	public static final String CONNECTION = "jdbc:mysql://127.0.0.1/mybnb";
	public static final String USER = "root";
	public static final String PASS = "";
	
	public static ResultSet execute_query(String query) throws ClassNotFoundException {
		Class.forName(bnb_Util.dbClassName);
		//Database credentials
		final String USER = "root";
		final String PASS = "";
		ResultSet rs = null;
		try {
			//Establish connection
			Connection conn = DriverManager.getConnection(bnb_Util.CONNECTION,USER,PASS);
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
}
