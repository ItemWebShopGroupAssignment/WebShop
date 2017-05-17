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
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import store.Item;
import store.Store;

/**
 * Servlet implementation class LoadUserProfileServlet
 */
@WebServlet(name = "LoadUserProfile", urlPatterns = { "/LoadUserProfile" })
public class LoadUserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadUserProfileServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("username");
		
		response.setContentType("text;characterset=UTF-8");
		PrintWriter out = response.getWriter();

		if(username != null)
			out.print(username);
		else
			out.print("");
		
		out.flush();
	}

}
