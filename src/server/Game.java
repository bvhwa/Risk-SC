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
import gamelogic.Adjacencies;
import gamelogic.GameLogic;
import gamelogic.Player;
import gamelogic.Territory;

@ServerEndpoint(value="/g")
public class Game {
	
	private static Vector<Session> playerSessions = new Vector<Session>();
	private static Vector<Player> players = new Vector<Player>();
	private static HashMap<Session, Player> sessionPlayerMap = new HashMap <Session, Player>();
	private static GameLogic gl;
	private static int numOfConnections = 0;
	private static int turnPlayer = -1;

	
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
		
		String logMessage = "Activity:";
		
		if (message.startsWith("player_info: "))	{	
			intializePage(message, session);
			logMessage += message.split(" ")[1] + " has joined the game";
			this.sendLog(logMessage);
		} else if (message.startsWith("Placing_Troops"))	{
			String[] placeTroops = message.split(" ");
			int numTroops = Integer.parseInt(placeTroops[1]);
			String territory = placeTroops[2];
			
			Game.gl.place(Adjacencies.getTerritoryID(territory), numTroops);
			this.sendStatistics(Game.gl.getPlayers());
			logMessage += players.get(turnPlayer).getUserName() + " placed " + numTroops + " troops at " + territory; 
			this.sendLog(logMessage);
		} else if(message.startsWith("Attack from:"))	{
			String attackToTerritories = "Attack To:";			
			String attackFromTerritory = message.split(":")[1];
			attackToTerritories += Game.gl.getTerritory(attackFromTerritory).getTroops() - 1;
			Territory[] territories = Game.gl.getAdjacentNonOwnedTerritories(Adjacencies.getTerritoryID(attackFromTerritory));
			for (Territory t: territories)	{
				attackToTerritories += "\n" + t.getName();
			}
			System.out.println(attackToTerritories);
			this.sendMessageToSession(attackToTerritories, session);
		} else if (message.startsWith("Move from:"))	{
			String moveToTerritories = "Move To:";
			String moveFromTerritory = message.split(":")[1];
			moveToTerritories += Game.gl.getTerritory(moveFromTerritory).getTroops() - 1;
			Territory[] territories = Game.gl.getAdjacentOwnedTerritories(Adjacencies.getTerritoryID(moveFromTerritory));
			for (Territory t: territories)	{
				moveToTerritories += "\n" + t.getName();
			}
			System.out.println(moveToTerritories);
			this.sendMessageToSession(moveToTerritories, session);
		}
	}

	@OnClose
	public void close(Session session)	{
		System.out.println("Disconnected " + sessionPlayerMap.get(session) + " from Game!");
		numOfConnections--;
		
		playerSessions.remove(session);
		players.remove(sessionPlayerMap.get(session));
	}
	
	@OnError
	public void error(Throwable error) {
		// Handle errors here
	}
	
	public void intializePage(String message, Session session)	{
		String[] playerInfo = message.split(" ");
		
		String username = playerInfo[1];
		String image = playerInfo[2];
		int maxPlayers = Integer.parseInt(playerInfo[3]);
		
		// Remove old instances of username
		for (int i = 0; i < Game.players.size(); i++)	{
			if (Game.players.get(i).getUserName().equals(username))	{
				Game.players.remove(i);
			}
		}
		
		Player p = new Player(username, image);
		
		Game.sessionPlayerMap.put(session, p);
		Game.players.addElement(p);
		
		if (maxPlayers == Game.numOfConnections)	{
			Game.gl = new GameLogic(Game.players);
			this.sendStatistics(Game.gl.getPlayers());
			Game.turnPlayer = 0;
			
			String firstSendMessage = "Place Troops:" + Game.gl.getPlaceNumOfTroops(turnPlayer);
			for (Territory t: Game.gl.getOwnedTerritories(turnPlayer))	{
				firstSendMessage += "\n" + t.getName();
			}
			
			this.sendMessageToSession(firstSendMessage, Game.playerSessions.get(turnPlayer));
		} else	{
			this.sendStatistics(Game.players.toArray(new Player[Game.players.size()]));
		}
	}
	
	public void sendStatistics(Player[] players)	{
		String message = "statistics:";
		
		for (Player p : players)	{
			message += "\n" + p.getUserName() + "\t" + p.getTerritories() + "\t" + p.getTroops();
		}
		
		for (Session s : Game.playerSessions)	{
			this.sendMessageToSession(message, s);
		}
	}

	
	private void sendLog(String logMessage) {
		for (Session s: Game.playerSessions)	{
			this.sendMessageToSession(logMessage, s);
		}
			
	}
	
	public void sendMessageToSession(String message, Session session)	{
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException ioe) {
			System.out.println("IOException in sendStatistics: " + ioe.getMessage());
		}
	}
}
