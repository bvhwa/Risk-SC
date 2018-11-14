package server;

import java.io.IOException;
import java.sql.Connection;
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

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    }
    
    private String getFirstName(String name) {
    	String first = null;
    	
    	String[] names = name.split(" ");
    	if(names != null) {
    		first = names[0];
    	}
    	
    	return first;
    }
    
    private String getLastName(String name) {
    	String last = null;
    	
    	String[] names = name.split(" ");
    	if(names != null) {
    		last = names[0];
    	}
    	
    	return last;
    }
    
    private boolean userExists(String username, Connection conn) {
		boolean exists = false;
    	
    	try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = '" + username + "'");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				exists = true;
			}
		} 
		catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
    	
    	return exists;
    }
    
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
    
    private boolean containsChars(String password) {
		char[] passwordChars = password.toCharArray();
    	
		for(int i = 0; i < passwordChars.length; i++) {
			if((passwordChars[i] > 'a' && passwordChars[i] < 'z') || (passwordChars[i] > 'A' && passwordChars[i] < 'Z')) {
				return true;
			}
		}
    	
    	return false;
    }
    
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
