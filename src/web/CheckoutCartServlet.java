package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import store.Item;
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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
        Store store = new Store();
        
        try {
            // Get the added data.
            String data = request.getReader().lines().collect(Collectors.joining());
            Item item = new Gson().fromJson(data, Item.class); // Retrieve the item from the data.
            
            if(item != null && item.getCartId() >= 0) {
                List<String> result = store.checkOutCart(item.getCartId());
                
                response.setContentType("application/json;characterset=UTF-8");
                PrintWriter out = response.getWriter();
                
                if(result.size() > 0)
                    out.print("Successfully checked out the cart.");
                else
                    out.print("Failed to check out the cart.");
                
                out.flush();
            }
            else {
            	request.getSession().setAttribute("cartId", 0);
                response.getWriter().append("Warning: No parameters available!");
            }
            
        } catch (SQLException e) {
            response.getWriter().append("SQL Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            response.getWriter().append("CNF Error: " + e.getMessage());
        }
	}

}
