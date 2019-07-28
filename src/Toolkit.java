import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Toolkit {
	
	// want to suggest based on listing postal code, and number of beds, and date range
	public static double suggest_price(String postal, int people, Date date_start, Date date_end) throws ClassNotFoundException, SQLException {
		List<String> adj_postal = bnb_util.get_adj(postal);
		String sdate_start = bnb_util.date_to_string(date_start);
		String sdate_end = bnb_util.date_to_string(date_end);
		String query = "select avg(price) from (select * from (listing l inner join calendar c on c.listing_id = l.Lid)) as list_cal where beds between "+ Math.ceil(people/2) + " AND " + people + " AND avaliable_from >= '" + sdate_start + "' AND avaliable_till <= '" + sdate_end + "' AND ";
		query = query + "(postal_code = '" + adj_postal.get(0) + "' OR postal_code = '" + adj_postal.get(1) + "' ";
		if (query.length() == 3) {
			query = query + "OR postal_code = '" + adj_postal.get(2) + "'";
		}
		query = query + ");";

		ResultSet rs = bnb_util.execute_query(query);
		return rs.getDouble("avg(price)");
	}

	// suggest basic amenities based on desired price
	public static Map<String, Integer> suggest_amenity(int price_start, int price_end, Date date_start, Date date_end) throws ClassNotFoundException, SQLException {
		String sdate_start = bnb_util.date_to_string(date_start);
		String sdate_end = bnb_util.date_to_string(date_end);
		String query = "select avg(beds), avg(bathrooms), avg(kitchens), avg(parking) from (select * from (listing l inner join calendar c on c.listing_id = l.Lid)) as list_cal where " + "avaliable_from >= '" + sdate_start + "' AND avaliable_till <= '" + sdate_end + "' AND " + "price between " + price_start + " AND " + price_end + ";";
		ResultSet rs = bnb_util.execute_query(query);
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("beds", rs.getInt("avg(beds)"));
		result.put("bathrooms", rs.getInt("avg(bathrooms)"));
		result.put("kitchens", rs.getInt("avg(kitchens)"));
		result.put("parking", rs.getInt("avg(parking)"));
		return result;
	}
}
