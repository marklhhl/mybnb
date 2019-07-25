import java.sql.*;
import java.util.*;

public class reports {
	
	public static List<String> get_cities() throws ClassNotFoundException, SQLException {
		String query = "SELECT DISTINCT city from Listing;";
		List<String> cities = new ArrayList<String>();
		ResultSet rs = bnb_util.execute_query(query);
		while(rs.next()){
			//Retrieve by column name
			cities.add(rs.getString("city"));
		}
		return cities;
	}
	
}
