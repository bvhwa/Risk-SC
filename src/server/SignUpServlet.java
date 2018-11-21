package server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String defaultImage = "https://www.google.com/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwiD1OuxvtLdAhWNB3wKHd7NBDYQjRx6BAgBEAU&url=https%3A%2F%2Fcidco-smartcity.niua.org%2Fno-image-found%2F&psig=AOvVaw32m7njsNn-ln0B9xy6xvru&ust=1537838875192818";

	public SignUpServlet() {
		super();
	}

	// Service block to sign up the user based off the form constructed in the
	// LogIn/SignUp JSP
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String first = request.getParameter("first");
		String last = request.getParameter("last");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confirm = request.getParameter("confirmPassword");
		String image = request.getParameter("image");
		HttpSession http = request.getSession();

		/*
		 * Message used to communicate with client about log-in
		 * 
		 * Returns the alert message
		 */

		String message = "";

		synchronized (this) {

			if (first.isEmpty())
				message += "Your first name cannot be empty\n";

			if (last.isEmpty())
				message += "Your last name cannot be empty\n";

			if (username.isEmpty())
				message += "Your username cannot be empty\n";

			if (!has8Characters(password)) {
				message += "Your password must have at least 8 characters\n";
			}

			if (!containsAlpha(password)) {
				message += "Your password must have at least one alphabetical character\n";
			}

			if (!containsDigit(password)) {
				message += "Your password must have at least one digit\n";
			}

			if (!password.equals(confirm)) {
				message += "Your re-entered password is incorrect\n";
			}

			JDBCDriver database = new JDBCDriver();

			if (database.userExists(username)) {
				message += "This username already exists\n";
			}

			if (message.length() == 0) {
				message += "Signed Up!";
				database.createUser(first, last, username, (password.isEmpty()) ? defaultImage : password, image);
				http.setAttribute("username", username);
			}

			database.close();

			System.out.println(message);

			// Sends message back to jQuery call
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(message);
		}
	}

	private boolean containsAlpha(String s) {
		return !s.matches("[^a-zA-Z]+");
	}

	private boolean containsDigit(String s) {
		return !s.matches("[^0-9]+");
	}

	private boolean has8Characters(String s) {
		return (s.length() >= 8);
	}
}
