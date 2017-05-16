package web;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import database.MySqlHandler;

/**
 * Servlet implementation class AddToInventoryServlet
 */
@WebServlet("/AddToInventory")
@MultipartConfig
public class AddToInventoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddToInventoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String category = request.getParameter("category");
	    String itemName = request.getParameter("itemName");
	    String artNr = request.getParameter("artNr");
	    float price = Float.parseFloat(request.getParameter("price"));
	    String description = request.getParameter("description");
	    Part imagePart = request.getPart("image");
	    InputStream image = imagePart.getInputStream();
	    int stockBalance = Integer.parseInt(request.getParameter("stockBalance"));
	    String storageFormat = request.getParameter("storageFormat");
	    MySqlHandler db = new MySqlHandler();
	    
	    try {
            db.insertItemToInventory(category,
                    itemName,
                    artNr,
                    price,
                    description,
                    image,
                    stockBalance,
                    storageFormat);
            response.sendRedirect("index.html#!/admin");
        } catch (ClassNotFoundException e) {
            
        } catch (SQLException e) {
            
        }
	    
	}

}
