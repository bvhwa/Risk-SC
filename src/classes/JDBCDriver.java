package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCDriver {
	
	private Connection conn;
	
	private final String schema = "final";
	private final String username = "root";
	private final String password = "root";
	
	/**
	 * Initializes the connection to the database
	 */
	public JDBCDriver()	{
		try	{
	        Class.forName("com.mysql.jdbc.Driver");
	        this.conn = DriverManager.getConnection("jdbc:mysql://localhost/" + this.schema + "?user=" + this.username + "&password=" + this.password + "&allowPublicKeyRetrieval=true&useSSL=false");
    	} catch (SQLException sqle) {
    		System.out.println("SQLException in JDBCDriver Constructor: " + sqle.getMessage());
    	} catch (ClassNotFoundException cnfe) {
    		System.out.println("ClassNotFoundException in JDBCDriver Constructor: " + cnfe.getMessage());
		}
	}
	
	/**
	 * Checks if the User already exists in the database to prevent duplicate accounts
	 * @param username the username to check the existence of
	 * @return true if the username exists in the database, false otherwise
	 */
    public boolean userExists(String username) {
    	try {
			PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM users WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				return true;
			}
		} 
		catch (SQLException sqle) {
			System.out.println("SQLException in JDBCDriver userExists: " + sqle.getMessage());
		}
    	
    	return false;
    }

    /**
     * Checks the credentials provided for Log In
     * @param username attempt to authenticate the username
     * @param password attempt to authenticate the password
     * @return true if the credentials match the database, false otherwise
     */
	public boolean checkCredentials(String username, String password) {
		
		try	{
			PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM users WHERE `username` = ? AND `password` = ?");
			ps.setString(1, username);
			ps.setString(2, Authentication.hashString(password));
			ResultSet rs = ps.executeQuery();
			
			return rs.next();
		} catch (SQLException sqle)	{
			System.out.println("SQLException in JDBCDriver checkCredentials: " + sqle.getMessage());
		}
		
		return false;
	}

	/**
	 * Checks the Signed In Status of a User
	 * @param username check the signed in status of the username
	 * @return true if the user is signed in, false otherwise
	 */
	public boolean isSignedIn(String username) {
		
		try	{
			PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM users WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			return (rs.next() && rs.getBoolean("playing"));
		} catch (SQLException sqle)	{
			System.out.println("sqle: " + sqle.getMessage());
		}
		return false;
	}

	/**
	 * Changes the Signed In Status of a User
	 * @param username the username to update the status of
	 * @param signInStatus true if the user is playing, false otherwise
	 * @return true if the status was updated, false otherwise
	 */
	public boolean setSignedIn(String username, boolean signInStatus) {
		
		try	{
			PreparedStatement ps = conn.prepareStatement("UPDATE users SET playing = ? WHERE username = ?");
			ps.setBoolean(1, signInStatus);
			ps.setString(2, username);
			ps.execute();
			return true;
		} catch (SQLException sqle)	{
			System.out.println("SQLException in JDBCDriver setSignedIn: " + sqle.getMessage());
			return false;
		}
		
	}
	
	/**
	 * Creates a User in the Database
	 * @param first the first name of the user
	 * @param last the last name of the user
	 * @param username the username of the user
	 * @param password the hashed password of the user
	 * @param image the image url of the user
	 * @return true if the creation of the user was successful, false otherwise
	 */
	public boolean createUser(String first, String last, String username, String password, String image) {
    	try {
    		PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, fname, lname, image) values (?, ?, ?, ?, ?)");
    		
    		String hash = Authentication.hashString(password);
    		
    		ps.setString(1, username);
    		ps.setString(2, hash);
    		ps.setString(3, first);
    		ps.setString(4, last);
    		ps.setString(5, image);
    		
    		ps.execute();
    		return true;
    	}
    	catch (SQLException sqle) {
    		System.out.println("SQLException in JDBCDriver createUser: " + sqle.getMessage());
    		return false;
    	}
    }
	
	/**
	 * Closes the connection to the database
	 */
	public void close()	{
		try {
			this.conn.close();
		} catch (SQLException sqle)	{
			System.out.println("SQLException in JDBCDriver close: " + sqle.getMessage());
		}
	}
    
	
}
