package server;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

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
			initializePage(message, session);
		} else if (message.startsWith("Placing"))	{
			place(message);
		} else if (message.startsWith("Attacking"))	{
			attack(message);
		} else if (message.startsWith("Moving"))	{
			move(message);
		} else if(message.startsWith("Attack from:"))	{
			getNonOwnedAdjacent(message, session);
		} else if (message.startsWith("Move from:"))	{
			getOwnedAdjacent(message, session);
		} else if (message.equals("Finished Placing") || message.equals("Attacked"))	{
			initAttacking(session);
		} else if (message.equals("Finished Attacking"))	{
			initMoving(session);
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
	
	/**
	 * Initializes the page and the internal data of the class to match.
	 * Starts a game when the maximum expected number of connections is reached.
	 * @param message the message containing the user data who has just joined
	 * @param session the session of the user who has just joined
	 */
	private void initializePage(String message, Session session)	{
		String[] playerInfo = message.split(" ");
		
		// Extract the data from the message
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
		
		// If we have reached the maximum number of connections, initialize the game and broadcast the stats
		if (maxPlayers == Game.numOfConnections)	{
			Game.gl = new GameLogic(Game.players);
			this.sendStatistics(Game.gl.getPlayers());
			this.startTurn();
		} else	{
			this.sendStatistics(Game.players.toArray(new Player[Game.players.size()]));
		}
		
		// Send "username has joined" to every active session
		String logMessage = "Activity:" + username + " has joined the game";
		this.sendLog(logMessage);
	}

	/**
	 * Performs the place and sends the data to every active session
	 * @param message the message containing the information of the place
	 */
	private void place(String message) {

		String[] placeTroops = message.split(",");
		int numTroops = Integer.parseInt(placeTroops[1]);
		String territory = placeTroops[2];
		
		Game.gl.place(Adjacencies.getTerritoryID(territory), numTroops);
		this.sendStatistics(Game.gl.getPlayers());
		String logMessage = "Activity:" + players.get(turnPlayer).getUserName() + " placed " + numTroops + " troops at " + territory; 
		this.sendLog(logMessage);
	}
	
	/**
	 * Performs the attack and sends the data to every active session
	 * @param message the message containing the information of the attack
	 */
	private void attack(String message)	{

		String[] attackTroops = message.split(",");
		String attackFromTerritory = attackTroops[1];
		String attackToTerritory = attackTroops[2];
		int troops = Integer.parseInt(attackTroops[3]);
		// Update so the defender has a choice of the troops
		int defendTroops = Game.gl.getTerritory(attackToTerritory).getTroops();
		
		// Update attack to return the result of the battle
		Game.gl.attack(Adjacencies.getTerritoryID(attackFromTerritory), troops, Adjacencies.getTerritoryID(attackToTerritory), defendTroops);
		this.sendStatistics(Game.gl.getPlayers());
		String logMessage = "Activity:" + players.get(turnPlayer).getUserName() + " attacked from " + attackFromTerritory + " with " + troops + " troops while " + players.get(Game.gl.getTerritory(attackToTerritory).getOccupier()).getUserName() + " defended " + attackToTerritory + " with " + defendTroops + " troops";
		this.sendLog(logMessage);
	}
	
	/**
	 * Performs the move and sends the data to every active session
	 * @param message the message containing the information of the move
	 */
	private void move(String message)	{

		String[] moveTroops = message.split(",");
		String moveFromTerritory = moveTroops[1];
		String moveToTerritory = moveTroops[2];
		int troops = Integer.parseInt(moveTroops[3]);
		
		Game.gl.move(Adjacencies.getTerritoryID(moveFromTerritory), Adjacencies.getTerritoryID(moveToTerritory), troops);
		String logMessage = "Activity:" + players.get(turnPlayer).getUserName() + " moved " + troops + " troops from " + moveFromTerritory + " to " + moveToTerritory;
		this.sendLog(logMessage);
		
		// Move To next player's turn
		this.startTurn();
	}
	
	/**
	 * Return the territories that can be attacked from the current territory
	 * @param message the message containing the current territory
	 * @param session the session to send the data of the possible territories to
	 */
	private void getNonOwnedAdjacent(String message, Session session)	{

		String attackToTerritories = "Attack To:";			
		String attackFromTerritory = message.split(":")[1];
		attackToTerritories += Game.gl.getTerritory(attackFromTerritory).getTroops() - 1;
		Territory[] territories = Game.gl.getAdjacentNonOwnedTerritories(Adjacencies.getTerritoryID(attackFromTerritory));
		for (Territory t: territories)	{
			attackToTerritories += "\n" + t.getName();
		}
		this.sendMessageToSession(attackToTerritories, session);
	}
	
	/**
	 * Return the territories that can be moved to from the current territory
	 * @param message the message containing the current territory
	 * @param session the session to send the data of the possible territories to
	 */
	private void getOwnedAdjacent(String message, Session session)	{

		String moveToTerritories = "Move To:";
		String moveFromTerritory = message.split(":")[1];
		moveToTerritories += Game.gl.getTerritory(moveFromTerritory).getTroops() - 1;
		Territory[] territories = Game.gl.getAdjacentOwnedTerritories(Adjacencies.getTerritoryID(moveFromTerritory));
		for (Territory t: territories)	{
			moveToTerritories += "\n" + t.getName();
		}
		this.sendMessageToSession(moveToTerritories, session);
	}
	
	/**
	 * A method called by the onMessage method to initialize the elements on the Attacking Stage
	 * @param session the session to send the data of the elements to
	 */
	private void initAttacking(Session session)	{

		Territory[] ownedTerritories = Game.gl.getOwnedTerritories(turnPlayer);
		
		// Only Display the Territories the player can actually attack from
		LinkedList <Territory> ownedTerritoriesWithMoreThanOneTroopLinkedList = new LinkedList<Territory>();
		for (Territory t: ownedTerritories)
			if (t.getTroops() > 0)
				ownedTerritoriesWithMoreThanOneTroopLinkedList.add(t.clone());
		
		Territory[] ownedTerritoriesWithMoreThanOneTroop = ownedTerritoriesWithMoreThanOneTroopLinkedList.toArray(new Territory[ownedTerritoriesWithMoreThanOneTroopLinkedList.size()]);
		
		Territory initTerritory = ownedTerritoriesWithMoreThanOneTroop[0];
		Territory[] nonOwnedAdjacentTerritories = Game.gl.getAdjacentNonOwnedTerritories(initTerritory.getID());
		int maxAttackTroops = initTerritory.getTroops() - 1;
		
		// Update the attack message
		String updateAttacking = "Update Attacking\n";
		for (Territory t: ownedTerritoriesWithMoreThanOneTroop)
			updateAttacking += "\t" + t.getName();
		updateAttacking += "\n";
		for (Territory t: nonOwnedAdjacentTerritories)
			updateAttacking += "\t" + t.getName();
		updateAttacking += "\n";
		updateAttacking += maxAttackTroops;
		
		this.sendMessageToSession(updateAttacking, session);
		// Need to check for player have 0 territories
	}
	
	/**
	 * A method called by the onMessage method to initialize the elements on the Waiting Stage
	 * @param session the session to send the data of the elements to
	 */
	private void initMoving(Session session)	{

		Territory[] ownedTerritories = Game.gl.getOwnedTerritories(turnPlayer);

		// Only Display the Territories the player can actually move from
		LinkedList <Territory> ownedTerritoriesWithMoreThanOneTroopLinkedList = new LinkedList<Territory>();
		for (Territory t: ownedTerritories)
			if (t.getTroops() > 0)
				ownedTerritoriesWithMoreThanOneTroopLinkedList.add(t.clone());
		
		Territory[] ownedTerritoriesWithMoreThanOneTroop = ownedTerritoriesWithMoreThanOneTroopLinkedList.toArray(new Territory[ownedTerritoriesWithMoreThanOneTroopLinkedList.size()]);
		
		Territory initTerritory = ownedTerritoriesWithMoreThanOneTroop[0];
		Territory[] ownedAdjacentTerritories = Game.gl.getAdjacentOwnedTerritories(initTerritory.getID());
		int maxAttackTroops = initTerritory.getTroops() - 1;
		
		// Update the move message
		String updateAttacking = "Update Moving\n";
		for (Territory t: ownedTerritoriesWithMoreThanOneTroop)
			updateAttacking += "\t" + t.getName();
		updateAttacking += "\n";
		for (Territory t: ownedAdjacentTerritories)
			updateAttacking += "\t" + t.getName();
		updateAttacking += "\n";
		updateAttacking += maxAttackTroops;
		
		this.sendMessageToSession(updateAttacking, session);
		// Need to check for player have 0 territories
	}
	
	/**
	 * Sends a list of statistics to every active session
	 * @param players the list of players create statistics upon
	 */
	private void sendStatistics(Player[] players)	{
		String message = "statistics:";
		
		for (Player p : players)	{
			message += "\n" + p.getUserName() + "\t" + p.getTerritories() + "\t" + p.getTroops();
		}
		
		for (Session s : Game.playerSessions)	{
			this.sendMessageToSession(message, s);
		}
	}

	
	/**
	 * Sends a message to every active session
	 * @param message the message to send to every available session
	 */
	private void sendLog(String message) {
		for (Session s: Game.playerSessions)	{
			this.sendMessageToSession(message, s);
		}
			
	}
	
	/**
	 * Sends a message to the user whose turn is next that they may start their turn
	 */
	private void startTurn()	{
		Game.turnPlayer = (Game.turnPlayer + 1) % Game.gl.getPlayers().length;
		
		String startMessage = "Place Troops:" + Game.gl.getPlaceNumOfTroops(turnPlayer);
		for (Territory t: Game.gl.getOwnedTerritories(turnPlayer))	{
			startMessage += "\n" + t.getName();
		}
		
		this.sendMessageToSession(startMessage, Game.playerSessions.get(turnPlayer));
	}
	
	/**
	 * Sends a message to a session
	 * @param message the message to send to the session
	 * @param session the session to send the message to
	 */
	private void sendMessageToSession(String message, Session session)	{
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException ioe) {
			System.out.println("IOException in sendStatistics: " + ioe.getMessage());
		}
	}
}
