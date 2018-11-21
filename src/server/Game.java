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
import gamelogic.GameLogic;
import gamelogic.Player;

@ServerEndpoint(value="/g")
public class Game {
	
	private static Vector<Session> playerSessions = new Vector<Session>();
	private static Vector<Player> players = new Vector<Player>();
	private static HashMap<Session, Player> sessionPlayerMap = new HashMap <Session, Player>();
	private static GameLogic gl;
	public static int numOfConnections = 0;

	
	@OnOpen
	// Add the player's session to the vector of sessions
	public void open(Session session)	{
		
		System.out.println("Connection to Game!");
		playerSessions.addElement(session);
		numOfConnections++;
	}
	
	@OnMessage
	// Retrieve the message from the client
	public void message(String message, Session session)	{
		
		System.out.println(message);
		
		if (message.startsWith("player_info: "))	{
			
			String[] playerInfo = message.split(" ");
			
			String username = playerInfo[1];
			String image = playerInfo[2];
			int maxPlayers = Integer.parseInt(playerInfo[3]);
			
			Player p = new Player(username, image);
			
			Game.sessionPlayerMap.put(session, p);
			Game.players.addElement(p);
			
			if (maxPlayers == Game.numOfConnections)	{
				System.out.println("Updating Stats");
				Game.gl = new GameLogic(Game.players);
				this.sendStatistics(Game.gl.getPlayers());
			} else	{
				System.out.println("Updating Users");
				this.sendStatistics(Game.players.toArray(new Player[Game.players.size()]));
			}
			
		} 
	}
	
	@OnClose
	public void close(Session session)	{
		System.out.println("Disconnected " + sessionPlayerMap.get(session) + " from Game!");
		numOfConnections--;
		
		playerSessions.remove(session);
	}
	
	@OnError
	public void error(Throwable error) {
		// Handle errors here
	}
	
	public void sendStatistics(Player[] players)	{
		String message = "statistics:";
		
		for (Player p : players)	{
			message += "\n" + p.getUserName() + "\t" + p.getTerritories() + "\t" + p.getTroops();
		}
		
		for (Session s : Game.playerSessions)	{
			try {
				s.getBasicRemote().sendText(message);
			} catch (IOException ioe) {
				System.out.println("IOException in sendStatistics: " + ioe.getMessage());
			}
		}
	}
}
