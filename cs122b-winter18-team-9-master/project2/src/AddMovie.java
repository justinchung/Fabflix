

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.google.gson.JsonArray;

/**
 * Servlet implementation class AddMovie
 */
@WebServlet("/AddMovie")
public class AddMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMovie() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		Connection dbcon = null;
		
		String title = request.getParameter("title");
		String year = request.getParameter("year");
		String director = request.getParameter("director");
		String genre = request.getParameter("genre");
		String star = request.getParameter("star");
		
		String query = "CALL moviedb.add_movie('tt9999999', '" + title + "', " + year + ", '" 
				+ director + "', '" + star + "', '" + genre + "')";
		
		// Output stream to STDOUT
        response.setContentType("application/json"); // Response mime type
        PrintWriter out = response.getWriter();
        
        try {
        	synchronized(session) {
				dbcon = ((DataSource) session.getAttribute("dswrite")).getConnection();
				System.out.println("Writing to master");
        	}
			
			// Declare a new statement
			Statement statement = dbcon.createStatement();
			
			// Execute query
			ResultSet rs = statement.executeQuery(query);
			
			dbcon.close();
        }
        catch (java.lang.Exception ex) {
			System.out.println(ex.getMessage());
			return;
		}
        
	}

}
