package server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import classes.JDBCDriver;

@ServerEndpoint(value="/wr")
public class WaitingRoom {
	
	private static Vector<Session> players = new Vector<Session>();
	private static HashMap<Session, String> usernames = new HashMap <Session, String>();
	public static int numConnections = 0;

	
	@OnOpen
	// Add the player's session to the vector of sessions
	public void open(Session session)	{
		
		JDBCDriver database = new JDBCDriver();
		
		String username = WaitingRoom.usernames.get(session);
		
		if (username != null)
			database.setSignedIn(username, true);
		
		database.close();
		
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
			WaitingRoom.usernames.put(session, message);
			
			String users = "";
			for (int i = 0; i < players.size(); i++) {
				users += usernames.get(players.get(i)) + ((i < players.size() - 1) ? "&" : "");
			}
			
			
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

		
		JDBCDriver database = new JDBCDriver();
		
		String username = WaitingRoom.usernames.get(session);
		
		if (username != null)
			database.setSignedIn(username, false);
		
		database.close();
		
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
	public void error(Throwable error) {
		// Handle errors here
	}
}
