package server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/wr")
public class WaitingRoom {
	
	private static Vector<Session> players = new Vector<Session>();
	private static HashMap<Session, String> usernames = new HashMap <Session, String>();
	public static int numConnections = 0;
	
	private String password = "root";

	
	@OnOpen
	// Add the player's session to the vector of sessions
	public void open(Session session)	{
		
		try	{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/final?user=root&password=" + password + "&allowPublicKeyRetrieval=true&useSSL=false");
			PreparedStatement ps = conn.prepareStatement("UPDATE users SET playing = ? WHERE username = '" + usernames.get(session) + "'");
			ps.setBoolean(1, false);
			ps.execute();
		}
    	catch (SQLException sqle) {
    		System.out.println(sqle.getMessage());
    	} 
    	catch (ClassNotFoundException cnfe) {
    		System.out.println(cnfe.getMessage());
		}
		
		System.out.println("Connection!");
		players.addElement(session);
		numConnections++;
	}
	
	@OnMessage
	// Retrieve the message from the client
	public void message(String message, Session session)	{
		
		if (message.equals("Ready to Start Game"))	{
			for (Session player: players)	{
				try	{
					player.getBasicRemote().sendText(message);
				} 
				catch (IOException ioe)	{
					System.out.println("ioe: " + ioe.getMessage());
				}
			}
		} 
		else {
			usernames.put(session, message);
			
			System.out.println(message);
			
			String users = "";
			for (int i = 0; i < players.size(); i++) {
				users += usernames.get(players.get(i)) + ((i < players.size() - 1) ? "&" : "");
			}
			
			// System.out.println(users);
			
			for (Session player: players)	{
				try {
					player.getBasicRemote().sendText(users);
				} 
				catch (IOException ioe) {
					System.out.println("ioe: " + ioe.getMessage());
				}
			}
		}
	}
	
	@OnClose
	public void close(Session session)	{
		System.out.println("Disconnected " + usernames.get(session) + "!");
		numConnections--;

		try	{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/final?user=root&password=" + password + "&allowPublicKeyRetrieval=true&useSSL=false");
			PreparedStatement ps = conn.prepareStatement("UPDATE users SET playing = ? WHERE username = '" + usernames.get(session) + "'");
			ps.setBoolean(1, false);
			ps.execute();
		}
    	catch (SQLException sqle) {
    		System.out.println(sqle.getMessage());
    	} 
    	catch (ClassNotFoundException cnfe) {
    		System.out.println(cnfe.getMessage());
		}
		
		players.remove(session);
		
		String users = "";
		for (int i = 0; i < players.size(); i++)	{
			users += usernames.get(players.get(i)) + ((i < players.size() - 1) ? "&" : "");
		}
		
		for (Session player: players)	{
			
			try {
				player.getBasicRemote().sendText(users);
			} catch (IOException ioe) {
				System.out.println("ioe: " + ioe.getMessage());
			}

		}
	}
	
	@OnError
	public void error (Throwable error)	{
		// Handle errors here
	}
}
