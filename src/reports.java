import java.sql.*;
import java.util.*;
import java.util.Date;

public class reports {

	private static List<String> get_countries() throws ClassNotFoundException, SQLException {
		String query = "SELECT DISTINCT country FROM listing;";
		List<String> country = new ArrayList<String>();
		ResultSet rs = bnb_util.execute_query(query);
		while(rs.next()){
			//Retrieve by column name
			country.add(rs.getString("city"));
		}
		return country;
	}
	
	private static List<String> get_cities(String country) throws ClassNotFoundException, SQLException {
		String query = "SELECT DISTINCT city FROM listing";
		if (country != "all") {
			query = "SELECT DISTINCT city FROM listing WHERE country = '" + country + "'";
		}
		query = query + ";";
		List<String> cities = new ArrayList<String>();
		ResultSet rs = bnb_util.execute_query(query);
		while(rs.next()){
			//Retrieve by column name
			cities.add(rs.getString("city"));
		}
		return cities;
	}
	
	private static List<String> get_postal(String city) throws ClassNotFoundException, SQLException {
		String query = "SELECT DISTINCT postal_code FROM listing WHERE city = '" + city + "';";
		List<String> postal_codes = new ArrayList<String>();
		ResultSet rs = bnb_util.execute_query(query);
		while(rs.next()){
			//Retrieve by column name
			postal_codes.add(rs.getString("postal_code"));
		}
		return postal_codes;
	}
	
	public static Map<String, String> reportBooking(boolean country, boolean city, boolean postal) throws ClassNotFoundException, SQLException {
		return null;
	}
	
	public static Map<String, String> reportBooking(boolean by_postal, Date date_start, Date date_end) throws ClassNotFoundException, SQLException {
		String sDate_start = bnb_util.date_to_string(date_start);
		String sDate_end = bnb_util.date_to_string(date_end);

		List<String> cities = reports.get_cities("all");
		Map<String, String> result = new HashMap<String, String>();
		for (String city : cities) {
			String query = "SELECT count(*) FROM (history h join listing l on h.list_id = l.Lid) WHERE (city = '" + city + "') " + "AND (transaction_date >= '" + sDate_start + "') AND (transaction_date <= '" + sDate_end + "')";

			if (by_postal) {
				List<String> postals = reports.get_postal(city);
				for (String postal : postals) {
					query = "SELECT count(*) FROM (history h join listing l on h.list_id = l.Lid) WHERE (city = '" + city + "') " + "AND (transaction_date >= '" + sDate_start + "') AND (transaction_date <= '" + sDate_end + "')";
					query = query + " AND (postal_code = '" + postal + "')" + ";";
					ResultSet rs = bnb_util.execute_query(query);
					while(rs.next()) { result.put(city + " " + postal, Integer.toString(rs.getInt("count(*)"))); }
				}
			} else {
				query = query + ";";
				ResultSet rs = bnb_util.execute_query(query);
				while(rs.next()) { result.put(city, Integer.toString(rs.getInt("count(*)"))); }
			}
		}
		return result;
	}
}
