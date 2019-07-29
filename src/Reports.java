import java.sql.*;
import java.util.*;
import java.util.Date;

import org.apache.log4j.BasicConfigurator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.BasicConfigurator;
import edu.stanford.nlp.coref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;


public class Reports {

	// get countries
	private static List<String> get_countries() throws ClassNotFoundException, SQLException {
		String query = "SELECT DISTINCT country FROM listing;";
		List<String> country = new ArrayList<String>();
		ResultSet rs = bnb_util.execute_query(query);
		while(rs.next()){
			//Retrieve by column name
			country.add(rs.getString("country"));
		}
		return country;
	}

	// get all cities by country or all, asumming city names are unique between countries
	private static List<String> get_cities(String country) throws ClassNotFoundException, SQLException {
		String query = "SELECT DISTINCT city FROM listing";
		if (country != "all") {
			query = "SELECT DISTINCT city FROM listing WHERE country = '" + country + "'";
		}
		query = query + ";";
		List<String> cities = new ArrayList<String>();
		ResultSet rs = bnb_util.execute_query(query);
		while(rs.next()){
			//Retrieve by column name
			cities.add(rs.getString("city"));
		}
		return cities;
	}
	
	// get all postal codes by city
	// get postal_code
	private static List<String> get_postal(String city) throws ClassNotFoundException, SQLException {
		String query = "SELECT DISTINCT postal_code FROM listing WHERE city = '" + city + "';";
		List<String> postal_codes = new ArrayList<String>();
		ResultSet rs = bnb_util.execute_query(query);
		while(rs.next()){
			//Retrieve by column name
			postal_codes.add(rs.getString("postal_code"));
		}
		return postal_codes;
	}
	
	// get all listings
	private static List<Integer> get_listing() throws ClassNotFoundException, SQLException {
		String query = "SELECT DISTINCT Lid FROM listing;";
		List<Integer> Lids = new ArrayList<Integer>();
		ResultSet rs = bnb_util.execute_query(query);
		while(rs.next()){
			//Retrieve by column name
			Lids.add(rs.getInt("Lid"));
		}
		return Lids;
	}
	
	// check if tree is a personal pronoun
	private static boolean notPRP (Tree tree) {
		  for (Tree subtree : tree) {
			  if (subtree.label().value().contentEquals("PRP")) {
				  return false;
			  }
		  }
		  return true;
	  }
	
	// find noun phrases in given string
	private static List<String> parseNP(String comment) {
		 // initialize the log4j system
	    BasicConfigurator.configure(); 
	     
	    // creates a StanfordCoreNLP object, with POS tagging, lemmatization,
	    Properties props = new Properties();

	    // in the actual application, you would only load the annotators that you need
	    props.setProperty("annotators", "tokenize, ssplit, pos, parse");
	    
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(comment);

	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    

	    List<String> nps = new ArrayList<String>();
	    // for every sentence, get the annotations
	    for (CoreMap sentence : sentences) {
	        
	        Tree tree = sentence.get(TreeAnnotation.class);
	        for (Tree subtree: tree) {
	          if(subtree.label().value().equals("NP")) {
	          	String np = "";
	        	
	          	if (Reports.notPRP(subtree)) {
	          		List<Word> npw = subtree.yieldWords();
	          		for (Word w : npw) {
	          			np = np + " " + w.toString();
	          		}
	            	nps.add(np.trim());
	          	}
	          }
	        }
	    }
	    return nps;
	}
	
	// find the top used noun phrases in each listing's comments
	public static Map<Integer, TreeMap<String, Integer>> wordCloud() throws ClassNotFoundException, SQLException {
		List<Integer> Lids = Reports.get_listing();
		Map<Integer, TreeMap<String, Integer>> result = new HashMap<Integer, TreeMap<String, Integer>>();
		
		for (Integer Lid : Lids) {
			// find the comments for every listing
			String query = "SELECT comment FROM list_comment WHERE comment_to_list = " + Lid + ";";
			ResultSet rs = bnb_util.execute_query(query);
			// parse each comment for noun phrases and count them in a treeMap
			TreeMap<String, Integer> cMap = new TreeMap<String, Integer>(Collections.reverseOrder());
			while(rs.next()) {
				String comment = rs.getString("comment");
				List<String> listNp = Reports.parseNP(comment);
				for (String np: listNp) {
					if (cMap.containsKey(np)) {
						cMap.put(np, cMap.get(np) + 1);
					} else {
						cMap.put(np, 1);
					}
				}
			}
			result.put(Lid, cMap);
		}
		return result;
	}

