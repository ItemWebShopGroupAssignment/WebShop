package web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import store.Store;

/**
 * Servlet implementation class GetCartServlet
 */
@WebServlet("/GetCart")
public class GetCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Store store = new Store();
		
		HttpSession session = request.getSession();
		long cartId = (long)session.getAttribute("cartId");
		
		try {
			if(cartId == 0) {
				cartId = store.getCart();
				session.setAttribute("cartId", cartId);
			}
			
			System.out.println(cartId);
			response.getWriter().append(Long.toString(cartId));
			
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().append("Error: " + e.getMessage());
		}
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
