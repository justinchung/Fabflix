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

/**
 * Servlet implementation class MoveList
 */
@WebServlet("/MovieList")
public class MovieList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public String getServletInfo() {
		return "Servlet connects to MySQL database and displays result of a SELECT*";
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieList() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginUser = "root";
        String loginPasswd = "code123";
        String loginUrl = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";
        //String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        
        response.setContentType("text/html"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Fabflix</TITLE></HEAD>");
        out.println("<BODY><H1 align = \"center\">Top 20 Movies</H1>");

        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
            Statement statement = dbcon.createStatement();
            
            String query = "select title, year, director, group_concat(stars.name) as actors, group_concat(distinct genres.name) as genres, rating from movies join ratings on movies.id=ratings.movieId join stars_in_movies on movies.id = stars_in_movies.movieId join stars on stars_in_movies.starId = stars.id join genres_in_movies on genres_in_movies.movieId = movies.id join genres on genres.id = genres_in_movies.genreId group by title, year, director, rating order by ratings.rating desc limit 20";
            //String query = "SELECT * from stars";
            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            
            out.println("<TABLE border>");
            
            out.println("<tr><th>Title</th><th>Year</th><th>Directed by</th><th>Starring</th><th>Genre(s)</th><th>Rating</th></tr>");
            
            // Iterate through each row of rs
            
            while (rs.next()) {
                String m_title = rs.getString("title");
                String m_year = rs.getString("year");
                String m_director = rs.getString("director");
                String actors = rs.getString("actors");
                String genres = rs.getString("genres");
                String rating = rs.getString("rating");
                
                out.println("<tr>" + "<td>" + m_title + "</td>" + "<td>" + m_year + "</td>" + "<td>" + m_director + "</td>" + "<td>" + actors + "</td>" + "<td>" + genres + "</td>" + "<td>" + rating + "</td>"
                        + "</tr>");
     
            }           
            
            out.println("</TABLE>");
            
            rs.close();
            statement.close();
            dbcon.close();
        } /**catch (SQLException ex) {
            while (ex != null) {
                System.out.println("SQL Exception:  " + ex.getMessage());
                ex = ex.getNextException();
            } // end while
        } // end catch SQLException
        **/
        catch (java.lang.Exception ex) {
            out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
                    + "<P>SQL error in doGet: " + ex.getMessage() + "</P></BODY></HTML>");
            return; 
        }
        out.close();

        
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}