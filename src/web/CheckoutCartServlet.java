package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import store.Store;

/**
 * Servlet implementation class CheckoutCartServlet
 */
@WebServlet("/CheckoutCart")
public class CheckoutCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckoutCartServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
        Store store = new Store();
        
        try {
    		HttpSession session = request.getSession();
    		Integer cartId = (Integer)session.getAttribute("cartId");
        	
            if(cartId == 0 || cartId == null) {
            	cartId = (int)store.getCart();
				session.setAttribute("cartId", cartId);
            }
            
            boolean result = store.checkOutCart(cartId, 599, "adress 54", "anna");
            
            response.setContentType("text;characterset=UTF-8");
            PrintWriter out = response.getWriter();
            
            if(result == true) {
            	session.setAttribute("cartId", 0);
                out.print("Successfully checked out the cart.");
            }
            else
                out.print("Failed to check out the cart.");
            
            out.flush();
           
            
        } catch (SQLException e) {
            response.getWriter().append("SQL Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            response.getWriter().append("CNF Error: " + e.getMessage());
        }
	}

}
