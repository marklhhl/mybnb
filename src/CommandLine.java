import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CommandLine {
  
  private static Scanner sc = null;
  private Connection conn = null;
  private static Statement stmt = null;
  private static final String dbClassName = "com.mysql.cj.jdbc.Driver";
  private static final String CONNECTION = "jdbc:mysql://127.0.0.1/mybnb";
  
  public boolean startSession() {
    boolean success = true;
  //Register JDBC driver
    try {
      Class.forName(dbClassName);
    } catch (ClassNotFoundException e1) {
      success = false;
      // TODO Auto-generated catch block
      System.out.println("JDBC driver registration triggered an exception!");
      e1.printStackTrace();
    }
    if (sc == null) {
      sc = new Scanner(System.in);
    }
    //Database credentials
    final String USER = "root";
    final String PASS = "";
    System.out.println("Connecting to database...");
    
    try {
      //Establish connection
      conn = DriverManager.getConnection(CONNECTION,USER,PASS);
      System.out.println("Successfully connected to 'mybnb' as user: root");
      stmt = conn.createStatement();
    } catch (SQLException e) {
      System.out.println("Error with connecting to database");
      System.err.println(e);
      success = false;
      sc = null;
      conn = null;
      stmt = null;
    }
    
    return success;
  }
  
  public void endSession() {
    System.out.println("Ending session");
    if (conn != null)
      try {
        conn.close();
      } catch (SQLException e) {
        System.out.println("Error with closing connection");
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    if (sc != null) {
      sc.close();
    }
    conn = null;
    sc = null;
    stmt = null;
    System.out.println("Session ended");
  }
  
  public boolean execute() {
    if (sc != null && stmt != null && conn != null) {
      System.out.println("");
      System.out.println("***************************");
      System.out.println("******ACCESS GRANTED*******");
      System.out.println("***************************");
      System.out.println("");
      menu();
      
      
      
      return true;
    } else {
      System.out.println("");
      System.out.println("Connection could not been established! Bye!");
      System.out.println("");
      return false;
    }
  }
  
  private static void menu() {
    String input = "";
    int choice = -1;
    do {
      System.out.println("=========MENU=========");
      System.out.println("0. Exit.");
      System.out.println("1. Login.");
      System.out.println("2. Create an account.");
      System.out.print("Choose one of the previous options [0-2]: ");
      input = sc.nextLine();
      try {
        choice = Integer.parseInt(input);
        switch (choice) { //Activate the desired functionality
        case 1:
          login();
          break;
        case 2:
          Operations.createUser(sc, stmt);
          break;
        }
      } catch (NumberFormatException e) {
        input = "-1";
      }
    } while (input.compareTo("0") != 0);
  }
  
  public static void login() {
    System.out.println("Please enter your email: ");
    String email = sc.nextLine();
    System.out.println("Please enter your password: ");
    String password = sc.nextLine();
    
    String query = "SELECT * FROM user WHERE email='"+email+"' AND password='"+password+"';";
    try {
      ResultSet user = bnb_util.execute_query2(query, stmt);
      if (user.next()) {
        // go to user action page
        userActionPage(user.getInt("Uid"));
      } else {
        System.out.println("Incorrect email or password");
      }
    } catch (ClassNotFoundException | SQLException e) {
      System.out.println("Error with logging in");
      e.printStackTrace();
    }
  }
  
  public static void userActionPage(int user_id) {
    String input = "";
    int choice = -1;
    do {
      System.out.println("=========USER MENU=========");
      System.out.println("0. Logout.");
      System.out.println("1. List a unit for rent.");
      System.out.println("2. Rent a unit.");
      System.out.println("3. Comment a listing.");
      System.out.println("4. Comment a renter.");
      System.out.println("5. Search for listings.");
      System.out.print("Choose one of the previous options [0-5]: ");
      input = sc.nextLine();
      try {
        choice = Integer.parseInt(input);
        switch (choice) { //Activate the desired functionality
        case 1:
          Operations.createListing(""+user_id, sc, stmt);
          break;
        case 2:
          Operations.createBooking(""+user_id, sc, stmt);
          break;
        case 3:
          Operations.createListComment(""+user_id, sc, stmt);
          break;
        case 4:
          Operations.createRenterComment(""+user_id, sc, stmt);
          break;
        case 5:
          queryPage();
          break;
        }
      } catch (NumberFormatException e) {
        input = "-1";
      }
    } while (input.compareTo("0") != 0);
    System.out.println("You have been logged out");
  }
  
  public static void queryPage() {
    String input = "";
    int choice = -1;
    do {
      System.out.println("=========Search Listings=========");
      System.out.println("0. Exit.");
      System.out.println("1. Search by address.");
      System.out.println("2. Search by postal code.");
      System.out.println("3. Search by longitude and latitude.");
      System.out.print("Choose one of the previous options [0-3]: ");
      input = sc.nextLine();
      try {
        choice = Integer.parseInt(input);
        switch (choice) { //Activate the desired functionality
        case 1:
          Operations.querySearch(1, sc, stmt);
          break;
        case 2:
          Operations.querySearch(2, sc, stmt);
          break;
        case 3:
          Operations.querySearch(3, sc, stmt);
          break;
        }
      } catch (NumberFormatException e) {
        input = "-1";
      }
    } while (input.compareTo("0") != 0);
  }

}
