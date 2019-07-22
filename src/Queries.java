import java.sql.*;


public class Queries {
	private static final String dbClassName = "com.mysql.cj.jdbc.Driver";
	private static final String CONNECTION = "jdbc:mysql://127.0.0.1/mybnb";
	
	public static class Builder {
		private double longitude;
		private double latitude;
		private int radius = 5;
		private double[] price_range;
		private String[] date_range;
		private int beds;
		private int parking;
		private int baths;
		private int kitchens;
		private boolean sort_by_price;
		private boolean sort_by_range;
		
		public Builder withCoord(double longitude, double latitude) {
			this.longitude = longitude;
			this.latitude = longitude;
			return this;
		}
		
		public Builder withRadius(int radius) {
			this.radius = radius;
			return this;
		}
		
		public Builder withPriceRange(double[] price_range) {
			this.price_range = price_range;
			return this;
		}
		
		public Builder withDateRange(String[] date_range) {
			this.date_range = date_range;
			return this;
		}
		
		public Builder withBeds(int beds) {
			this.beds = beds;
			return this;
		}
		
		public Builder withParking(int parking) {
			this.parking = parking;
			return this;
		}
		
		public Builder withBaths(int baths) {
			this.baths = baths;
			return this;
		}
		
		public Builder withKitchens(int kitchens) {
			this.kitchens = kitchens;
			return this;
		}
		
		public Builder withSortPrice(boolean sortPrice) {
			this.sort_by_price = sortPrice;
			return this;
		}
		
		public Builder withSortRange(boolean sortRange) {
			this.sort_by_range = sortRange;
			return this;
		}
	}
	
	public static String prepare_query() {
		String sql = "SELECT * FROM listing;";
		return sql;
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		//Register JDBC driver
				Class.forName(dbClassName);
				//Database credentials
				final String USER = "root";
				final String PASS = "";
				System.out.println("Connecting to database...");


				try {
					//Establish connection
					Connection conn = DriverManager.getConnection(CONNECTION,USER,PASS);
					System.out.println("Successfully connected to 'mybnb' as user: root");
					Statement stmt = conn.createStatement();
					String sql = Queries.prepare_query();
					ResultSet rs = stmt.executeQuery(sql);
					

					//STEP 5: Extract data from result set
					while(rs.next()){
						//Retrieve by column name
						int Lid  = rs.getInt("Lid");
						String addr = rs.getString("addr");
					
						//Display values
						System.out.print("ID: " + Lid);
						System.out.print(", addr: " + addr + "\n");
					}

					System.out.println("Closing connection...");
					conn.close();
					System.out.println("Success!");
				} catch (SQLException e) {
					System.err.println(e);
				}
	}
	
}
