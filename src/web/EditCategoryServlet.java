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

import database.MySqlHandler;
import store.Category;

/**
 * Servlet implementation class EditCategoryServlet
 */
@WebServlet("/EditCategory")
public class EditCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditCategoryServlet() {
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
                boolean result = db.editCategory(category.getCategoryName(), category.getContents(), category.getOldCategoryName());
                
                response.setContentType("text;characterset=UTF-8");
                PrintWriter out = response.getWriter();
                
                if(result == false) {
                    out.print("Failed to edit category.");
                }
                
                out.print("Successfully edited category.");
                
                out.flush();
            }
            else {
                response.getWriter().append("EditCategory: Warning: No parameters available!");        
            }
            
        } catch (SQLException e) {
            response.getWriter().append("EditCategory: SQL Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            response.getWriter().append("EditCategory: Error: " + e.getMessage());
        } catch (NullPointerException e) {
            response.getWriter().append("EditCategory: Null-Pointer Error: " + e.getMessage());
        }
	}

}
