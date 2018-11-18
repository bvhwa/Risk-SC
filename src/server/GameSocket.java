package server;


import java.util.HashMap;
import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

public class GameSocket {
	private static Vector<Session> players = new Vector<Session>();
	private static HashMap<Session, String> usernames = new HashMap <Session, String>();
	
	@OnOpen
	// Add the player's session to the vector of sessions
	public void open(Session session)	{
		
	}
	
	@OnMessage
	// Retrieve the message from the client
	public void message(String message, Session session)	{
		
	}
	
	@OnClose
	public void close(Session session)	{
		
	}
	
	@OnError
	public void error (Throwable error)	{
		
	}
}
