package server;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/waitingroom")
public class WaitingRoomWS {
	
	@OnOpen
	public void onOpen() {
		System.out.println("Open connection...");
	}
	
	@OnClose
	public void onClose() {
		System.out.println("Close connection...");
	}
	
	@OnMessage
	public String onMessage(String message) {
		System.out.println("Message from the client: " + message);
		String echoMsg = "Echo from the server : " + message;
		return echoMsg;
	}
	
	@OnError
	public void onError(Throwable e) {
		System.out.println(e.getMessage());
	}
}
