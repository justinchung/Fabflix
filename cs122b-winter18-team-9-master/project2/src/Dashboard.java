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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.google.gson.JsonObject;
import com.mysql.cj.jdbc.DatabaseMetaData;
import com.google.gson.JsonArray;

/**
 * Servlet implementation class Dashboard
 */
@WebServlet("/Dashboard")
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Dashboard() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		Connection dbcon = null;
		
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
        	
        	// Get database metadata
        	java.sql.DatabaseMetaData md = dbcon.getMetaData();
        	
        	ResultSet rs = md.getTables("moviedb", null, "%", null);        	
        				
        	JsonArray jsonArray = new JsonArray();
        	
        	while (rs.next()) {
        		String table_name = rs.getString(3);
        		System.out.println(table_name);
        		
        		Statement statement = dbcon.createStatement();
        		String query = "SHOW CREATE TABLE moviedb." + table_name;
        		ResultSet columns_set = statement.executeQuery(query);
        		
        		JsonObject jsonObject = new JsonObject();
        		jsonObject.addProperty("table_name", table_name);
        		
        		while (columns_set.next()) {
        			String schema = columns_set.getString(2);
        			System.out.println(schema);
        			jsonObject.addProperty("schema", schema);
        		}
        		
        		jsonArray.add(jsonObject);
        		statement.close();
        	}
        	out.write(jsonArray.toString());
	        rs.close();
	        dbcon.close();
        }
        catch (java.lang.Exception ex) {
			System.out.println(ex.getMessage());
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
