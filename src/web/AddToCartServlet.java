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
			
			if(item != null) {
				boolean result = store.addToCart(item.getArtNr(), item.getCartId(), item.getStockBalance());
				
				response.setContentType("application/json;characterset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print(result);
				out.flush();
			}
			else {
				response.getWriter().append("Warning: No parameters available!");
			}
			
		} catch (SQLException e) {
			response.getWriter().append("Error: " + e.getMessage());
		}
	}

}
