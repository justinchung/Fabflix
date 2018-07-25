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
import java.lang.Math;
import java.lang.System;
import java.util.*;
import java.io.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

/**
 * Servlet implementation class MovieSearch
 */

@WebServlet("/MovieSearch")
public class MovieSearch extends HttpServlet {
	long startServletTime;
	long elapsedJDBCTime;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		startServletTime = System.nanoTime();
		// Since we are using connection pooling, need to fetch the session that we stored
		// our data source in
		HttpSession session = request.getSession(true);
		
			
		String movieTitle = request.getParameter("title").toLowerCase();
		String movieYear = request.getParameter("year").toLowerCase();
		String movieDir = request.getParameter("director").toLowerCase();
		String movieActor = request.getParameter("actor").toLowerCase();
		String movieGenre = request.getParameter("genre").toLowerCase();
		String type = request.getParameter("type").toLowerCase();
		
		System.out.println(movieTitle + " " + type);
		
		String queryMovieSearch = 
				"SELECT movies.id, title, year, director, group_concat(distinct genres.name) as genres, group_concat(distinct stars.name) as actors "
				+ "FROM movies JOIN stars_in_movies ON movies.id = stars_in_movies.movieId JOIN stars ON stars.id = stars_in_movies.starId "
				+ "JOIN genres_in_movies ON genres_in_movies.movieId = movies.id JOIN genres ON genres.id = genres_in_movies.genreId";
		
		boolean first = true;
		
		
		if (!movieTitle.equals("")) {
			if (type.equals("search")) {
				//queryMovieSearch += " WHERE title LIKE '%" + movieTitle + "%'";
				queryMovieSearch += " WHERE title LIKE ?";
			}
			else if (type.equals("browse")) {
				if (movieTitle.equals("1")) {
					//queryMovieSearch += " WHERE title regexp '^[0-9]+' ";
					queryMovieSearch += " WHERE title regexp '^[0-9]+'";
				}
				else {
					//queryMovieSearch += " WHERE title LIKE '" + movieTitle + "%'";
					queryMovieSearch += " WHERE title LIKE ?";
				}
			}
			else if (type.equals("searchbar")) {
				/*
				queryMovieSearch += " WHERE MATCH (title) AGAINST ('";
				String[] titleWords = movieTitle.split(" ");
				for (int i = 0; i < titleWords.length; i++) {
					queryMovieSearch += "+" + titleWords[i] + "*";
				}
				queryMovieSearch += "' IN BOOLEAN MODE) OR edth(lower(title), '" + movieTitle + "', 3)";
				*/
				queryMovieSearch += " WHERE MATCH (title) AGAINST (";
				String[] titleWords = movieTitle.split(" ");
				for (int i = 0; i < titleWords.length; i++) {
					queryMovieSearch += "? ";
				}
				queryMovieSearch += "IN BOOLEAN MODE) OR edth(lower(title), ?, 3)";
			}
			first = false;
		}
		if (!movieYear.equals("")) {
			if (!first) { queryMovieSearch += " AND"; }
			else { queryMovieSearch += " WHERE"; }
			
			//queryMovieSearch += String.format(" year = '%s'", movieYear);
			queryMovieSearch += " year = ?";

			first = false;
		}
		if (!movieDir.equals("")) {
			if (!first) { queryMovieSearch += " AND"; }
			else { queryMovieSearch += " WHERE"; }
			
			//queryMovieSearch += " director LIKE '%" + movieDir + "%'";
			queryMovieSearch += " director LIKE ?";

			first = false;
		}
		
		queryMovieSearch += " GROUP BY movies.id";
		
		if (!movieActor.equals("")) {
			//queryMovieSearch += " HAVING actors LIKE '%" + movieActor + "%'";
			queryMovieSearch += " HAVING actors LIKE ?";
		}
		if (!movieActor.equals("") && !movieGenre.equals("")) {
			//queryMovieSearch += " AND genres LIKE '%" + movieGenre + "%'";
			queryMovieSearch += " AND genres LIKE ?";
		}
		else if (movieActor.equals("") && !movieGenre.equals("")) {
			//queryMovieSearch += " HAVING genres LIKE '%" + movieGenre + "%'";
			queryMovieSearch += " HAVING genres LIKE ?";
		}
		
