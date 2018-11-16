package server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/endpoint")
public class WaitingRoomWS {
	
	// private int numUsers = 0;
	static Set<Session> players = Collections.synchronizedSet(new HashSet<Session>());
	
	@OnOpen
	public void onOpen(Session user) throws Exception {
		// System.out.println("Open connection...");
		players.add(user);
		for(Session player: players) {
			player.getBasicRemote().sendObject(player.getId());
		}
	}

	@OnClose
	public void onClose(Session user) throws Exception {
		players.remove(user);
		for(Session player: players) {
			if(!user.getId().equals(player.getId())) {
				player.getBasicRemote().sendObject(player.getId());
			}
		}
	}
}
