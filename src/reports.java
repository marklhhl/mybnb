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
			country.add(rs.getString("country"));
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
	
	public static Map<String, List<Integer>> rankRenter(boolean by_city, Date date_start, Date date_end) throws ClassNotFoundException, SQLException {
		String sDate_start = bnb_util.date_to_string(date_start);
		String sDate_end = bnb_util.date_to_string(date_end);
		Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
		if (by_city) {
			List<String> cities = reports.get_cities("all");
			for (String city : cities) {
				String query = "select Uid from (select Uid, count(*) total from (Select * from (user u inner join history h on u.Uid = h.renter_id inner join (select Lid, city as lcity from listing) s on h.list_id = s.Lid)"
								+ " where (transaction_date between '" + sDate_start + "' AND '" + sDate_end + "') AND lcity = '" + city + "') as renter_hist Group by Uid) as renter_book_count where total > 1;";
				ResultSet rs = bnb_util.execute_query(query);
				List<Integer> renters = new ArrayList<Integer>();
				while(rs.next()) {
					renters.add(rs.getInt("Uid"));
				}
				result.put(city + " between " + sDate_start + " AND " + sDate_end + " ", renters);
			}
		} else {
			String query = "select Uid from (select Uid, count(*) total from (Select * from (user u inner join history h on u.Uid = h.renter_id) where transaction_date "
					+ "between '"+ sDate_start + "' AND '" + sDate_end + "') as renter_hist Group by Uid) as renter_book_count where total > 1;";
			ResultSet rs = bnb_util.execute_query(query);
			List<Integer> renters = new ArrayList<Integer>();
			while(rs.next()) {
				renters.add(rs.getInt("Uid"));
			}
			result.put("between " + sDate_start + " AND " + sDate_end + " ", renters);
		}
	
		return result;
	}
	
	public static Map<String, List<Integer>> identifyCommercialHost() throws ClassNotFoundException, SQLException {
		Map<String, List<List<Integer>>> host_listing_count = rankHost(true);
		Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
		for (String key : host_listing_count.keySet()) {
			String[] parts = key.split(" ");
			String query = "Select sum(listingCount) totalList from (select Uid, count(*) listingCount From (Select * from (user u inner join (select Lid, host_id, country as lcountry, city as lcity from listing) l on u.Uid = l.host_id) where "
					+ "lcountry = '" + parts[0] + "' AND lcity = '" + parts[1] + "') as user_listing GROUP BY Uid Desc) as user_listing_total";
			ResultSet rs = bnb_util.execute_query(query);
			int total_listings = 0;
			while(rs.next()) {
				total_listings = rs.getInt("totalList");
			}
			List<Integer> flagged_hosts = new ArrayList<Integer>();
		    for (List<Integer> host: host_listing_count.get(key)) {
		    	if (host.get(1) > Math.floor(total_listings*0.1)) {
		    		flagged_hosts.add(host.get(0));
		    	}
		    }
		    result.put(key, flagged_hosts);
		}
		return result;
	}
	
	public static Map<String, List<List<Integer>>> rankHost(boolean by_city) throws ClassNotFoundException, SQLException {
		Map<String, List<List<Integer>>> result = new HashMap<String, List<List<Integer>>>();
		List<String> countries = reports.get_countries();
		for (String country : countries) {
			if (by_city) {
				List<String> cities = reports.get_cities(country);
				for (String city : cities) {
					String query = "Select Uid, count(*) totalCount From (Select * from (user u inner join (select Lid, host_id, country as lcountry, city as lcity from listing) l on u.Uid = l.host_id) where "
							+ "lcountry = '" + country + "' AND lcity = '" + city + "') as user_listing group by Uid Desc;";
					ResultSet rs = bnb_util.execute_query(query);
					List<List<Integer>> rank = new ArrayList<List<Integer>>();
					while(rs.next()) {
						List<Integer> pair = new ArrayList<Integer>();
						pair.add(rs.getInt("Uid"));
						pair.add(rs.getInt("totalCount"));
						rank.add(pair);
					}
					result.put(country + " " + city, rank);
				}
			} else {
				String query = "Select Uid, count(*) totalCount From (Select * from (user u inner join (select Lid, host_id, country as lcountry, city as lcity from listing) l on u.Uid = l.host_id) where "
						+ "lcountry = '" + country + "') as user_listing group by Uid Desc;";
				ResultSet rs = bnb_util.execute_query(query);
				List<List<Integer>> rank = new ArrayList<List<Integer>>();
				while(rs.next()) {
					List<Integer> pair = new ArrayList<Integer>();
					pair.add(rs.getInt("Uid"));
					pair.add(rs.getInt("totalCount"));
					rank.add(pair);
				}
				result.put(country, rank);
			}
		}
	
		return result;
	}
	
	public static Map<String, String> reportListing(boolean by_city, boolean by_postalCity) throws ClassNotFoundException, SQLException {
		Map<String, String> result = new HashMap<String, String>();
		List<String> countries = reports.get_countries();
		for (String country : countries) {
			if (by_city || by_postalCity) {
				List<String> cities = reports.get_cities(country);
				for (String city : cities) {
					if (by_postalCity) {
						List<String> postals = reports.get_postal(city);
						for (String postal : postals) {
							String query = "SELECT count(*) FROM listing WHERE (country = '" + country + "') AND (city = '" + city + "') AND (postal_code = '" + postal + "');";
							ResultSet rs = bnb_util.execute_query(query);
							while(rs.next()) {
								result.put(country + " " + city + " " + postal, Integer.toString(rs.getInt("count(*)")));
							}
						}
					} else {
						String query = "SELECT count(*) FROM listing WHERE (country = '" + country + "') AND (city = '" + city + "');";
						ResultSet rs = bnb_util.execute_query(query);
						while(rs.next()) {
							result.put(country + " " + city, Integer.toString(rs.getInt("count(*)")));
						}
					}
				}
			} else {
				String query = "SELECT count(*) FROM listing WHERE (country = '" + country + "');";
				ResultSet rs = bnb_util.execute_query(query);
				while(rs.next()) {
					result.put(country, Integer.toString(rs.getInt("count(*)")));
				}
			}
		}

		return result;
	}
	
	public static Map<String, String> reportBooking(boolean by_postal, Date date_start, Date date_end) throws ClassNotFoundException, SQLException {
		String sDate_start = bnb_util.date_to_string(date_start);
		String sDate_end = bnb_util.date_to_string(date_end);

		List<String> cities = reports.get_cities("all");
		Map<String, String> result = new HashMap<String, String>();
		for (String city : cities) {
			if (by_postal) {
				List<String> postals = reports.get_postal(city);
				for (String postal : postals) {
					String query = "SELECT count(*) FROM (history h join listing l on h.list_id = l.Lid) WHERE (city = '" + city + "') " + "AND (transaction_date >= '" + sDate_start + "') AND (transaction_date <= '" + sDate_end + "')";
					query = query + " AND (postal_code = '" + postal + "')" + ";";
					ResultSet rs = bnb_util.execute_query(query);
					while(rs.next()) {
						result.put(city + " " + postal + " between " + date_start + " AND " + date_end, Integer.toString(rs.getInt("count(*)"))); 
					}
				}
			} else {
				String query = "SELECT count(*) FROM (history h join listing l on h.list_id = l.Lid) WHERE (city = '" + city + "') " + "AND (transaction_date >= '" + sDate_start + "') AND (transaction_date <= '" + sDate_end + "');";
				ResultSet rs = bnb_util.execute_query(query);
				while(rs.next()) {
					result.put(city + " between " + date_start + " AND " + date_end, Integer.toString(rs.getInt("count(*)"))); 
				}
			}
		}
		return result;
	}
}
