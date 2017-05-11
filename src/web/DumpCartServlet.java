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

import com.google.gson.Gson;

import store.Item;
import store.Store;

/**
 * Servlet implementation class DumpCartServlet
 */
@WebServlet("/DumpCart")
public class DumpCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DumpCartServlet() {
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
            System.out.println(data);
            if(item != null && item.getCartId() >= 0) {
                boolean result = store.dumpCart(item.getCartId());
                
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
        } catch (ClassNotFoundException e) {
            response.getWriter().append("Error: " + e.getMessage());
        }
	}

}
