package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import store.Item;
import store.Store;

/**
 * @author Gustaf Peter Hultgren
 */
@WebServlet(name = "RemoveFromCart", urlPatterns = { "/RemoveFromCart" })
public class RemoveFromCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveFromCartServlet() {
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
    		int cartId = (Integer)session.getAttribute("cartId");
        		
            if(cartId == 0) {
            	cartId = (int)store.getCart();
				session.setAttribute("cartId", cartId);
            }
            
			// Get the added data.
			String data = request.getReader().lines().collect(Collectors.joining());
			Item item = new Gson().fromJson(data, Item.class); // Retrieve the item from the data.
			
			if(item != null && item.getCartId() >= 0) {
				boolean result = store.returnFromCart(item.getArtNr(), cartId, item.getStockBalance());
				
				response.setContentType("application/json;characterset=UTF-8");
				PrintWriter out = response.getWriter();
				
				if(result)
					out.print("Successfully threw away the product.");
				else
					out.print("Something went wrong, you're stuck with this product!");
					
				out.flush();
			}
			else {
				response.getWriter().append("Warning: No parameters available!");
			}
			
		} catch (SQLException e) {
			response.getWriter().append("SQL Error: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			response.getWriter().append("Error: " + e.getMessage());
		}
	}

}
