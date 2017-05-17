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

import store.Authentication;
import store.Store;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(name = "Login", urlPatterns = { "/Login" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Store store = new Store();
		
		// Get the added data.
		String data = request.getReader().lines().collect(Collectors.joining());
		Authentication info = new Gson().fromJson(data, Authentication.class); // Retrieve the item from the data.
		
		Authentication user = null;
		
		try {
			user = store.getUser(info.getUsername());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		boolean confirmed = false;
		
		if(user != null) {
			
			if(user.getPassword().equalsIgnoreCase(info.getPassword())) {
				HttpSession session = request.getSession();
				session.setAttribute("username", user.getUsername());
				session.setAttribute("password", user.getPassword());
				
				response.setContentType("text;characterset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("Confirmed!");
				out.flush();
				
				confirmed = true;
			}
			
		}
		
		if(!confirmed) {
			response.setContentType("text;characterset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("Access Denied!");
			out.flush();
		}
	}

}
