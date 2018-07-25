import java.io.IOException;
import java.io.*;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
		// Verify CAPTCHA.
		boolean valid = VerifyUtils.verify(gRecaptchaResponse);
		
		/*
		// JDBC, connecting to our database
		String usernameDB = "root";
		String passwordDB = "code123";
		String loginUrlDB = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";	// remote
		//String loginUrlDB = "jdbc:mysql://localhost:3306/moviedb";						// local
		*/
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		try {
			// the following few lines are for connection pooling
			Context initCtx = new InitialContext();
	        Context envCtx = (Context) initCtx.lookup("java:comp/env");
	        DataSource dsRead = (DataSource) envCtx.lookup("jdbc/moviedbread");
	        DataSource dsWrite = (DataSource) envCtx.lookup("jdbc/moviedbwrite");
	  
	        Connection dbcon = dsWrite.getConnection();
	        
	        session.setAttribute("dsread", dsRead);
	        session.setAttribute("dswrite", dsWrite);
	        
	        /*
			// Create an instance of the driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			// Create a connection to the database
			Connection dbcon = DriverManager.getConnection(loginUrlDB, usernameDB, passwordDB);
			*/
			// Declare a new statement
			Statement statement = dbcon.createStatement();
			
			// Create query to search for valid username and password
			String queryLogin = "SELECT * FROM customers WHERE email = \"" + username + "\" and password = \"" + password + "\"";
			
			// Execute query and store into rs
			ResultSet rs = statement.executeQuery(queryLogin);
			
			// Store the results of the query
			if (rs.next()) {
				String rs_username = rs.getString("email");
				String rs_password = rs.getString("password");
				
				if (username.equals(rs_username) && password.equals(rs_password)) {
					// login success
					// set this user into the session
					request.getSession().setAttribute("user", new User(username));
					
					JsonObject responseJsonObject = new JsonObject();
					responseJsonObject.addProperty("status", "success");
					responseJsonObject.addProperty("message", "success");
					
					response.getWriter().write(responseJsonObject.toString());
				}
			}
			else {
				// login fail
				request.getSession().setAttribute("user", new User(username));
				
				JsonObject responseJsonObject = new JsonObject();
				responseJsonObject.addProperty("status", "fail");
				responseJsonObject.addProperty("message", "incorrect username/password");
				
				response.getWriter().write(responseJsonObject.toString());
			}
			rs.close();
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
            out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
                    + "<P>SQL error in doGet: " + ex.getMessage() + "</P></BODY></HTML>");
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