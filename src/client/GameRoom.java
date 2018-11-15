package client;

import java.util.Vector;
import classes.User;

public class GameRoom {
	private String host = null;
	private int numUsers = 0;
	private Vector<User> players = new Vector<User>();
	
	public GameRoom(String host) {
		this.host = host;
	}

	public Vector<User> getPlayers() {
		return players;
	}

	public void setPlayers(Vector<User> players) {
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
	
	public synchronized void addUser(User user) {
		this.players.add(user);
	}
	
	public synchronized void removeUser(User user) {
		this.players.remove(user);
	}
}
