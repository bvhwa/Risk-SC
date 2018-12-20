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
	private static boolean alreadyStarted = false;

	
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
		} else if (message.equals("Finished Moving"))	{
			initWaiting(session);
		} else if (message.startsWith("Initiated Attack"))	{
			askDefender(message);
		}
	}

	@OnClose
	public void close(Session session)	{
		System.out.println("Disconnected " + sessionPlayerMap.get(session).getUserName() + " from Game!");
		numOfConnections--;
		
		JDBCDriver database = new JDBCDriver();
		database.setSignedIn(sessionPlayerMap.get(session).getUserName(), false);
		database.close();
		
		playerSessions.remove(session);
		players.remove(sessionPlayerMap.get(session));
		
		alreadyStarted = false;
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
		
		// Set the Sign-In To True
		JDBCDriver database = new JDBCDriver();
		database.setSignedIn(username, true);
		database.close();
		
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
		if ((maxPlayers == Game.numOfConnections) && !alreadyStarted)	{
			alreadyStarted = true;
			Game.gl = new GameLogic(Game.players);
			this.sendStatistics(Game.gl.getPlayers());
			this.sendMap(Game.gl.getTerritoryMap());
			this.startTurn();
			System.out.println("Game Started");
		} else	{
			this.sendStatistics(Game.players.toArray(new Player[Game.players.size()]));
		}
		
		// Send "username has joined" to every active session
		String logMessage = "Activity:<b>Connection - </b>" + username + " has joined the game";
		this.sendMessageToEverySession(logMessage);
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
		String logMessage = "Activity:<b>Placement - </b>" + players.get(turnPlayer).getUserName() + " placed " + numTroops + " troops at " + territory; 
		this.sendMessageToEverySession(logMessage);
		this.sendMap(Game.gl.getTerritoryMap());
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
//		int defendTroops = Math.min(Game.gl.getTerritory(attackToTerritory).getTroops(), 2); // Maximum value of 2 troops
		int defendTroops = Integer.parseInt(attackTroops[4]);
		
		// Calculate the stats before
		int attackedTerritoryOccupier = Game.gl.getTerritory(attackToTerritory).getOccupier();
		int attackTroopsLost = Game.gl.getTerritory(attackFromTerritory).getTroops();
		int defendTroopsLost = Game.gl.getTerritory(attackToTerritory).getTroops();
		
		Game.gl.attack(Adjacencies.getTerritoryID(attackFromTerritory), troops, Adjacencies.getTerritoryID(attackToTerritory), defendTroops);
		
		// Calculate stats after
		attackTroopsLost -= Game.gl.getTerritory(attackFromTerritory).getTroops();
		defendTroopsLost -= Game.gl.getTerritory(attackToTerritory).getTroops();
		
		String attacker = Game.players.elementAt(Game.turnPlayer).getUserName();
		String defender = Game.players.elementAt(Game.gl.getTerritory(attackToTerritory).getOccupier()).getUserName();
		
		// Necessary check because the troops in defending territory will go up if conquered
		String logMessage = "Activity:<b>Battle - </b>";
		if (attackedTerritoryOccupier == Game.gl.getTerritory(attackToTerritory).getOccupier())	{			
	
			logMessage += "<i>";
			
			if (attackTroopsLost < defendTroopsLost)	{
				logMessage += attacker + " wins ";
			} else if (defendTroopsLost < attackTroopsLost)	{
				logMessage += defender + " wins ";
			} else	{
				logMessage += "Tie ";
			}
			
			logMessage += "</i>(";
			
			logMessage += attacker + " lost " + attackTroopsLost + " troops attacking from " + attackFromTerritory + " and " + defender + " lost " + defendTroopsLost + " troops defending " + attackToTerritory;
		} else	{
			logMessage += attacker + " conquered - " + attacker + " has " + Game.gl.getTerritory(attackFromTerritory).getTroops() + " troops in " + attackFromTerritory + " and " + Game.gl.getTerritory(attackToTerritory).getTroops() + " troops in " + attackToTerritory;
		}
		
		logMessage += ")";
		
		this.sendMessageToEverySession(logMessage);
		
		this.sendStatistics(Game.gl.getPlayers());
		
		Session curSession = Game.playerSessions.elementAt(Game.gl.getTerritory(attackFromTerritory).getOccupier());
		
		this.sendMessageToSession("Finished Attack", curSession);
		
		this.initAttacking(curSession);
		
		// Check if the player has won
		if (Game.gl.checkWin(turnPlayer))	{
			for (Session s: Game.playerSessions)	{
				this.sendMessageToSession("Winner - " + Game.players.get(turnPlayer).getUserName(), s);
			}
		}
		this.sendMap(Game.gl.getTerritoryMap());
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
		String logMessage = "Activity:<b>Movement - </b>" + players.get(turnPlayer).getUserName() + " moved " + troops + " troops from " + moveFromTerritory + " to " + moveToTerritory;
		this.sendMessageToEverySession(logMessage);
		this.sendMap(Game.gl.getTerritoryMap());
	}
	
	/**
	 * Return the territories that can be attacked from the current territory
	 * @param message the message containing the current territory
	 * @param session the session to send the data of the possible territories to
	 */
	private void getNonOwnedAdjacent(String message, Session session)	{

		String attackToTerritories = "Attack To:";			
		String attackFromTerritory = message.split(":")[1];
		attackToTerritories += Math.min(Game.gl.getTerritory(attackFromTerritory).getTroops() - 1, 3); // Maximum of 3 troops to attack
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
			if (t.getTroops() > 1)
				ownedTerritoriesWithMoreThanOneTroopLinkedList.add(t.clone());
		
		Territory[] ownedTerritoriesWithMoreThanOneTroop = ownedTerritoriesWithMoreThanOneTroopLinkedList.toArray(new Territory[ownedTerritoriesWithMoreThanOneTroopLinkedList.size()]);
		
		// Update the attack message
		String updateAttacking = "Update Attacking\n";
		
		Territory[] nonOwnedAdjacentTerritories = new Territory[0];
		int maxAttackTroops = 0;
		
		if (ownedTerritoriesWithMoreThanOneTroop.length > 0)	{
		
			Territory initTerritory = ownedTerritoriesWithMoreThanOneTroop[0];
			nonOwnedAdjacentTerritories = Game.gl.getAdjacentNonOwnedTerritories(initTerritory.getID());
			maxAttackTroops = Math.min(initTerritory.getTroops() - 1, 3); // Maximum of 3 troops to attack with
		}
		
		
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
	 * A method called by the onMessage method to initialize the elements on the Moving Stage
	 * @param session the session to send the data of the elements to
	 */
	private void initMoving(Session session)	{

		Territory[] ownedTerritories = Game.gl.getOwnedTerritories(turnPlayer);

		// Only Display the Territories the player can actually move from
		LinkedList <Territory> ownedTerritoriesWithMoreThanOneTroopLinkedList = new LinkedList<Territory>();
		for (Territory t: ownedTerritories)
			if (t.getTroops() > 1)
				ownedTerritoriesWithMoreThanOneTroopLinkedList.add(t.clone());
		
		Territory[] ownedTerritoriesWithMoreThanOneTroop = ownedTerritoriesWithMoreThanOneTroopLinkedList.toArray(new Territory[ownedTerritoriesWithMoreThanOneTroopLinkedList.size()]);
		
		Territory[] ownedAdjacentTerritories = new Territory[0];
		int maxMoveTroops = 0;
		
		if (ownedTerritoriesWithMoreThanOneTroop.length > 0)	{
			Territory initTerritory = ownedTerritoriesWithMoreThanOneTroop[0];
			ownedAdjacentTerritories = Game.gl.getAdjacentOwnedTerritories(initTerritory.getID());
			maxMoveTroops = initTerritory.getTroops() - 1;
	}
		
		// Update the move message
		String updateMoving = "Update Moving\n";
		for (Territory t: ownedTerritoriesWithMoreThanOneTroop)
			updateMoving += "\t" + t.getName();
		updateMoving += "\n";
		for (Territory t: ownedAdjacentTerritories)
			updateMoving += "\t" + t.getName();
		updateMoving += "\n";
		updateMoving += maxMoveTroops;
		
		this.sendMessageToSession(updateMoving, session);
		// Need to check for player have 0 territories
	}
	
	/**
	 * A method called by the onMessage method to initialize the elements on the Waiting Stage
	 * @param session the session to send the data of the elements to
	 */
	private void initWaiting(Session session)	{
		
		this.sendMessageToSession("Update Waiting", session);
		
		this.startTurn();
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
	 * Sends an updated map to every active session
	 * @param territories the list of territories to send to every available session
	 */
	private void sendMap(Territory[] territories)	{
		
		String updatedMap = "Update Map:";
		
		for(int i = 0; i < territories.length; i++)
		{
			updatedMap += "\n";
			updatedMap += territories[i].getID();
			updatedMap += " ";
			updatedMap += territories[i].getTroops();
			updatedMap += " ";
			updatedMap += territories[i].getOccupier();
		}

		this.sendMessageToEverySession(updatedMap);
	}
	
	/**
	 * Sends a message to the person who was allegedly attacked
	 * @param message the message to send back to the defender as well as a well formatted human readable message
	 */
	private void askDefender(String message)	{
		String[] attackTroops = message.split(",");
		String attackFromTerritory = attackTroops[1];
		String attackToTerritory = attackTroops[2];
		int troops = Integer.parseInt(attackTroops[3]);
		
		Player attacker = Game.players.elementAt(Game.gl.getTerritory(attackFromTerritory).getOccupier());
		int maxDefendTroops = Game.gl.getTerritory(attackToTerritory).getTroops();
		
		String sendMessage = attacker.getUserName() + " is going to attack " + attackToTerritory + " from " + attackFromTerritory + " with " + troops + " troops. ";
		
		Session defenderSession = Game.playerSessions.elementAt(Game.gl.getTerritory(attackToTerritory).getOccupier());
		this.sendMessageToSession(message + "," + maxDefendTroops + "\n" + sendMessage, defenderSession);
	}
	
	
	/**
	 * Sends a message to every active session
	 * @param message the message to send to every available session
	 */
	private void sendMessageToEverySession(String message) {
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
