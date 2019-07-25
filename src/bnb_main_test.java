import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class bnb_main_test {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		bnb_util.getConnection();
		System.out.println(reports.get_cities());
	}
}
