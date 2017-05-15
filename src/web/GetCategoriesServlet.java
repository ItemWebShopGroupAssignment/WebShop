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

import store.Category;
import store.Store;

/**
 * Servlet implementation class GetCategoriesServlet
 */
@WebServlet("/GetCategories")
public class GetCategoriesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCategoriesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
        Store store = new Store();
        
        try {
            List<Category> categories = store.showCategories();
            
            response.setContentType("application/json;characterset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(categories));
            out.flush();
            
        } catch (SQLException e) {
            response.getWriter().append("Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
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
