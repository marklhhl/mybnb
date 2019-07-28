import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TestRunner2 {
  
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
 // valid sin input check
//    String wifi;
//    do {
//      System.out.println("Includes Wifi?(yes/no): ");
//      wifi = sc.nextLine();
//      if (wifi.equals("yes") || wifi.equals("no")) {
////        query += "'"+wifi+"', ";
//        break;
//      } else {
//        System.out.println("invalid input");
//      }
//    } while (true);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date startD = new Date(sdf.parse("2020-10-01").getTime());
      Calendar c = Calendar.getInstance();
      c.setTime(startD);
      c.add(Calendar.DATE, -1);
      System.out.println(sdf.format(startD));
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
//    System.out.println(sdf.format(Calendar.getInstance().getTime()));
    
  }

}
