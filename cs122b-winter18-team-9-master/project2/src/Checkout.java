

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class Checkout
 */
@WebServlet("/Checkout")
public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Checkout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		Connection dbcon = null;
		
		// Get current session
		//HttpSession session = request.getSession();
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    
	    // Database info
		String usernameDB = "root";
		String passwordDB = "code123";
		String loginUrlDB = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";	// remote
		//String loginUrlDB = "jdbc:mysql://localhost:3306/moviedb";						// local
		
		String queryMovieSearch = 
				"SELECT movies.id, title, year, director, group_concat(distinct genres.name) as genres, group_concat(distinct stars.name) as actors "
				+ "FROM movies JOIN stars_in_movies ON movies.id = stars_in_movies.movieId JOIN stars ON stars.id = stars_in_movies.starId "
				+ "JOIN genres_in_movies ON genres_in_movies.movieId = movies.id JOIN genres ON genres.id = genres_in_movies.genreId "
				+ "WHERE movies.id IN (";
	
		
		synchronized(session) {
			HashMap<String, Integer> cart = (HashMap<String, Integer>)session.getAttribute("cart");
			if (cart == null) {
				cart = new HashMap<String, Integer>();
				session.setAttribute("cart", cart);
			}
			int i = 0;
			for (Map.Entry<String, Integer> entry : cart.entrySet())
		    {
				if (i == cart.size() - 1) {
					queryMovieSearch += "'" + entry.getKey() + "'";
				}
				else {
			        queryMovieSearch += "'" + entry.getKey() + "',";
				}
		    }
			queryMovieSearch += ") GROUP BY movies.id";
		}
				
		try {
			synchronized(session) {
				// Since we are only reading from database, can randomly choose between master or slave
				// A better implementation would to keep count of how connections each server has and
				// pick the one with less
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
		
			
			// Declare a new statement
			Statement statement = dbcon.createStatement();
			
			// Execute query
			ResultSet rs = statement.executeQuery(queryMovieSearch);
			
			JsonArray jsonArray = new JsonArray();
			
			synchronized(session) {
				HashMap<String, Integer> cart = (HashMap<String, Integer>)session.getAttribute("cart");
				while (rs.next()) {
					String m_id = rs.getString("id");
					String m_title = rs.getString("title");
					//String m_year = rs.getString("year");
					//String m_director = rs.getString("director");
					//String m_genres = rs.getString("genres");
					//String m_actors = rs.getString("actors");
					
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("m_quantity", cart.get(m_id));
					//jsonObject.addProperty("m_id", m_id);
					jsonObject.addProperty("m_title", m_title);
					//jsonObject.addProperty("m_year", m_year);
					//jsonObject.addProperty("m_director", m_director);
					//jsonObject.addProperty("m_genres", m_genres);
					//jsonObject.addProperty("m_actors", m_actors);
					
					jsonArray.add(jsonObject);
				}
			}
			
			out.write(jsonArray.toString());
	        rs.close();
	        statement.close();
	        dbcon.close();
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
