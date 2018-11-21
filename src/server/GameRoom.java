package server;

import java.util.Vector;
import classes.Player;
import classes.Game;
import classes.Territory;
import java.io.IOException;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

public class GameRoom 
	{
		private Game g;	
		private String host = null;
		private int numUsers = 0;
		private Vector<Player> players = new Vector<Player>();
		
		
		private static Vector<Session> sessionVector = new Vector<Session>();
		@OnOpen
		public void open (Session session) {
			System.out.println("Connection made!");
		}
		@OnMessage
		public void onMessage(String message, Session session) {
			System.out.println(message);
			
		}
		@OnClose
		public void close(Session session) {
			System.out.println("Disconnecting!");
			sessionVector.remove(session);
		}
		@OnError
		public void error(Throwable error)
		{
			System.out.println("Error!");
		}
			
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