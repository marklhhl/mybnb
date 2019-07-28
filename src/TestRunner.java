import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TestRunner {
  private static final String dbClassName = "com.mysql.cj.jdbc.Driver";
  private static final String CONNECTION = "jdbc:mysql://127.0.0.1/mybnb";
  public static void main (String[] args) {
      Scanner scan = new Scanner(System.in);
//    Date obj1 = new Date(2018, 2, 9);
//    Date from = new Date(2018, 2, 5);
//    Date until = new Date(2018, 2, 12);
//    long diffInMillies = until.getTime() - from.getTime();
//    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//    Calendar cal = Calendar.getInstance();
//    System.out.println(dateFormat.format(Calendar.getInstance().getTime())); //2016/11/16 12:08:43
  //Register JDBC driver
    try {
      Class.forName(dbClassName);
    } catch (ClassNotFoundException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    //Database credentials
    final String USER = "root";
    final String PASS = "";
    System.out.println("Connecting to database...");
    

    try {
      //Establish connection
      Connection conn = DriverManager.getConnection(CONNECTION,USER,PASS);
      System.out.println("Successfully connected to 'mybnb' as user: root");
      Statement stmt = conn.createStatement();
//      String sql = "INSERT INTO listing (home_type, longitude, latitude, city, addr, postal_code,"
//          + " country, wifi, beds, bathrooms, kitchens, parking, other_accom, addtional_comment, host_id) VALUES ";
//      sql += Operations.createListing(scan, "2");
//      System.out.println(sql);
//      stmt.executeUpdate(sql);
      Operations.changePrice("3", scan, stmt);
      ResultSet rs = stmt.executeQuery("SELECT * FROM calendar");
      
      //STEP 5: Extract data from result set
      while(rs.next()){
        //Retrieve by column name
        String addr  = rs.getString("avaliable_from");
        String lid = rs.getString("avaliable_till");
        int a = rs.getInt("price");
      
        //Display values
        System.out.print("lid: "+a);
        System.out.print(" from: " + addr);
        System.out.print(", til: " + lid + "\n");
      }

      System.out.println("Closing connection...");
      conn.close();
      System.out.println("Success!");
    } catch (SQLException e) {
      System.err.println(e);
    }
  }

}
