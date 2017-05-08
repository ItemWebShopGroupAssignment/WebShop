package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

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
@WebServlet(name = "GetCartItems", urlPatterns = { "/GetCartItems" })
public class GetCartItemsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCartItemsServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Store store = new Store();
		
		try {
			int cartId = Integer.parseInt(request.getParameter("cartId"));
			List<Item> inventory = store.getCartItems(cartId);
			
			response.setContentType("application/json;characterset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(new Gson().toJson(inventory));
			out.flush();
			
		} catch (SQLException | NumberFormatException e) {
			response.getWriter().append("Error: " + e.getMessage());
		}
	
	}

}
