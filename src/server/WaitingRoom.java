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

@ServerEndpoint(value="/ss")
public class WaitingRoom {
	
	private static Vector <Session> players = new Vector<Session>();
	private static HashMap <Session, String> usernames = new HashMap <Session, String>();
	
	@OnOpen
	public void open(Session session)	{
		System.out.println("Connection!");
		this.players.addElement(session);
	}
	
	@OnMessage
	public void message(String message, Session session)	{
		System.out.println(message);
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
	
	@OnClose
	public void close(Session session)	{
		System.out.println("Disconnected " + this.usernames.get(session) + "!");
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
		
	}
	
//	// private int numUsers = 0;
//	static Set<Session> players = Collections.synchronizedSet(new HashSet<Session>());
//	
//	@OnOpen
//	public void onOpen(Session user) throws Exception {
//		// System.out.println("Open connection...");
//		players.add(user);
//		for(Session player: players) {
//			player.getBasicRemote().sendObject(player.getId());
//		}
//	}
//
//	@OnClose
//	public void onClose(Session user) throws Exception {
//		players.remove(user);
//		for(Session player: players) {
//			if(!user.getId().equals(player.getId())) {
//				player.getBasicRemote().sendObject(player.getId());
//			}
//		}
//	}
}
