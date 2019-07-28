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
//		Date d = sdf.parse("2019/05/17");
//		Date d2= sdf.parse("2019/06/30");
//
////		Map<String, List<Integer>> rs = reports.rankRenter(true, d, d2);
//		Queries q = new Queries("M2M3A6").withDateRange(d, d2, true);
//		String qy = q.prepare_query();
//		ResultSet rs = bnb_util.execute_query(qy);
//		while(rs.next()) {
//			System.out.println(rs.getInt("Lid"));
//		}
		//System.out.println(Reports.parseNP("the owner had provided a very clean house"));
		System.out.println(Reports.wordCloud());
	}
}