	// rank renters and hosts (seperately) by the number of cancelation they have
	public static List<List<Integer>> rankCancel() throws ClassNotFoundException, SQLException {
		String renter_query = "select * from (select Uid, count(*) total_can from (select * from (history h inner join user u on u.uid = h.renter_id) where status = 'canceled')"
								+ "as rental_history group by Uid Desc) as rental_history2 where total_can >= 2 Order by total_can Desc;";
		String host_query = "select * from (select Uid, count(*) total_can from (select * from (history h inner join user u on u.uid = h.host_id) where status = 'canceled')"
								+ "as rental_history group by Uid Desc) as rental_history2 where total_can >= 2 Order by total_can Desc;";
		ResultSet canceled_renter = bnb_util.execute_query(renter_query);
		ResultSet canceled_host = bnb_util.execute_query(host_query);
		List<Integer> cr = new ArrayList<Integer>();
		List<Integer> ch = new ArrayList<Integer>();
		
		// parse each query result set
		while(canceled_renter.next()) {
			cr.add(canceled_renter.getInt("Uid"));
		}
		
		while(canceled_host.next()) {
			ch.add(canceled_host.getInt("Uid"));
		}
		
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		result.add(cr);
		result.add(ch);
		
		return result;
	}
	
	// rank renters by the number of bookings they have
	public static Map<String, List<Integer>> rankRenter(boolean by_city, Date date_start, Date date_end) throws ClassNotFoundException, SQLException {
		String sDate_start = bnb_util.date_to_string(date_start);
		String sDate_end = bnb_util.date_to_string(date_end);
		Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
		
		// option to be refined by renters from each city
		if (by_city) {
			List<String> cities = Reports.get_cities("all");
			for (String city : cities) {
				String query = "select Uid from (select Uid, count(*) total from (Select * from (user u inner join history h on u.Uid = h.renter_id inner join (select Lid, city as lcity from listing) s on h.list_id = s.Lid)"
								+ " where (transaction_date between '" + sDate_start + "' AND '" + sDate_end + "') AND lcity = '" + city + "') as renter_hist Group by Uid) as renter_book_count where total > 1;";
				ResultSet rs = bnb_util.execute_query(query);
				List<Integer> renters = new ArrayList<Integer>();
				
				while(rs.next()) {
					renters.add(rs.getInt("Uid"));
				}
				result.put(city + " between " + sDate_start + " AND " + sDate_end + " ", renters);
			}
		} else {
			String query = "select Uid from (select Uid, count(*) total from (Select * from (user u inner join history h on u.Uid = h.renter_id) where transaction_date "
					+ "between '"+ sDate_start + "' AND '" + sDate_end + "') as renter_hist Group by Uid) as renter_book_count where total > 1;";
			
			ResultSet rs = bnb_util.execute_query(query);
			List<Integer> renters = new ArrayList<Integer>();
			
			while(rs.next()) {
				renters.add(rs.getInt("Uid"));
			}
			result.put("between " + sDate_start + " AND " + sDate_end + " ", renters);
		}
	
		return result;
	}
	
	// identify hosts in every country and city who holds listings that is greater than 10 percent of the total number of listings in that country and city
	public static Map<String, List<Integer>> identifyCommercialHost() throws ClassNotFoundException, SQLException {
		Map<String, List<List<Integer>>> host_listing_count_cc = rankHost(true);
		Map<String, List<List<Integer>>> host_listing_count_c = rankHost(false);

		Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
		
		for (String key : host_listing_count_c.keySet()) {
			String query = "Select sum(listingCount) totalList from (select Uid, count(*) listingCount From (Select * from (user u inner join (select Lid, host_id, country as lcountry, city as lcity from listing) l on u.Uid = l.host_id) where "
					+ "lcountry = '" + key + "') as user_listing GROUP BY Uid Desc) as user_listing_total";
			ResultSet rs = bnb_util.execute_query(query);
			int total_listings = 0;
			while(rs.next()) {
				total_listings = rs.getInt("totalList");
			}
			List<Integer> flagged_hosts = new ArrayList<Integer>();
		    for (List<Integer> host: host_listing_count_c.get(key)) {
		    	if (host.get(1) > Math.floor(total_listings*0.1)) {
		    		flagged_hosts.add(host.get(0));
		    	}
		    }
		    result.put(key, flagged_hosts);
		}
		for (String key : host_listing_count_cc.keySet()) {
			String[] parts = key.split(" ");
			// query the total number of listings in that country and city
			String query = "Select sum(listingCount) totalList from (select Uid, count(*) listingCount From "
					+ "(Select * from (user u inner join (select Lid, host_id, country as lcountry, city as lcity from listing) l on u.Uid = l.host_id) where "
					+ "lcountry = '" + parts[0] + "' AND lcity = '" + parts[1] + "') as user_listing GROUP BY Uid Desc) as user_listing_total";
			ResultSet rs = bnb_util.execute_query(query);
			int total_listings = 0;
			while(rs.next()) {
				total_listings = rs.getInt("totalList");

			}
			
			// iterate through each host to identify which host have more than 10 percent of the total number of listings
			List<Integer> flagged_hosts = new ArrayList<Integer>();
		    for (List<Integer> host: host_listing_count_cc.get(key)) {
		    	if (host.get(1) > Math.floor(total_listings*0.1)) {
		    		flagged_hosts.add(host.get(0));
		    	}
		    }
		    result.put(key, flagged_hosts);
		}
		
		return result;
	}
	
