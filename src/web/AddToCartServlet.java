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
@WebServlet(name = "AddToCart", urlPatterns = { "/AddToCart" })
public class AddToCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddToCartServlet() {
        super();
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
			HttpSession session = request.getSession();
			int cartId = (Integer)session.getAttribute("cartId");

			if(item != null && cartId != 0) {
				boolean result = store.addToCart(item.getArtNr(), cartId, item.getStockBalance());
				
				response.setContentType("application/json;characterset=UTF-8");
				PrintWriter out = response.getWriter();
				
				if(result)
					out.print("Successfully added a new product.");
				else
					out.print("Failed to add a new product.");
				
				out.flush();
			}
			else {
				request.getSession().setAttribute("cartId", 0);
				response.getWriter().append("Warning: No parameters available!");		
			}
			
		} catch (SQLException e) {
			request.getSession().setAttribute("cartId", 0);
			response.getWriter().append("Error: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			response.getWriter().append("Error: " + e.getMessage());
		} catch (NullPointerException e) {
			request.getSession().setAttribute("cartId", 0);
			response.getWriter().append("Null-Pointer Error: " + e.getMessage());
		}
	}

}
