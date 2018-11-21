package server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classes.JDBCDriver;

@WebServlet("/LogInServlet")
public class LogInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LogInServlet() {
		super();
	}

	// Log-in the user into the game and server
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		System.out.println(username + " " + password);

		/*
		 * Message used to communicate with client about log-in
		 * 
		 * Returns the alert message
		 */

		String message = "";

		synchronized (this) {

			JDBCDriver database = new JDBCDriver();

			if (!database.userExists(username)) {
				message = "This user does not exist";
			} else if (!database.checkCredentials(username, password)) {
				message = "That is the incorrect password";
			} else if (database.isSignedIn(username)) {
				message = "The user is already logged in";
			} else {
				database.setSignedIn(username, true);
				message = "User Signed In";
			}

			database.close();

			// Sends message back to jQuery call
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(message);
		}
	}

}
