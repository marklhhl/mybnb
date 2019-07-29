import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class bnb_main_test {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		bnb_util.getConnection();
		//System.out.println(Reports.identifyCommercialHost());
	
		//System.out.println(Reports.wordCloud());
		System.out.println(Toolkit.suggest_price("M2M3A6", 10, "2019-10-22", "2019-10-30", ));
	}
}