	// rank hosts by the number of listings they have per country or country and city
	public static Map<String, List<List<Integer>>> rankHost(boolean by_city) throws ClassNotFoundException, SQLException {
		Map<String, List<List<Integer>>> result = new HashMap<String, List<List<Integer>>>();
		List<String> countries = Reports.get_countries();
		for (String country : countries) {
			if (by_city) {
				List<String> cities = Reports.get_cities(country);
				for (String city : cities) {
					String query = "Select Uid, count(*) totalCount From (Select * from (user u inner join (select Lid, host_id, country as lcountry, city as lcity from listing)"
							+ " l on u.Uid = l.host_id) where lcountry = '" + country + "' AND lcity = '" + city + "') as user_listing group by Uid Desc;";
					ResultSet rs = bnb_util.execute_query(query);
					List<List<Integer>> rank = new ArrayList<List<Integer>>();
					while(rs.next()) {
						List<Integer> pair = new ArrayList<Integer>();
						pair.add(rs.getInt("Uid"));
						pair.add(rs.getInt("totalCount"));
						rank.add(pair);
					}
					result.put(country + " " + city, rank);
				}
			} else {
				String query = "Select Uid, count(*) totalCount From (Select * from (user u inner join (select Lid, host_id, country as lcountry, city as lcity from listing) l on u.Uid = l.host_id) where "
								+ "lcountry = '" + country + "') as user_listing group by Uid Desc;";
				ResultSet rs = bnb_util.execute_query(query);
				List<List<Integer>> rank = new ArrayList<List<Integer>>();
				while(rs.next()) {
					List<Integer> pair = new ArrayList<Integer>();
					pair.add(rs.getInt("Uid"));
					pair.add(rs.getInt("totalCount"));
					rank.add(pair);
				}
				result.put(country, rank);
			}
		}
	
		return result;
	}
	
	// report the total number of listings per country, per country and city, or country, city and postal code
	public static Map<String, String> reportListing(boolean by_city, boolean by_postalCity) throws ClassNotFoundException, SQLException {
		Map<String, String> result = new HashMap<String, String>();
		List<String> countries = Reports.get_countries();
		for (String country : countries) {
			if (by_city || by_postalCity) {
				List<String> cities = Reports.get_cities(country);
				for (String city : cities) {
					if (by_postalCity) {
						List<String> postals = Reports.get_postal(city);
						for (String postal : postals) {
							String query = "SELECT count(*) FROM listing WHERE (country = '" + country + "') AND (city = '" + city + "') AND (postal_code = '" + postal + "');";
							ResultSet rs = bnb_util.execute_query(query);
							while(rs.next()) {
								result.put(country + " " + city + " " + postal, Integer.toString(rs.getInt("count(*)")));
							}
						}
					} else {
						String query = "SELECT count(*) FROM listing WHERE (country = '" + country + "') AND (city = '" + city + "');";
						ResultSet rs = bnb_util.execute_query(query);
						while(rs.next()) {
							result.put(country + " " + city, Integer.toString(rs.getInt("count(*)")));
						}
					}
				}
			} else {
				String query = "SELECT count(*) FROM listing WHERE (country = '" + country + "');";
				ResultSet rs = bnb_util.execute_query(query);
				while(rs.next()) {
					result.put(country, Integer.toString(rs.getInt("count(*)")));
				}
			}
		}

		return result;
	}
	
	// report the total number of bookings per city, or postal codes within a city
	public static Map<String, String> reportBooking(boolean by_postal, Date date_start, Date date_end) throws ClassNotFoundException, SQLException {
		String sDate_start = bnb_util.date_to_string(date_start);
		String sDate_end = bnb_util.date_to_string(date_end);

		List<String> cities = Reports.get_cities("all");
		Map<String, String> result = new HashMap<String, String>();
		for (String city : cities) {
			if (by_postal) {
				List<String> postals = Reports.get_postal(city);
				for (String postal : postals) {
					String query = "SELECT count(*) FROM (history h join listing l on h.list_id = l.Lid) WHERE (city = '" + city + "') "
									+ "AND (transaction_date >= '" + sDate_start + "') AND (transaction_date <= '" + sDate_end + "')";
					query = query + " AND (postal_code = '" + postal + "')" + ";";
					ResultSet rs = bnb_util.execute_query(query);
					while(rs.next()) {
						result.put(city + " " + postal + " between " + date_start + " AND " + date_end, Integer.toString(rs.getInt("count(*)"))); 
					}
				}
			} else {
				String query = "SELECT count(*) FROM (history h join listing l on h.list_id = l.Lid) WHERE (city = '" + city + "') "
								+ "AND (transaction_date >= '" + sDate_start + "') AND (transaction_date <= '" + sDate_end + "');";
				ResultSet rs = bnb_util.execute_query(query);
				while(rs.next()) {
					result.put(city + " between " + date_start + " AND " + date_end, Integer.toString(rs.getInt("count(*)"))); 
				}
			}
		}
		return result;
	}
}
