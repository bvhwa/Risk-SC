package client;

import java.util.Vector;
import classes.Player;

public class GameRoom {
	private String host = null;
	private int numUsers = 0;
	private Vector<Player> players = new Vector<Player>();
	
	public GameRoom(String host) {
		this.host = host;
	}

	public Vector<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Vector<Player> players) {
		this.players = players;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getNumUsers() {
		return numUsers;
	}

	public void setNumUsers(int numUsers) {
		this.numUsers = numUsers;
	}
	
	public synchronized void addUser(Player user) {
		this.players.add(user);
	}
	
	public synchronized void removeUser(Player user) {
		this.players.remove(user);
	}
}
