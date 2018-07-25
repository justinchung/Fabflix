import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SinlgeStar
 */
@WebServlet("/SingleStar")
public class SingleStar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleStar() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		Connection dbcon = null;
				
		String starName = request.getParameter("name");
		//String starDOB = request.getParameter("dob");
		//String starMovies = request.getParameter("movies");
		
		String queryStarSearch = "SELECT name, birthYear, group_concat(movies.title) AS movieTitles"
				+ " FROM stars JOIN stars_in_movies ON stars.id = stars_in_movies.starId"
				+ " JOIN movies on movies.id = stars_in_movies.movieId";
				
		if (!starName.equals("")) {
			queryStarSearch += " WHERE name = '" + starName + "'";
		}
		queryStarSearch += " GROUP BY stars.id";
		System.out.println(queryStarSearch);
		
		// Output stream to STDOUT
		response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();
		
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
			ResultSet rs = statement.executeQuery(queryStarSearch);
			
			JsonArray jsonArray = new JsonArray();
			
			while (rs.next()) {
				String s_name = rs.getString("name");
				String s_dob = rs.getString("birthYear");
				String s_movies = rs.getString("movieTitles");
				
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("s_name", s_name);
				jsonObject.addProperty("s_dob", s_dob);
				jsonObject.addProperty("s_movies", s_movies);
				
				jsonArray.add(jsonObject);
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
