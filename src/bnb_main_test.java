import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class bnb_main_test {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		bnb_util.getConnection();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
//		Date d = sdf.parse("2019/03/13");
//		Date d2= sdf.parse("2019/12/12");
//		
//		Map<String, List<Integer>> rs = reports.rankRenter(true, d, d2);

		System.out.println(reports.rankCancel());

	}
}
