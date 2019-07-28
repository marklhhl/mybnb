import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Operations {
  
  public static final String COMMASPACE = ", ";
  
  public static int getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
    long diffInMillies = date2.getTime() - date1.getTime();
    return (int) timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
  }
  
  public static List<HashMap> getConsecutiveDateEntries(String list_id, String startDate, String endDate, Statement stmt){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date startD = null;
    Date endD = null;
    try {
      startD = new Date(sdf.parse(startDate).getTime());
      endD = new Date(sdf.parse(endDate).getTime());
    } catch (ParseException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    List<HashMap> result = new ArrayList<HashMap>();
    ResultSet rs = null;
    String sql = "SELECT * FROM calendar WHERE listing_id="+list_id+" ORDER BY avaliable_from ASC;";
    boolean foundOne = false;
    Date previous_avaliable_til = null;
    try {
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        do {
          // put resultset data into a hashmap
          HashMap row = new HashMap();
          row.put("Caid", rs.getInt("Caid"));
          row.put("avaliable_from", rs.getDate("avaliable_from"));
          row.put("avaliable_till", rs.getDate("avaliable_till"));
          row.put("price", rs.getDouble("price"));
          row.put("listing_id", rs.getInt("listing_id"));
          if (!foundOne) {
            if (startD.compareTo(rs.getDate("avaliable_from")) >= 0 && startD.compareTo(rs.getDate("avaliable_till")) <= 0) {
              if (endD.compareTo(rs.getDate("avaliable_till")) <= 0) {
                result.add(row);
                return result;
              } else {
                foundOne = true;
              }
              previous_avaliable_til = rs.getDate("avaliable_till");
            }
          } else {
            Calendar previous = Calendar.getInstance();
            System.out.println(previous_avaliable_til);
            previous.setTime(previous_avaliable_til);
            previous.add(Calendar.DATE, 1);
            Calendar cur = Calendar.getInstance();
            cur.setTime(rs.getDate("avaliable_from"));
            if (previous.compareTo(cur) == 0) {
              System.out.println(2323);
              result.add(row);
              if (endD.compareTo(rs.getDate("avaliable_till")) <= 0) {
//                System.out.println(result.size());
                return result;
              }
              previous_avaliable_til = rs.getDate("avaliable_till");
            }else {
//              System.out.println(sdf.format(previous.getInstance().getTime()));
//              System.out.println(rs.getDate("avaliable_till"));
              System.out.println("Date range entered is no good");
              result.clear();
              return result;
            }
          }
        } while (rs.next());
      } else {
        System.out.println("List identification not found");
        return result;
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // clear this since end date exceeds total amount of days avaialble
    result.clear();
    return result;
  }
  
  // method will return a value that will be used as part a create user query
  public static String createUser(Scanner sc) {
    String query = "(";
    System.out.println("Enter your address: ");
    String address = sc.nextLine();
    query += ("'"+address+"', ");
    System.out.println("Enter your country: ");
    String country = sc.nextLine();
    query += ("'"+country+"', ");
    System.out.println("Enter your city: ");
    String city = sc.nextLine();
    query += ("'"+city+"', ");
    
    // valid postal_code input check
    String postal_code;
    do {
      System.out.println("Enter your postal code: ");
      postal_code = sc.nextLine();
      Pattern pattern = Pattern.compile("^[A-Z][0-9][A-Z][0-9][A-Z][0-9]$");
      if (!pattern.matcher(postal_code).matches()) {
        System.out.println("Invalid input, make sure there are no spaces and letters are capital");
      } else {
        query += ("'"+postal_code+"', ");
        break;
      }
    } while (true);
    
    // valid birthday input check
    String birthday;
    do {
      System.out.println("Enter your birthday in year, month, day format(xxxx-xx-xx): ");
      birthday = sc.nextLine();
      Pattern pattern = Pattern.compile("^[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]$");
      if (!pattern.matcher(birthday).matches()) {
        System.out.println("Invalid date input");
      } else {
        query += ("'"+birthday+"', ");
        break;
      }
    } while(true);
    
    // valid sin input check
    String sin;
    do {
      System.out.println("Enter your social insurance number: ");
      sin = sc.nextLine();
      Pattern pattern = Pattern.compile("^[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]$");
      if (!pattern.matcher(sin).matches()) {
        System.out.println("Invalid sin input; make sure its a 9 digit input");
      } else {
        query += sin+", ";
        break;
      }
    } while (true);
    
    System.out.println("Enter your first name: ");
    String first_name = sc.nextLine();
    query += ("'"+first_name+"', ");
    System.out.println("Enter your last name: ");
    String last_name = sc.nextLine();
    query += ("'"+last_name+"', ");
    System.out.println("Enter your middle name (press enter to skip): ");
    String middle_name = sc.nextLine();
    if (middle_name.length()==0) {
      query += ("null, ");
    } else {
      query += ("'"+middle_name+"', ");
    }
    System.out.println("Enter your occupation: ");
    String occupation = sc.nextLine();
    query += ("'"+occupation+"', ");
    
    // valid credit card number check
    String credit_card;
    do {
      System.out.println("Enter your credit card number: ");
      credit_card = sc.nextLine();
      Pattern pattern = Pattern.compile("^[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]$");
      if (!pattern.matcher(credit_card).matches()) {
        System.out.println("Invalid credit card value; make sure its a 16 digit input");
      } else {
        query += credit_card+", ";
        break;
      }
    } while (true);
    
    // valid direct deposit number check
    String direct_deposit;
    do {
      System.out.println("Enter your direct deposit number: ");
      direct_deposit = sc.nextLine();
      Pattern pattern = Pattern.compile("^[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]$");
      if (!pattern.matcher(direct_deposit).matches()) {
        System.out.println("Invalid direct deposit value; make sure its a 15 digit input");
      } else {
        query += direct_deposit+", ";
        break;
      }
    } while (true);
    
    // valid email check
    String email;
    do {
      System.out.println("Enter your email: ");
      email = sc.nextLine();
      Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
      if (!pattern.matcher(email).matches()) {
        System.out.println("Incorrect email input");
      } else {
        query += "'"+email+"', ";
        break;
      }
    } while (true);
    
    System.out.println("Enter your password: ");
    String password = sc.nextLine();
    query += ("'"+password+"');");
    
    return (query);
  }
  
  
  // method will return a value that will be used as part of a create list query
  public static void createListing(String hid, Scanner sc, Statement stmt) {
    String query = "INSERT INTO listing (home_type, longitude, latitude, city, addr, postal_code,"
      + " country, wifi, beds, bathrooms, kitchens, parking, other_accom, addtional_comment, host_id) VALUES ";
    query += "(";
    System.out.println("Enter the type of home: ");
    String home_type = sc.nextLine();
    query += "'"+home_type+"', ";
    
    String longitude;
    do {
      System.out.println("Enter the longtitude: ");
      longitude = sc.nextLine();
      if (!longitude.matches("^-?([1]?[1-7][1-9]|[1]?[1-8][0]|[1-9]?[0-9])\\.{1}\\d{1,6}")) {
        System.out.println("Invalid longitude");
      } else if (longitude.length() > 9) {
        System.out.println("Invalid length of longtitude(maximum 9 digits)");
      } else {
        query += longitude+", ";
        break;
      }
    } while (true);
    
    String latitude;
    do {
      System.out.println("Enter the latitude: ");
      latitude = sc.nextLine();
      //
      //-?[0-9]+(\\.[0-9][0-9]?)?
      // ^(\\\\+|-)?(?:90(?:(?:\\\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\\\.[0-9]{1,6})?))$
      if (!latitude.matches("^-?([1-8]?[1-9]|[1-9]0)\\.{1}\\d{1,6}")) {
        System.out.println("Invalid latitude");
      } else if (latitude.length() > 8) {
        System.out.println("Invalid length of longtitude(maximum 8 digits)");
      } else {
        query += latitude+", ";
        break;
      }
    } while (true);
    
    System.out.println("Enter your city: ");
    String city = sc.nextLine();
    query += "'"+city+"', ";
    System.out.println("Enter your address: ");
    String address = sc.nextLine();
    query += "'"+address+"', ";
    
 // valid postal_code input check
    String postal_code;
    do {
      System.out.println("Enter your postal code: ");
      postal_code = sc.nextLine();
      Pattern pattern = Pattern.compile("^[A-Z][0-9][A-Z][0-9][A-Z][0-9]$");
      if (!pattern.matcher(postal_code).matches()) {
        System.out.println("Invalid input, make sure there are no spaces and letters are capital");
      } else {
        query += ("'"+postal_code+"', ");
        break;
      }
    } while (true);
    
    System.out.println("Enter your country: ");
    String country = sc.nextLine();
    query += "'"+country+"', ";
    
    String wifi;
    do {
      System.out.println("Includes Wifi?(yes/no): ");
      wifi = sc.nextLine();
      if (wifi.equals("yes") || wifi.equals("no")) {
        query += "'"+wifi+"', ";
        break;
      } else {
        System.out.println("invalid input");
      }
    } while (true);
    
    String bedNum;
    do {
      System.out.println("Number of beds: ");
      bedNum = sc.nextLine();
      if (!bedNum.matches("\\d+")) {
        System.out.println("invalid number");
      } else {
        query += bedNum+", ";
        break;
      }
    } while (true);
    
    String bathroomNum;
    do {
      System.out.println("Number of bathrooms: ");
      bathroomNum = sc.nextLine();
      if (!bathroomNum.matches("\\d+")) {
        System.out.println("invalid number");
      } else {
        query += bathroomNum+", ";
        break;
      }
    } while (true);
   
    String kitchenNum;
    do {
      System.out.println("Number of kitchens: ");
      kitchenNum = sc.nextLine();
      if (!kitchenNum.matches("\\d+")) {
        System.out.println("invalid number");
      } else {
        query += kitchenNum+", ";
        break;
      }
    } while (true);
    
    String parkingNum;
    do {
      System.out.println("Number of parkings: ");
      parkingNum = sc.nextLine();
      if (!parkingNum.matches("\\d+")) {
        System.out.println("invalid number");
      } else {
        query += parkingNum+", ";
        break;
      }
    } while (true);
    
    System.out.println("Other accommodation: ");
    String accommodation = sc.nextLine();
    if (accommodation.length()==0) {
      query += "null, ";
    } else {
      query += "'"+accommodation+"', ";
    }
    
    System.out.println("additional comment: ");
    String additionalComment = sc.nextLine();
    if (additionalComment.length()==0) {
      query += "null, ";
    } else {
      query += "'"+additionalComment+"', ";
    }
    query += hid+");";
    try {
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    // create calender
    query = "INSERT INTO calendar (avaliable_from, avaliable_till, price, listing_id) VALUES ('";
    String startDate;
    do {
      System.out.println("Enter the start date of the listing(yyyy-mm-dd): ");
      startDate = sc.nextLine();
      Pattern pattern = Pattern.compile("^[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]$");
      if (!pattern.matcher(startDate).matches()) {
        System.out.println("Invalid date input");
      } else {
        query+= startDate+"', ";
        break;
      }
    } while(true);
    
    String endDate;
    do {
      System.out.println("Enter the end date of the listing(yyyy-mm-dd): ");
      endDate = sc.nextLine();
      Pattern pattern = Pattern.compile("^[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]$");
      if (!pattern.matcher(endDate).matches()) {
        System.out.println("Invalid date input");
      } else {
        query+= "'"+ endDate+"', ";
        break;
      }
    } while(true);
    
    String price;
    do {
      System.out.println("Price per day: ");
      price = sc.nextLine();
      if (!price.matches("\\d+")) {
        System.out.println("invalid number");
      } else {
        query += price+", ";
        break;
      }
    } while (true);
    
    // to retrieve the list id
    ResultSet newlyInsertedData;
    String lid;
    try {
      newlyInsertedData = stmt.executeQuery("SELECT * FROM listing WHERE longitude="+longitude+" AND latitude="+latitude+" AND host_id="+hid);
      if (newlyInsertedData.next()) {
        lid = ""+newlyInsertedData.getInt("Lid");
        query+= lid +");";
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    try {
      stmt.executeUpdate(query);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
 
 public static void createRenterComment(String writerId, Scanner sc, Statement stmt) {
//   boolean error = false;
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   String renterId = "";
   do {
     System.out.println("Enter the id of the renter whom you want to comment: ");
     renterId = sc.nextLine();
     if (renterId.equals("quit")) {
       break;
     }
     // check to see if the renter has stayed in host's listing before
     try {
      ResultSet r = stmt.executeQuery("SELECT * FROM history WHERE host_id="+writerId+" AND renter_id="+renterId+" AND status = 'Completed';");
      if (r.next()) {
        break;
      } else {
        System.out.println("You can't comment on ths renter yet because either he hasn't completed his stay at your listing");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
   } while (true);
   
   if (!renterId.equals("quit")) {
     System.out.println("Enter your comment: ");
     String comment = sc.nextLine();
     System.out.println("Enter rating: ");
     String rating = sc.nextLine();
     String date = sdf.format(Calendar.getInstance().getTime());
     String sql = "INSERT INTO renter_comment (rating, date, comment, comment_writer, comment_to_renter) VALUES ("+rating+", '"+date+"', '"+comment+"', "+writerId+
         ", "+renterId+");";
     try {
       stmt.executeUpdate(sql);
     } catch (SQLException e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
     }
   }
 }
 
 
 public static void createListComment(String writerId, Scanner sc, Statement stmt) {
   String ListId = "";
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   do {
     System.out.println("Enter the id of the list that you want to comment: ");
     ListId = sc.nextLine();
     if (ListId.equals("quit")) {
       break;
     }
     // check to see if the renter has stayed in host's listing before
     try {
      ResultSet r = stmt.executeQuery("SELECT * FROM history WHERE renter_id="+writerId+" AND list_id="+ListId+" AND status='"+"Completed';");
      if (r.next()) {
        break;
      } else {
        System.out.println("You can't comment on ths list yet because you havn't completed the stay");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
   } while (true);
   
   if (!ListId.equals("quit")) {
     System.out.println("Enter your comment: ");
     String comment = sc.nextLine();
     System.out.println("Enter rating: ");
     String rating = sc.nextLine();
     String date = sdf.format(Calendar.getInstance().getTime());
     String sql = "INSERT INTO list_comment (rating, date, comment, comment_writer, comment_to_list) VALUES ("+rating+", '"+date+"', '"+comment+"', "+writerId+
         ", "+ListId+");";
     try {
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
     
   }
 }
 
 
//method will return a value that will be used as part a create user query
public static void createBooking(String rid, Scanner sc, Statement stmt) throws SQLException {
  // calendar contains when a listing is avaialbe and the price. However, booking can book a date in between the avaiable dates
  boolean error = false;
  String listId;
  do {
    System.out.println("Enter the id of the listing: ");
    listId = sc.nextLine();
    if (!listId.matches("\\d+")) {
      System.out.println("invalid number");
    } else {
      ResultSet r = stmt.executeQuery("SELECT * FROM listing WHERE Lid="+listId+";");
      if (!r.next()) {
        System.out.println("list id does not exist");
      } else {
        break;
      }
    }
  } while (true);
  
  String startDate;
  String endDate;
  String sql;
  
  do {
    do {
      System.out.println("Enter start date of listing in year, month, day format(xxxx-xx-xx): ");
      startDate = sc.nextLine();
      Pattern pattern = Pattern.compile("^[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]$");
      if (!pattern.matcher(startDate).matches()) {
        System.out.println("Invalid date input");
      } else {
        break;
      }
    } while(true);
    do {
      System.out.println("Enter end date of listing in year, month, day format(xxxx-xx-xx): ");
      endDate = sc.nextLine();
      Pattern pattern = Pattern.compile("^[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]$");
      if (!pattern.matcher(endDate).matches()) {
        System.out.println("Invalid date input");
      } else {
        break;
      }
    } while(true);
    
    List<HashMap> calendarListings = getConsecutiveDateEntries(listId, startDate, endDate, stmt);
    if (calendarListings.size() == 0) {
      // incorrect 
    } else {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date startD = null;
      Date endD = null;
      try {
        startD = new Date(sdf.parse(startDate).getTime());
        endD = new Date(sdf.parse(endDate).getTime());
      } catch (ParseException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      
      if (calendarListings.size() == 1) {
        HashMap a = calendarListings.get(0);
        boolean updatedOnce = false;
        
        // check to see if it should be deleted
        if (((Date)a.get("avaliable_from")).compareTo(startD) == 0 && ((Date)a.get("avaliable_till")).compareTo(endD) == 0) {
          sql = "DELETE FROM calendar WHERE Caid="+a.get("Caid")+";";
          stmt.executeUpdate(sql);
        }
        
        // check front
        if (((Date)a.get("avaliable_from")).compareTo(startD) < 0) {
          Calendar c = Calendar.getInstance();
          c.setTime(startD);
          c.add(Calendar.DATE, -1);
          sql = "UPDATE calendar SET avaliable_till='"+sdf.format(c.getTime())+"' WHERE listing_id="+a.get("listing_id")+";";
          stmt.executeUpdate(sql);
          updatedOnce = true;
        }
        
        // check back
        if (((Date)a.get("avaliable_till")).compareTo(endD) > 0) {
          Calendar d = Calendar.getInstance();
          d.setTime(endD);
          d.add(Calendar.DATE, 1);
          // update original if it hasnt been updated it. Create a new one if it did
          if (!updatedOnce) {
            sql = "UPDATE calendar SET avaliable_from='"+sdf.format(d.getTime())+"' WHERE listing_id="+a.get("listing_id")+";";
            stmt.executeUpdate(sql);
          }else {
            sql = "INSERT INTO calendar (avaliable_from, avaliable_till, price, listing_id) VALUES ('"+sdf.format(d.getTime())+"', '"+sdf.format(a.get("avaliable_till"))+
                "', "+a.get("price")+", "+a.get("listing_id")+");";
            System.out.println(sql);
            stmt.executeUpdate(sql);
          }
        }
        // plus one because booking days are inclusive
        int totalDays = getDateDiff(startD,endD,TimeUnit.DAYS)+1;
        // get host id
        ResultSet hlist = stmt.executeQuery("Select * FROM listing WHERE Lid="+listId+";");
        int hostId = 0;
        if (hlist.next()) {
          hostId = hlist.getInt("host_id");
        }
        // insert data into history
        sql = "INSERT INTO history (start, end, transaction_date, cost_per_day, total_cost, status, host_id, renter_id, list_id) VALUES ('"+startDate+"', '"+endDate+"', '"+sdf.format(Calendar.getInstance().getTime())+"', "+a.get("price")+
            ", "+(totalDays*(Double)a.get("price"))+", "+"'Pending', "+hostId+", "+rid+", "+a.get("listing_id")+");";
        stmt.executeUpdate(sql);
        break;
      }else {
        HashMap a = null;
        for (int i = 0; i < calendarListings.size(); i++) {
          a = calendarListings.get(i);
          ResultSet hlist = stmt.executeQuery("Select * FROM listing WHERE Lid="+listId+";");
          int hostId = 0;
          if (hlist.next()) {
            hostId = hlist.getInt("host_id");
          }
          // handle first element
          if (i == 0) {
            Date start;
            if (((Date)a.get("avaliable_from")).compareTo(startD) < 0) {
              Calendar c = Calendar.getInstance();
              c.setTime(startD);
              c.add(Calendar.DATE, -1);
              sql = "UPDATE calendar SET avaliable_till='"+sdf.format(c.getTime())+"' WHERE listing_id="+a.get("listing_id")+";";
              stmt.executeUpdate(sql);
              start = startD;
            } else {
              start = (Date) a.get("avaliable_from");
              // delete
              System.out.println(1);
              sql = "DELETE FROM calendar WHERE Caid="+a.get("Caid")+";";
              stmt.executeUpdate(sql);
            }
            int totalDays = getDateDiff(start,(Date)a.get("avaliable_till"),TimeUnit.DAYS)+1;
            // create history entry
            sql = "INSERT INTO history (start, end, transaction_date, cost_per_day, total_cost, status, host_id, renter_id, list_id) VALUES ('"+sdf.format(start)+"', '"+sdf.format(a.get("avaliable_till"))+"', '"+sdf.format(Calendar.getInstance().getTime())+"', "+a.get("price")+
                ", "+(totalDays*(Double)a.get("price"))+", "+"'Pending', "+hostId+", "+rid+", "+a.get("listing_id")+");";
            stmt.executeQuery(sql);
          // handle last element
          } else if (i == calendarListings.size()-1) {
            Date end;
            if (((Date)a.get("avaliable_till")).compareTo(startD) > 0) {
              Calendar c = Calendar.getInstance();
              c.setTime(startD);
              c.add(Calendar.DATE, 1);
              sql = "UPDATE calendar SET avaliable_from='"+sdf.format(c.getTime())+"' WHERE listing_id="+a.get("listing_id")+";";
              stmt.executeUpdate(sql);
              end = endD;
            } else {
              end = (Date) a.get("avaliable_till");
              System.out.println(2);
              // delete
              sql = "DELETE FROM calendar WHERE Caid="+a.get("Caid")+";";
              stmt.executeUpdate(sql);
            }
            // create history entry
            int totalDays = getDateDiff((Date)a.get("avaliable_from"), end, TimeUnit.DAYS)+1;
            sql = "INSERT INTO history (start, end, transaction_date, cost_per_day, total_cost, status, host_id, renter_id, list_id) VALUES ('"+sdf.format(a.get("avaliable_from"))+"', '"+sdf.format(end)+"', '"+sdf.format(Calendar.getInstance().getTime())+"', "+a.get("price")+
                ", "+(totalDays*(Double)a.get("price"))+", "+"'Pending', "+hostId+", "+rid+", "+a.get("listing_id")+");";
            stmt.executeUpdate(sql);
          // handle all middle elements
          } else {
            // create history entry
            int totalDays = getDateDiff((Date)a.get("avaliable_from"), (Date)a.get("avaliable_till"), TimeUnit.DAYS)+1;
            sql = "INSERT INTO history (start, end, transaction_date, cost_per_day, total_cost, status, host_id, renter_id, list_id) VALUES ('"+sdf.format(a.get("avaliable_from"))+"', '"+sdf.format(a.get("avaliable_till"))+"', '"+sdf.format(Calendar.getInstance().getTime())+"', "+a.get("price")+
                ", "+(totalDays*(Double)a.get("price"))+", "+"'Pending', "+hostId+", "+rid+", "+a.get("listing_id")+");";
            stmt.executeUpdate(sql);
            System.out.println(3);
            // delete calendar entry
            sql = "DELETE FROM calendar WHERE Caid="+a.get("Caid")+";";
            stmt.executeUpdate(sql);
          }
        }
        break;
      }
    }
  }while(true);
}


 public static void changePrice(String hostId, Scanner sc, Statement stmt) {
   boolean error = false;
   String listId;
   do {
     System.out.println("Enter the id of the listing: ");
     listId = sc.nextLine();
     try {
      ResultSet listings = stmt.executeQuery("SELECT * FROM calendar WHERE listing_id = (SELECT Lid FROM listing WHERE host_id ="+hostId+");");
      if (!listId.matches("\\d+")) {
        System.out.println("invalid number");
      } else if (listings.next()){
        if (listId.equals(""+listings.getInt("listing_id"))) {
          break;
        } else {
          System.out.println("You can't adjust the price of the listing with id you inputted");
        }
      }else {
        System.out.println("You don't have any availiable listing to change price");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
   } while (true);
   
   String newPrice;
   do {
     System.out.println("Enter the new price of listing: ");
     newPrice = sc.nextLine();
     if (!newPrice.matches("\\d+")) {
       System.out.println("invalid number");
     } else {
       break;
     }
   } while (true);
   
   String startDate;
   String endDate;
   String sql;
   
   do {
     do {
       System.out.println("Enter start date of listing in year, month, day format(xxxx-xx-xx): ");
       startDate = sc.nextLine();
       Pattern pattern = Pattern.compile("^[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]$");
       if (!pattern.matcher(startDate).matches()) {
         System.out.println("Invalid date input");
       } else {
         break;
       }
     } while(true);
     do {
       System.out.println("Enter end date of listing in year, month, day format(xxxx-xx-xx): ");
       endDate = sc.nextLine();
       Pattern pattern = Pattern.compile("^[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]$");
       if (!pattern.matcher(endDate).matches()) {
         System.out.println("Invalid date input");
       } else {
         break;
       }
     } while(true);
     List<HashMap> calendarListings = getConsecutiveDateEntries(listId, startDate, endDate, stmt);
     if (calendarListings.size() == 0) {
       // error with date input
     } else {
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       Date startD = null;
       Date endD = null;
       try {
         startD = new Date(sdf.parse(startDate).getTime());
         endD = new Date(sdf.parse(endDate).getTime());
         if (calendarListings.size() == 1) {
           HashMap a = calendarListings.get(0);
           boolean updatedOnce = false;
           
           // check front
           if (((Date)a.get("avaliable_from")).compareTo(startD) < 0) {
             Calendar c = Calendar.getInstance();
             c.setTime(startD);
             c.add(Calendar.DATE, -1);
             sql = "UPDATE calendar SET avaliable_till='"+sdf.format(c.getTime())+"' WHERE listing_id="+a.get("listing_id")+";";
             stmt.executeUpdate(sql);
             updatedOnce = true;
           }
           
           // check back
           if (((Date)a.get("avaliable_till")).compareTo(endD) > 0) {
             Calendar d = Calendar.getInstance();
             d.setTime(endD);
             d.add(Calendar.DATE, 1);
             // update original if it hasnt been updated it. Create a new one if it did
             if (!updatedOnce) {
               sql = "UPDATE calendar SET avaliable_from='"+sdf.format(d.getTime())+"' WHERE listing_id="+a.get("listing_id")+";";
               stmt.executeUpdate(sql);
               updatedOnce = true;
             }else {
               sql = "INSERT INTO calendar (avaliable_from, avaliable_till, price, listing_id) VALUES ('"+sdf.format(d.getTime())+"', '"+sdf.format(a.get("avaliable_till"))+
                   "', "+a.get("price")+", "+a.get("listing_id")+");";
               stmt.executeUpdate(sql);
             }
           }
           
           // update price for the dates specified
           // if it hasnt been updated, then we can just go ahead and just update the original entry with new price
           if (!updatedOnce) {
             sql = "UPDATE calendar SET price="+newPrice+" WHERE listing_id="+a.get("listing_id")+";";
             stmt.executeUpdate(sql);
           } else {
             // if updated, then we need to insert a new entry with the new price
             sql = "INSERT INTO calendar (avaliable_from, avaliable_till, price, listing_id) VALUES ('"+startDate+"', '"+endDate+
                 "', "+newPrice+", "+a.get("listing_id")+");";
             stmt.executeUpdate(sql);
           }
           
         }else {
           HashMap a = null;
           for (int i = 0; i < calendarListings.size(); i++) {
             a = calendarListings.get(i);
             
             // handle first entry
             if (i == 0) {
                // split entry
               if (((Date)a.get("avaliable_from")).compareTo(startD) < 0) {
                 Calendar c = Calendar.getInstance();
                 c.setTime(startD);
                 c.add(Calendar.DATE, -1);
                 sql = "UPDATE calendar SET avaliable_till='"+sdf.format(c.getTime())+"' WHERE listing_id="+a.get("listing_id")+";";
                 stmt.executeQuery(sql);
                 // if updated, then we need to insert a new entry with the new price
                 sql = "INSERT INTO calendar (avaliable_from, avaliable_till, price, listing_id) VALUES ('"+startDate+"', '"+sdf.format(a.get("avaliable_till"))+
                     "', "+newPrice+", "+a.get("listing_id")+");";
                 stmt.executeUpdate(sql);
               // convert entry
               } else {
                 sql = "UPDATE calendar SET price="+newPrice+" WHERE listing_id="+a.get("listing_id")+";";
                 stmt.executeUpdate(sql);
               }
             // handle last entry
             } else if (i == calendarListings.size()-1) {
               // split entry
               if (((Date)a.get("avaliable_till")).compareTo(endD) > 0 && ((Date)a.get("avaliable_from")).compareTo(endD) < 0) {
                 Calendar c = Calendar.getInstance();
                 c.setTime(endD);
                 c.add(Calendar.DATE, 1);
                 sql = "UPDATE calendar SET avaliable_from='"+sdf.format(c.getTime())+"' WHERE listing_id="+a.get("listing_id")+";";
                 stmt.executeUpdate(sql);
                 // if updated, then we need to insert a new entry with the new price
                 sql = "INSERT INTO calendar (avaliable_from, avaliable_till, price, listing_id) VALUES ('"+sdf.format(a.get("avaliable_from"))+"', '"+endDate+
                     "', "+newPrice+", "+a.get("listing_id")+");";
                 stmt.executeUpdate(sql);
               // convert entry
               } else {
                 sql = "UPDATE calendar SET price="+newPrice+" WHERE listing_id="+a.get("listing_id")+";";
                 stmt.executeUpdate(sql);
               }
               
             // handle middle entries
             }else {
               sql = "UPDATE calendar SET price="+newPrice+" WHERE listing_id="+a.get("listing_id")+";";
               stmt.executeUpdate(sql);
             }
           }
           
         }
       } catch (ParseException | SQLException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
       }
       
       break;
     }
   } while(true);
 }
}
