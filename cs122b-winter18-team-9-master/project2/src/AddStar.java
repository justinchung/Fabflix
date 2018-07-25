

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
 * Servlet implementation class AddStar
 */
@WebServlet("/AddStar")
public class AddStar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddStar() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		Connection dbcon = null;
				
		String name = request.getParameter("name");
		String birthyear = request.getParameter("birthyear");
						
		try {
			synchronized(session) {
				dbcon = ((DataSource) session.getAttribute("dswrite")).getConnection();
				System.out.println("Writing to master");
        	}
	    	
	    	// Get latest star_id
	    	Statement statement = dbcon.createStatement();
	    	ResultSet rs = statement.executeQuery("SELECT * FROM latest_star_id");
	    	
	    	rs.next();
	    	String last_starid = rs.getString("last_starid");
	    	String last_id = last_starid.substring(2);
	    	int id = Integer.parseInt(last_id);
	    	id += 1;
	    		
			String query = "INSERT INTO stars VALUES ('nm" + id + "', '" + name + "', '" + birthyear + "')";

		        				
	    	// Insert new star
	    	PreparedStatement preparedStatement1 = dbcon.prepareStatement(query);
	    	preparedStatement1.execute();
	    	
	    	// Update latest_star_id
	    	PreparedStatement preparedStatement2 = dbcon.prepareStatement("TRUNCATE TABLE latest_star_id");
	    	PreparedStatement preparedStatement3 = dbcon.prepareStatement("INSERT INTO latest_star_id VALUES ('nm" + id + "'");      
	    	preparedStatement2.execute();
	    	preparedStatement3.execute();
		        
	  
	    	preparedStatement1.close();
	    	dbcon.close();
	    }
		catch (java.lang.Exception ex) {
			System.out.println(ex.getMessage());
			return;
		}
	}

}