		// Output stream to STDOUT
        response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();
        
        long startJDBCTime = System.nanoTime();
		Connection dbcon = null;
		try {
			synchronized(session) {
				// Since we are only reading from database, can randomly choose between master or slave
				// A better implementation would to keep count of how connections each server has and
				// pick the one with less
				int pick = (int) (Math.random() % 2);
				if (pick == 0) {
					dbcon = ((DataSource) session.getAttribute("dsread")).getConnection();
				}
				else {
					dbcon = ((DataSource) session.getAttribute("dswrite")).getConnection();
				}
			}
			;
			// Declare a new statement
			//Statement statement = dbcon.createStatement();
			int paramIndex = 1;
			PreparedStatement statement = dbcon.prepareStatement(queryMovieSearch);
			if (!movieTitle.equals("")) {
				if (type.equals("browse")) {
					if (movieTitle.equals("1")) {
						;
					}
					else {
						statement.setString(paramIndex, movieTitle + "%");
					}
				}	
				else if (type.equals("searchbar")) {
					String[] titleWords = movieTitle.split(" ");
					for (String word : titleWords) {
						statement.setString(paramIndex, "+" + word + "*");
						paramIndex++;
					}
					statement.setString(paramIndex, movieTitle);
				}
				else if (type.equals("search")){
					statement.setString(paramIndex, movieTitle);
					paramIndex++;
				}
			}
			if (!movieYear.equals("")) {
				statement.setString(paramIndex, movieYear);
				paramIndex++;
			}
			if (!movieDir.equals("")) {
				statement.setString(paramIndex, "%" + movieDir + "%");
				paramIndex++;
			}
			if (!movieActor.equals("")) {
				statement.setString(paramIndex, "%" + movieActor + "%");
				paramIndex++;
			}
			if (!movieActor.equals("") && !movieGenre.equals("")) {
				statement.setString(paramIndex, "%" + movieGenre + "%");
				paramIndex++;
			}
			else if (movieActor.equals("") && !movieGenre.equals("")) {
				statement.setString(paramIndex, "%" + movieGenre + "%");
				paramIndex++;
			}
			
			// Execute query
			//ResultSet rs = statement.executeQuery(queryMovieSearch);
			ResultSet rs = statement.executeQuery();
			
			JsonArray jsonArray = new JsonArray();
			
			while (rs.next()) {
				String m_id = rs.getString("id");
				String m_title = rs.getString("title");
				String m_year = rs.getString("year");
				String m_director = rs.getString("director");
				String m_genres = rs.getString("genres");
				String m_actors = rs.getString("actors");
				
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("m_id", m_id);
				jsonObject.addProperty("m_title", m_title);
				jsonObject.addProperty("m_year", m_year);
				jsonObject.addProperty("m_director", m_director);
				jsonObject.addProperty("m_genres", m_genres);
				jsonObject.addProperty("m_actors", m_actors);
				
				jsonArray.add(jsonObject);
			}
			
			out.write(jsonArray.toString());
	        rs.close();
	        statement.close();
	        dbcon.close();
	        long endJDBCTime = System.nanoTime();
            elapsedJDBCTime += (endJDBCTime - startJDBCTime);
	        	    	
	    	String contextPath = getServletContext().getRealPath("/");
	    	String xmlFilePath=contextPath+"log.txt";
	    	/*
	    	File myfile = new File(xmlFilePath);
	    	myfile.createNewFile();
	    	
	    	String path_to_file = myfile.getAbsolutePath();*/
	    	System.out.println("path to file: " + xmlFilePath);
	    	
	    	FileWriter filewriter = new FileWriter(xmlFilePath, true);
	    	
	    	BufferedWriter writer = new BufferedWriter(filewriter);
	    	long elapsedServletTime = System.nanoTime() - startServletTime;

	    	String ts = "TS" + elapsedServletTime + " ";
	    	String tj = "TJ" + elapsedJDBCTime + "\n";
	    	writer.append(ts);
	    	writer.append(tj);
	    	
	    	writer.close();
	    	
	    	elapsedServletTime = 0;
	    	elapsedJDBCTime = 0;
	    	
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
