package web;

import java.io.IOException;
import java.io.InputStream;
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

import database.MySqlHandler;
import store.Category;

/**
 * Servlet implementation class AddCategoryServlet
 */
@WebServlet("/AddCategory")
public class AddCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCategoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
        MySqlHandler db = new MySqlHandler();
        
        try {
            // Get the added data.
            String data = request.getReader().lines().collect(Collectors.joining());
            Category category = new Gson().fromJson(data, Category.class); // Retrieve the item from the data.
            
            if(category != null) {
                boolean result = db.addToCategory(category.getCategoryName(), category.getContents());
                
                response.setContentType("text;characterset=UTF-8");
                PrintWriter out = response.getWriter();
                
                if(result == false) {
                    out.print("Failed to add a new category.");
                }
                
                out.print("Successfully added a new category.");
                
                out.flush();
            }
            else {
                response.getWriter().append("AddCategory: Warning: No parameters available!");        
            }
            
        } catch (SQLException e) {
            response.getWriter().append("AddCategory: SQL Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            response.getWriter().append("AddCategory: Error: " + e.getMessage());
        } catch (NullPointerException e) {
            response.getWriter().append("AddCategory: Null-Pointer Error: " + e.getMessage());
        }
	}

}
