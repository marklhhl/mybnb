import java.util.*;

public class Queries {
	private double longitude;
	private double latitude;
	private String postal_code;
	private String address;
	private int radius = 5;
	private double price_start = -1;
	private double price_end = -1;
	private String date_start;
	private String date_end;
	private int beds = -1;
	private int parking = -1;
	private int baths = -1;
	private int kitchens = -1;
	private boolean wifi = false;
	private boolean sort_by_price = false;
	private boolean order = true;
	
	public Queries(String postal_code) {
		this.postal_code = postal_code;
	}
	
	public Queries(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public Queries(String address, boolean verify) {
		this.address = address;
	}
		
	public Queries withRadius(int radius) {
		this.radius = radius;
		return this;
	}
	
	public Queries withSortPrice(boolean sortPrice, boolean asc_desc) {
		this.sort_by_price = sortPrice;
		return this;
	}

	public Queries withPriceRange(double price_start, double price_end) {
		this.price_start = price_start;
		this.price_end = price_end;
		return this;
	}
	
	public Queries withDateRange(String date_start, String date_end) {
		this.date_start = date_start;
		this.date_end = date_end;
		return this;
	}
	
	public Queries withBeds(int beds) {
		this.beds = beds;
		return this;
	}
	
	public Queries withParking(int parking) {
		this.parking = parking;
		return this;
	}
	
	public Queries withBaths(int baths) {
		this.baths = baths;
		return this;
	}
	
	public Queries withKitchens(int kitchens) {
		this.kitchens = kitchens;
		return this;
	}
	
	public Queries withWifi(boolean wifi) {
		this.wifi = wifi;
		return this;
	}
		
	// generate haversine constraint(to determine distance between coordinates)
	private String prepare_haversine_constraint(double logi,double lati) {
		String constraint = "6371*2*asin(sqrt(power(sin(radians(" + Double.toString(lati) + "-latitude)/2), 2) + cos(radians(latitude))*cos(radians(" + Double.toString(lati)
							+ "))*power(sin(radians(" + Double.toString(logi) + "-longitude)/2), 2)))";
		return constraint;
	}
	
	// get adjacent postal codes
	private List<String> get_adj(String postal_code) {
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

	// prepare query string
	public String prepare_query() {
		// join list and calendar
		String query = "SELECT * FROM listing NATURAL JOIN (Select listing_id as Lid, avaliable_from, avaliable_till, Caid, price from calendar) c_listing";
		String hav_constraint = null;
		List<String> adj_postal;
		
		// search location by ...
		if ((this.address != null)) {
			query = " where addr = " + this.address;
			return query + ";";
		}
		else if ((this.longitude != 0) && (this.latitude != 0)) {
			hav_constraint = this.prepare_haversine_constraint(this.longitude, this.latitude);
			query = query + " where (" + hav_constraint + " < " + this.radius + ")";
		}
		else if (this.postal_code != null) {
			adj_postal = this.get_adj(this.postal_code);
			query = query + " where (left(postal_code, 3) = '" + adj_postal.get(0) + "' OR left(postal_code, 3) = '" + adj_postal.get(1) + "'";
			if (adj_postal.size() == 3) {
				query = query + " OR left(postal_code, 3) = '" + adj_postal.get(2) + "')";
			}
		}
		
		// with constraints... 
		if (this.beds != -1) {
			query = query + " AND " + "(beds >=  " + Integer.toString(this.beds) + ")";
		}
		
		if (this.parking != -1) {
			query = query + " AND " + "(parking >=  " + Integer.toString(this.parking) + ")";
		}
		
		if (this.kitchens != -1) {
			query = query + " AND " + "(kitchens >=  " + Integer.toString(this.kitchens) + ")";
		}
		
		if (this.baths != -1) {
			query = query + " AND " + "(bathrooms >=  " + Integer.toString(this.baths) + ")";
		}
		
		if (this.wifi) {
			query = query + " AND " + "(wifi = 'YES')";
		}
		
		if (this.date_start != null) {
			query = query + " AND " + "(avaliable_from >=  " + this.date_start + " AND avaliable_till <=" + this.date_end + ")";
		}
		
		if (this.price_start != -1) {
			query = query + " AND " + "(price >=  " + this.price_start + " AND price <=" + this.price_end + ")";
		}
		
		// sort by...
		if ((this.longitude != 0) && (this.latitude != 0)) {
			query = query + " order by " + hav_constraint + " ASC";
		}
		if (this.sort_by_price) {
			query = query + ", " + "price ";
			if (this.order) {
				 query = query + "ASC";
			} else {
				 query = query + "DESC";
			}
			
		}
		
		// end statement
		query = query + ";";
		return query;
	}

}