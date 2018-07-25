

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class Autocomplete
 */
@WebServlet("/Autocomplete")
public class Autocomplete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Autocomplete() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Since we are using connection pooling, we need to access the data source that we previously stored
		HttpSession session = request.getSession(true);
		Connection dbcon = null;
		
		// JDBC, connecting to our database
		String usernameDB = "root";
		String passwordDB = "code123";
		String loginUrlDB = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";	// remote
		//String loginUrlDB = "jdbc:mysql://localhost:3306/moviedb";						// local
		
		String query = request.getParameter("query").toLowerCase();
		String[] words = query.split(" ");
		
		// Create the full text query for searching on movie titles and star names
		//ALTER TABLE table ADD FULLTEXT index_name(column1);

		//String queryMovie = "SELECT * FROM movies WHERE MATCH (title) AGAINST ('";
		//String queryStar = "SELECT * FROM stars WHERE MATCH (name) AGAINST ('";
		String queryMovie = "SELECT * FROM movies WHERE MATCH (title) AGAINST (";
		String queryStar = "SELECT * FROM stars WHERE MATCH (name) AGAINST (";

		for (int i = 0; i < words.length; i++) {
			//queryMovie += "+" + words[i] + "*";
			//queryStar += "+" + words[i] + "*";
			queryMovie += "? ";
			queryStar += "? ";
		}
		//queryMovie += "' IN BOOLEAN MODE) OR edth(lower(title), '" + query + "', 3)";
		//queryStar += "' IN BOOLEAN MODE) OR edth(lower(name), '" + query + "', 3)";
		queryMovie += "IN BOOLEAN MODE) OR edth(lower(title), ?, 3)";
		queryStar += "IN BOOLEAN MODE) OR edth(lower(name), ?, 3)";
		
		System.out.println(queryMovie);
		System.out.println(queryStar);
		
		// Output stream to STDOUT
        response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();
		try {
			System.out.println("hi");
			JsonArray jsonArray = new JsonArray();
			// return the empty json array if query is null or empty
			if (query == null || query.trim().isEmpty()) {
				response.getWriter().write(jsonArray.toString());
				return;
			}
			synchronized(session) {
				// Since we are only reading, randomly connect to slave or master instance for read
				int pick = (int) (Math.random() % 2);
				if (pick == 0) {
					dbcon = ((DataSource) session.getAttribute("dsread")).getConnection();
					System.out.println("Reading from slave");
				}
				else {
					dbcon = ((DataSource) session.getAttribute("dswrite")).getConnection();
					System.out.println("Reading from master");
				}
			}
			/*
			// Create an instance of the driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
						
			// Create a connection to the database
			Connection dbcon = DriverManager.getConnection(loginUrlDB, usernameDB, passwordDB);
			*/
			// Declare a new statement
			Statement statement = dbcon.createStatement();
			PreparedStatement statementMovie = dbcon.prepareStatement(queryMovie);
			PreparedStatement statementStar = dbcon.prepareStatement(queryStar);
			
			int paramIndexMovie = 1;
			int paramIndexStar = 1;
			
			for (String word : words) {
				statementMovie.setString(paramIndexMovie, "+" + word + "*");
				paramIndexMovie++;
				statementStar.setString(paramIndexStar, "+" + word + "*");
				paramIndexStar++;
			}
			statementMovie.setString(paramIndexMovie, query);
			statementStar.setString(paramIndexStar, query);
		
			
			// Execute queries on movie title and actor name
			//ResultSet rsMovie = statement.executeQuery(queryMovie);
			ResultSet rsMovie = statementMovie.executeQuery();
			
			int totalCount = 0, movieCount = 0, starCount = 0;
			
			while (rsMovie.next()) {
				String title = rsMovie.getString("title");
				
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("value", title);
				
				JsonObject additionalJsonObject = new JsonObject();
				additionalJsonObject.addProperty("category", "Movies");
				
				jsonObject.add("data", additionalJsonObject);
				
				jsonArray.add(jsonObject);
				
				totalCount++;
				movieCount++;
				
				if (totalCount == 10) {
					break;
				}
			}
	        rsMovie.close();
			//ResultSet rsStars = statement.executeQuery(queryStar);
			ResultSet rsStars = statementStar.executeQuery();


			while (rsStars.next()) {
				if (totalCount == 10) {
					break;
				}
				
				String name = rsStars.getString("name");
				
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("value", name);
				
				JsonObject additionalJsonObject = new JsonObject();
				additionalJsonObject.addProperty("category", "Stars");
				
				jsonObject.add("data", additionalJsonObject);
				
				jsonArray.add(jsonObject);
				
				totalCount++;
			}
			out.write(jsonArray.toString());
	        rsStars.close();
	        statement.close();
	        dbcon.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
            while (ex != null) {
                System.out.println("SQL Exception:  " + ex.getMessage());
                ex = ex.getNextException();
            }
		}
		catch (java.lang.Exception ex) {
			System.out.println(ex.getMessage());
			return;
		}
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
