import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Toolkit {
	
	// want to suggest based on listing postal code, and number of beds, and date range
	public static double suggest_price(String postal, int people, String date_start, String date_end, Statement stmt) throws ClassNotFoundException, SQLException {
		List<String> adj_postal = bnb_util.get_adj(postal);
		String sdate_start = date_start;
		String sdate_end = date_end;
		String query = "select avg(price) from (select * from (listing l inner join calendar c on c.listing_id = l.Lid)) as list_cal where (beds between "+ Math.ceil(people/2) + " AND " + people + ") AND avaliable_from >= '" + sdate_start + "' AND avaliable_till <= '" + sdate_end + "' AND ";
		query = query + "(LEFT(postal_code,3) = '" + adj_postal.get(0) + "' OR LEFT(postal_code,3) = '" + adj_postal.get(1) + "' ";
		System.out.println(adj_postal);
		if (adj_postal.size() == 3) {
			query = query + "OR LEFT(postal_code,3) = '" + adj_postal.get(2) + "'";
		}
		query = query + ");";

		ResultSet rs = stmt.executeQuery(query);
		double price = 0;
		while (rs.next()) {
		  price = rs.getDouble("avg(price)");
		};
		return price;
	}

	// suggest basic amenities based on desired price
	public static Map<String, Integer> suggest_amenity(int price_start, int price_end, String date_start, String date_end, Statement stmt) throws ClassNotFoundException, SQLException {
		String sdate_start = date_start;
		String sdate_end = date_end;
		String query = "select avg(beds), avg(bathrooms), avg(kitchens), avg(parking) from (select * from (listing l inner join calendar c on c.listing_id = l.Lid)) as list_cal where " + "avaliable_from >= '" + sdate_start + "' AND avaliable_till <= '" + sdate_end + "' AND " + "price between " + price_start + " AND " + price_end + ";";
		ResultSet rs = stmt.executeQuery(query);
		Map<String, Integer> result = new HashMap<String, Integer>();
		while(rs.next()) {
		  result.put("beds", rs.getInt("avg(beds)"));
	    result.put("bathrooms", rs.getInt("avg(bathrooms)"));
	    result.put("kitchens", rs.getInt("avg(kitchens)"));
	    result.put("parking", rs.getInt("avg(parking)"));
		}
		return result;
	}
}
