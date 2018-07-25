
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class DomParser {
	
	static HashMap<String, Movie> moviesToAdd;
	static HashMap<String, StarInMovie> starInMovie;
	static HashMap<String, Actor> actorsToAdd;
	//static List<Actor> actorsToAdd;
	static Connection dbcon;
	
	static int movieIDcounter;
	static int starIDcounter;
	
    Document dom;
    
	public DomParser() {
		moviesToAdd = new HashMap<String, Movie>();
		starInMovie = new HashMap<String, StarInMovie>();
		actorsToAdd = new HashMap<String, Actor>();
		//actorsToAdd = new ArrayList<Actor>();
    }

    public void runParser() throws SQLException{
    	
        parseXmlFile("../project2/xml/mains243.xml");
        parseFilmDocument();

        parseXmlFile("../project2/xml/casts124.xml");
        parseCastDocument();
        
        parseXmlFile("../project2/xml/actors63.xml");
        parseActorDocument();
    	
    }

    private void parseXmlFile(String xmlpath) {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            //parse using builder to get DOM representation of the XML file
            dom = db.parse(xmlpath);
            dom.normalize();

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void parseFilmDocument() throws SQLException{
    	String maxIDquery = "SELECT max(id) from movies";
    	Statement getMaxIDstatement = dbcon.createStatement();
    	ResultSet rs = getMaxIDstatement.executeQuery(maxIDquery);
    	rs.next();
    	String latest_id = rs.getString("max(id)");
    	Integer new_id = Integer.parseInt(latest_id.substring(2));
    	movieIDcounter = new_id;
    	
    	
    	String dirname_string = "";
    	String fid_string = "";
    	String title_string = "";
    	String year_string = "";
    	String genre_string = "";
        //get the root element
        Element docEle = dom.getDocumentElement();

        //get a nodelist of <directorfilms> elements
        NodeList directorFilms = docEle.getElementsByTagName("directorfilms"); // LIST OF <directorfilms>
        
        if (directorFilms != null && directorFilms.getLength() > 0) {
            for (int i = 0; i < directorFilms.getLength(); i++) {
            	// Declare variables for movie and genre
            	dirname_string = ""; 
            	fid_string = "";
            	title_string = ""; 
            	year_string = "";
            	genre_string = "";
            	         	
                // get a single node of <directorfilms>
                Element directorFilmsNode = (Element) directorFilms.item(i);				// <directorfilms>
                
                // Extract <dirname> from <director> tag
                NodeList directors = directorFilmsNode.getElementsByTagName("director");	// LIST OF <director>
                Element directorsNode = (Element) directors.item(0);						// single <director>
                NodeList dirnames = directorsNode.getElementsByTagName("dirname");			// LIST of <dirname>
                Element dirnameNode = (Element) dirnames.item(0);								// single <dirname>
                if (dirnameNode != null && dirnameNode.getFirstChild() != null) {
                	dirname_string = dirnameNode.getFirstChild().getNodeValue();				// value of <dirname>                   
                }
                
                // Extract <t>, <year>, and <cat> from <films>         
                NodeList films = directorFilmsNode.getElementsByTagName("films");			// LIST OF <films>
                Element filmsNode = (Element) films.item(0);								// single <films>
                NodeList film = filmsNode.getElementsByTagName("film");						// LIST OF <film>
                
                // Now loop through list of <film>
                for (int j = 0; j < film.getLength(); j++) {
                	genre_string = "";
              
                	Element filmNode = (Element) film.item(j);								// single <film>
                	 
                	// Extract <fid>
                	NodeList fid = filmNode.getElementsByTagName("fid");					// LIST OF <fid>
                	Element fidNode = (Element) fid.item(0);								// single <fid>
                	if (fidNode != null && fidNode.getFirstChild() != null) {
                		fid_string = fidNode.getFirstChild().getNodeValue();				// value of <fid>
                	}
                	
                	// Extract <t> title
                	NodeList title = filmNode.getElementsByTagName("t");					// LIST OF <t>
                	Element titleNode = (Element) title.item(0);							// single <t>
                	if (titleNode != null && titleNode.getFirstChild() != null) {
                        title_string = titleNode.getFirstChild().getNodeValue();			// value of <t>
                	 }
                	 
                	 // Extract <year>
                	 NodeList year = filmNode.getElementsByTagName("year");					// LIST OF <year>
                	 Element yearNode = (Element) year.item(0);								// single <year>
                	 if (yearNode != null && yearNode.getFirstChild() != null) {
                		 year_string = yearNode.getFirstChild().getNodeValue();				// value of <year>
                	 }
                	 
                	 // Extract <cat>
                	 NodeList cats = filmNode.getElementsByTagName("cats");					// LIST OF <cats>
                	 Element catsNode = (Element) cats.item(0);								// single <cats>
                	 if (cats.getLength() == 1) {
                         NodeList cat = catsNode.getElementsByTagName("cat");				// LIST OF <cat>
                         for (int k = 0; k < cat.getLength(); k++) {
                             Element catNode = (Element) cat.item(k);						// single <cat>
                             if (catNode != null && catNode.getFirstChild() != null) {
                            	 genre_string += catNode.getFirstChild().getNodeValue();
                            	 if (cat.getLength() - k != 1) {
                            		 genre_string += ",";
                            	 }
                             }
                         }
                	 }
                	 try {
                		 movieIDcounter += 1;
                    	 String m_id = "nm" + movieIDcounter;
                    	 Movie movie = new Movie(dirname_string, m_id, title_string, Integer.parseInt(year_string), genre_string);
                         //movie.showData();
                         moviesToAdd.put(fid_string, movie);
                	 }
                	 catch (NumberFormatException e) {
                		 System.out.println("Film record with " + fid_string + " does not have a properly formatted year. Record skipped.");
                	 }
                }
            }
        }
    }
    
    private void parseCastDocument() {
    	String f_string = "";	// Store <f>
    	String t_string = "";	// Store <t>
    	String a_string = "";	// Store <a>
    	
    	Element docEle = dom.getDocumentElement();
    	
    	NodeList dirfilms = docEle.getElementsByTagName("dirfilms");			// LIST OF <dirfilms>
    	if (dirfilms != null && dirfilms.getLength() > 0) {
            for (int i = 0; i < dirfilms.getLength(); i++) {
            	f_string = "";
            	t_string = "";
            	a_string = "";
            	
            	Element dirfilmsNode = (Element) dirfilms.item(i);				// single <dirfilms>
            	
            	NodeList filmc = dirfilmsNode.getElementsByTagName("filmc");	// LIST OF <filmc>
            	for (int j = 0; j < filmc.getLength(); j++) {
            		Element filmcNode = (Element) filmc.item(j);				// single <filmc>
            		
            		NodeList m = filmcNode.getElementsByTagName("m");			// LIST OF <m>
            		for (int k = 0; k < m.getLength(); k++) {
            			Element mNode = (Element) m.item(k);					// single <m>
            			
            			// Extract <f>
            			NodeList f = mNode.getElementsByTagName("f");			// LIST OF <f>
            			Element fNode = (Element) f.item(0);					// single <f>
            			if (fNode != null && fNode.getFirstChild() != null) {
                   		 	f_string = fNode.getFirstChild().getNodeValue();	// value of <f>
                   	 	}
            			
            			// Extract <t>
            			NodeList t = mNode.getElementsByTagName("t");			// LIST OF <t>
            			Element tNode = (Element) t.item(0);					// single <t>
            			if (tNode != null && tNode.getFirstChild() != null) {
            				t_string = tNode.getFirstChild().getNodeValue();	// value of <t>
            			}
            			
            			// Extract <a>
            			NodeList a = mNode.getElementsByTagName("a");			// LIST OF <a>
            			Element aNode = (Element) a.item(0);					// single <a>
            			if (aNode != null && aNode.getFirstChild() != null) {
            				a_string = aNode.getFirstChild().getNodeValue();	// value of <a>
            			
            			}
            			StarInMovie sim = new StarInMovie(f_string, t_string, a_string);
            			//sim.showData();
            			starInMovie.put(f_string, sim);
            		}
            	}
            }
    	}
    	
    }
    
    private void parseActorDocument() throws SQLException{
    	String maxIDquery = "SELECT max(id) from stars";
    	Statement getMaxIDstatement = dbcon.createStatement();
    	ResultSet rs = getMaxIDstatement.executeQuery(maxIDquery);
    	rs.next();
    	String latest_id = rs.getString("max(id)");
    	Integer new_id = Integer.parseInt(latest_id.substring(2));
    	starIDcounter = new_id;
    	
    	String stagename_string = "";
    	String dob_string = "";
    	
    	Element docEle = dom.getDocumentElement();
    	
    	NodeList actor = docEle.getElementsByTagName("actor");						// LIST OF <actor>
    	for (int i = 0; i < actor.getLength(); i++) {
    		stagename_string = "";
    		dob_string = "";
    		
    		Element actorNode = (Element) actor.item(i);							// single <actor>
    		
    		// Extract stagename
    		NodeList stagename = actorNode.getElementsByTagName("stagename");		// LIST OF <stagename>
    		Element stagenameNode = (Element) stagename.item(0);					// single <stagename>
    		if (stagenameNode != null && stagenameNode.getFirstChild() != null) {
        		stagename_string = stagenameNode.getFirstChild().getNodeValue();	// value of <stagename>
    		}
    			
    		// Extract dob
    		NodeList dob = actorNode.getElementsByTagName("dob");					// LIST OF <dob>
    		Element dobNode = (Element) dob.item(0);								// single <dob>
    		if (dobNode != null && dobNode.getFirstChild() != null) {
    			//dobNode.getFirstChild();
    			dob_string = dobNode.getFirstChild().getNodeValue();				// value of <dob>
    		}
    		try {
    			starIDcounter += 1;
        		String s_id = "nm" + starIDcounter;
        		if (dob_string.equals("")) {
        			System.out.println("hi");
        			dob_string = "0";
        		}
        		Actor newActor = new Actor(s_id, stagename_string, Integer.parseInt(dob_string));
        		actorsToAdd.put(stagename_string, newActor);
        		//newActor.showData();
    		}
    		catch (NumberFormatException e) {
       		 System.out.println("Record with " + stagename_string + "'s dob is not properly formatted. Record skipped.");
       	 	}
    			
    	}
    }

    private static void insertToDB() throws SQLException{
    	PreparedStatement insertMovieStatement = null;
    	String insertMovieQuery = null;
    	
    	PreparedStatement insertStarStatement = null;
    	String insertStarQuery = null;
    	
    	PreparedStatement insertGenreStatement = null;
    	String insertGenreQuery = null;
    	
    	PreparedStatement insertStarInMovieStatement = null;
    	String insertStarInMovieQuery = null;
    	
    	PreparedStatement insertGenreInMovieStatement = null;
    	String insertGenreInMovieQuery = null;
    	
    	insertMovieQuery = "INSERT INTO movies (id, title, year, director) VALUES (?, ?, ?, ?)";
    	insertStarQuery = "INSERT INTO stars (id, name, birthYear) VALUES (?, ?, ?)";
    	insertGenreQuery = "INSERT INTO genres (id, name) VALUES (?, ?)";
    	insertStarInMovieQuery = "INSERT INTO stars_in_movies (starId, movieId) VALUES (?, ?)";
    	insertGenreInMovieQuery = "INSERT INTO genres_in_movies (genreId, movieId) VALUES (?, ?)";
    	
    	dbcon.setAutoCommit(false);
    	
    	insertMovieStatement = dbcon.prepareStatement(insertMovieQuery);
    	insertStarStatement = dbcon.prepareStatement(insertStarQuery);
    	insertGenreStatement = dbcon.prepareStatement(insertGenreQuery);
    	insertStarInMovieStatement = dbcon.prepareStatement(insertStarInMovieQuery);
    	insertGenreInMovieStatement = dbcon.prepareStatement(insertGenreInMovieQuery);
    	
    	Iterator itMovies = moviesToAdd.entrySet().iterator();
    	Movie movieObj = null;
    	while (itMovies.hasNext()) {
    		Map.Entry<String, Movie> movieData = (Map.Entry<String, Movie>) itMovies.next();
    		movieObj = movieData.getValue();
    		
    		insertMovieStatement.setString(1, movieObj.getId());
    		insertMovieStatement.setString(2, movieObj.getTitle());
    		insertMovieStatement.setInt(3, movieObj.getYear());
    		insertMovieStatement.setString(4, movieObj.getDirname());
    		
    		for (String genre : movieObj.getGenres()) {
    			
    			String genreQuery = "SELECT id FROM genres WHERE name like '%" + genre + "%'";
    			Statement getGenreStatement = dbcon.createStatement();
    			ResultSet rs = getGenreStatement.executeQuery(genreQuery);
    			if(rs.next()) {
        			int genre_id = rs.getInt("id");
            		insertGenreInMovieStatement.setInt(1, genre_id);
    			}
    			else {
    				String idQuery = "SELECT max(id) FROM genres";
    				Statement getIDStatement = dbcon.createStatement();
    				ResultSet idrs = getIDStatement.executeQuery(idQuery);
    				idrs.next();
    				int max_id = idrs.getInt("max(id)");
    				max_id++;
    				
    				insertGenreStatement.setInt(1, max_id);
    				insertGenreStatement.setString(2, genre);
    				insertGenreStatement.addBatch();
    				insertGenreInMovieStatement.setInt(1, max_id);
    			}
    	    	insertGenreStatement.executeBatch();
        		insertGenreInMovieStatement.setString(2, movieObj.getId());
        		insertGenreInMovieStatement.addBatch();
    		}
    		insertMovieStatement.addBatch();
    	}
    	
    	Iterator itStars = actorsToAdd.entrySet().iterator();
    	Actor actorObj = null;
    	while (itStars.hasNext()) {
    		Map.Entry<String, Actor> starData = (Map.Entry<String, Actor>) itStars.next();
    		actorObj = starData.getValue();
    		
    		insertStarStatement.setString(1, actorObj.getID());
    		insertStarStatement.setString(2, actorObj.getName());
    		if (actorObj.getDOB() == 0) {
        		insertStarStatement.setString(3, null);

    		}
    		else {
        		insertStarStatement.setInt(3, actorObj.getDOB());
    		}
    		insertStarStatement.addBatch();
    	}
    	
    	Iterator itSIM = starInMovie.entrySet().iterator();
    	StarInMovie sim = null;
    	while (itSIM.hasNext()) {
    		Map.Entry<String, StarInMovie> simData = (Map.Entry<String, StarInMovie>) itSIM.next();
    		sim = simData.getValue();
    		
    		Movie mv = moviesToAdd.get(sim.getFID());
    		Actor ac = actorsToAdd.get(sim.getActor());
    		
    		if (ac != null && mv != null) {
    			String mv_id = mv.getId();
        		String ac_id = ac.getID();
        		insertStarInMovieStatement.setString(1, ac_id);
        		insertStarInMovieStatement.setString(2, mv_id);
        		insertStarInMovieStatement.addBatch();
    		}
    	}
    	
    	insertMovieStatement.executeBatch();
    	insertGenreInMovieStatement.executeBatch();
    	insertStarStatement.executeBatch();
    	insertStarInMovieStatement.executeBatch();
    	
    	dbcon.commit();
    }
    
    private static void connectToDB() throws Exception{
    	String usernameDB = "root";
        String passwordDB = "code123";
		String loginUrlDB = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";	// remote
		//String loginUrlDB = "jdbc:mysql://localhost:3306/moviedb";						// local

		try {
			// Create an instance of the driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
						
			// Create a connection to the database
			dbcon = DriverManager.getConnection(loginUrlDB, usernameDB, passwordDB);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		catch (java.lang.Exception ex) {
			System.out.println(ex.getMessage());
		}
    }

    public static void main(String[] args) throws Exception {
    	// Connect to database
    	connectToDB();
    	
        //create an instance
        DomParser dpe = new DomParser();

        //call run example
        dpe.runParser();
        
        // Insert new data
        insertToDB();
        System.out.println("Parsing completed. The above messages indicate which records were improperly"
        		+ " formatted, and therefore not inserted into the database");
    }

}
