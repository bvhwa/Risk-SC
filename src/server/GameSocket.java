package server;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import classes.Game;
import classes.Player;
import client.GameRoom;

@ServerEndpoint("/gs")
public class GameSocket {
	private static String hostName = "";
	private static Vector<Player> p = new Vector<Player>();
	private static GameRoom gameRoom = new GameRoom(hostName);
	private static Vector<Session> players = new Vector<Session>();
	private static HashMap<Session, String> usernames = new HashMap <Session, String>();
	
	@OnOpen
	// Add the player's session to the vector of sessions
	public void open(Session session)	{
		players.add(session);
		System.out.println("Connection!");
	}
	
	@OnMessage
	// Retrieve the message from the client
	public void message(String message, Session session)	{
		// if we get the username
		if(message.equals("")) {
			
		}
		else {
			try {
				String first = "";
				String last = "";
				
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/final?user=root&password=root&allowPublicKeyRetrieval=true&useSSL=false");
		    	
				if(this.userExists(message, conn)) {
					PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        			ps.setString(1, message);
        			ResultSet rs = ps.executeQuery();
        			
        			if(rs.next()) {
        				first = rs.getString(3);
        				last = rs.getString(4);
        				Player player = new Player(0, first, last, message, true);
        				p.add(player);
        				gameRoom.setPlayers(p);
        			}
        			else {
        				System.out.println("User does not exist.");
        			}
				}
			} 
			catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			} 
			catch (SQLException sqle) {
				sqle.printStackTrace();
			}
    		
		}
	}

	@OnClose
	public void close(Session session)	{
		
	}
	
	@OnError
	public void error (Throwable error)	{
		
	}
	
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
}
