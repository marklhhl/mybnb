import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CommandLine {
  
  private Scanner sc = null;
  private Connection conn = null;
  private Statement stmt = null;
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
  }
  
  public boolean execute() {
    if (sc != null && stmt != null && conn != null) {
      System.out.println("");
      System.out.println("***************************");
      System.out.println("******ACCESS GRANTED*******");
      System.out.println("***************************");
      System.out.println("");
      
      String input = "";
      int choice = -1;
      do {
//        menu(); //Print Menu
        input = sc.nextLine();
        try {
          choice = Integer.parseInt(input);
          switch (choice) { //Activate the desired functionality
          case 1:
            Operations.createUser(sc);
            break;
          }
        } catch (NumberFormatException e) {
          input = "-1";
        }
        System.out.println("done");
      } while (input.compareTo("0") != 0);
      
      return true;
    } else {
      System.out.println("");
      System.out.println("Connection could not been established! Bye!");
      System.out.println("");
      return false;
    }
  }

}
