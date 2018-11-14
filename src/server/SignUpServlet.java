package server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SignUpServlet() {
        super();
    }

    // Service block to sign up the user based off the form constructed in the LogIn/SignUp JSP
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String name = request.getParameter("name");
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	String image = request.getParameter("image");
    	String fname = this.getFirstName(name);
    	String lname = this.getLastName(name);
    	
    	String errorMessage = null;
    	boolean success = false;
    	
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/final?user=root&password=root&allowPublicKeyRetrieval=true&useSSL=false");
    		
    		if(!this.userExists(username, conn)) {
    			if(this.goodPass(password) == 0) {
    				this.createUser(fname, lname, username, password, image, conn);
    				success = true;
    			}
    			else if(this.goodPass(password) == 1) {
    				// System.err.println("Password does not have enough alphanumeric characters!");
    				errorMessage = "Password does not have enough alphanumeric characters!";
    			}
    			else if(this.goodPass(password) == 2) {
    				// System.err.println("Password does not have any characters!");
    				errorMessage = "Password does not have any characters!";
    			}
    			else {
    				// System.err.println("Password does not have any numbers!");
    				errorMessage = "Password does not have any numbers!";
    			}
    		}
    		else {
    			// System.err.println("User already exists!");
    			errorMessage = "User already exists!";
    		}
    		
    		conn.close();
    	}
    	catch (SQLException sqle) {
    		System.out.println(sqle.getMessage());
    	} 
    	catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		}
    	
    	// Sends error message back to LogIn JSP if sign up failed
    	request.setAttribute("success", success);
    	request.setAttribute("error", errorMessage);
    	request.getRequestDispatcher("LogIn.jsp").forward(request, response);
    }
    
    // Returns the first name from full name
    private String getFirstName(String name) {
    	String first = null;
    	
    	String[] names = name.split(" ");
    	if(names != null) {
    		first = names[0];
    	}
    	
    	return first;
    }
    
    // Returns the last name from full name
    private String getLastName(String name) {
    	String last = null;
    	
    	String[] names = name.split(" ");
    	if(names != null) {
    		last = names[0];
    	}
    	
    	return last;
    }
    
    // Checks whether or not a user exists already
    private boolean userExists(String username, Connection conn) {
    	try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = '" + username + "'");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				return true;
			}
		} 
		catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
    	
    	return false;
    }
    
    // Creates a user within the database
    private void createUser(String first, String last, String username, String password, String image, Connection conn) {
    	try {
    		PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, fname, lname, image) values (?, ?, ?, ?, ?)");
    		
    		ps.setString(1, username);
    		ps.setString(2, password);
    		ps.setString(3, first);
    		ps.setString(4, last);
    		ps.setString(5, image);
    		
    		ps.execute();
    	}
    	catch (SQLException sqle) {
    		System.out.println(sqle.getMessage());
    	}
    }
    
    /* Checks if the password meets criteria:	
   		1. Password length must be greater than or equal to 8 characters
   		2. Password must contain characters
   		3. Password must contain numbers
   	*/
    private int goodPass(String password) {
		if(password.length() < 8) {
			return 1;
		}
		if(!containsChars(password)) {
			return 2;
		}
		if(!containsNums(password)) {
			return 3;
		}
    	
    	return 0;
    }
    
    // Checks if password contains characters
    private boolean containsChars(String password) {
		char[] passwordChars = password.toCharArray();
    	
		for(int i = 0; i < passwordChars.length; i++) {
			if((passwordChars[i] > 'a' && passwordChars[i] < 'z') || (passwordChars[i] > 'A' && passwordChars[i] < 'Z')) {
				return true;
			}
		}
    	
    	return false;
    }
    
    // Checks if password contains numbers
    private boolean containsNums(String password) {
    	char[] passwordChars = password.toCharArray();
    	
		for(int i = 0; i < passwordChars.length; i++) {
			if(passwordChars[i] > '0' && passwordChars[i] < '9') {
				return true;
			}
		}
    	
    	return false;
    }
}
