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

@ServerEndpoint(value="/ss")
public class WaitingRoom {
	
	private Vector <Session> players = new Vector<Session>();
	private HashMap <Session, String> usernames = new HashMap <Session, String>();
	
	@OnOpen
	public void open(Session session)	{
		System.out.println("Connection!");
		this.players.addElement(session);
	}
	
	@OnMessage
	public void message(String message, Session session)	{
		
		if (message.equals("Ready to Start Game"))	{
			for (Session player: this.players)	{
				try	{
					player.getBasicRemote().sendText(message);
				} catch (IOException ioe)	{
					System.out.println("ioe: " + ioe.getMessage());
				}
			}
		} else	{
		
			this.usernames.put(session, message);
			
			System.out.println(message);
			
			String users = "";
			for (int i = 0; i < this.players.size(); i++)	{
				users += this.usernames.get(this.players.get(i)) + ((i < this.players.size() - 1) ? "&" : "");
			}
			
			System.out.println(users);
			
			for (Session player: this.players)	{
				
				try {
					player.getBasicRemote().sendText(users);
				} catch (IOException ioe) {
					System.out.println("ioe: " + ioe.getMessage());
				}
	
			}
		}
	}
	
	@OnClose
	public void close(Session session)	{
		System.out.println("Disconnected " + this.usernames.get(session) + "!");

		try	{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/final?user=root&password=root&allowPublicKeyRetrieval=true&useSSL=false");
			PreparedStatement ps = conn.prepareStatement("UPDATE users SET playing = ? WHERE username = '" + this.usernames.get(session) + "'");
			ps.setBoolean(1, false);
			ps.execute();
		}
    	catch (SQLException sqle) {
    		System.out.println(sqle.getMessage());
    	} 
    	catch (ClassNotFoundException cnfe) {
    		System.out.println(cnfe.getMessage());
		}
		
		this.players.remove(session);
		
		String users = "";
		for (int i = 0; i < this.players.size(); i++)	{
			users += this.usernames.get(this.players.get(i)) + ((i < this.players.size() - 1) ? "&" : "");
		}
		
		for (Session player: this.players)	{
			
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
