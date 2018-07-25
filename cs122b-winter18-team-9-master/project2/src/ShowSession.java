import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.util.*;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ShowSession
 */
@WebServlet("/ShowSession")
public class ShowSession extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowSession() {
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
		response.setContentType("text/html");
	    HttpSession session = request.getSession(true);
	    
	    String movieId = request.getParameter("movieId");
	   
	    //String[] cart = (String[])session.getAttribute("cart");
	    
	    HashMap<String, Integer> cart = (HashMap<String, Integer>)session.getAttribute("cart");
	    if (cart == null) {
	    	cart = new HashMap<String, Integer>();
	    	cart.put(movieId, 1);
	    	session.setAttribute("cart", cart);
	    }
	    else {
	    	if (cart.containsKey(movieId)) {
	    		Integer quantity = cart.get(movieId);
	    		cart.replace(movieId, quantity+1);
	    	}
	    	else {
	    		cart.put(movieId, 1);
	    	}
	    	session.setAttribute("cart", cart);
	    }
	    
	    for (Map.Entry<String, Integer> entry : ((HashMap<String, Integer>)session.getAttribute("cart")).entrySet())
	    {
	        System.out.println(entry.getKey() + "/" + entry.getValue());
	    }
	}
}
