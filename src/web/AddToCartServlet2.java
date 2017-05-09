package web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.MySqlHandler;

/**
 * Servlet implementation class AddToCartServlet2
 */
@WebServlet("/AddToCart2")
public class AddToCartServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddToCartServlet2() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    int cartId = Integer.parseInt(request.getParameter("cartId"));
	    String artNr = request.getParameter("artNr");
	    int count = Integer.parseInt(request.getParameter("count"));
	    
	    MySqlHandler db = new MySqlHandler();
	    
	    try {
            boolean result = db.addToCart(cartId, artNr, count);
            System.out.println(result);
        } catch (ClassNotFoundException e) {
            response.getWriter().append("Error: " + e.getMessage());
        } catch (SQLException e) {
            response.getWriter().append("Error: " + e.getMessage());
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
