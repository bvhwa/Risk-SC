package server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/HostServlet")
public class HostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public HostServlet() {
        super();
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	/*
    	 * If we clicked on host, then we intend to make ourselves host of a new game.
    	 * In this instance, we will only have one game running at any given time, so
    	 * we would need to check if a host already exists by checking the vector of users
    	 * that are currently logged-in. If a logged-in user that has a thread is already 
    	 * a host, then we are unable to host a game and should display a message saying
    	 * that we cannot host the game and must join it. 
    	 * 
    	 * However, if nobody is yet a host, then we have to instantiate a new GameRoom object
    	 * and WaitingRoom object to act as the server for the clients to connect to. Each of
    	 * these objects will have ServerSockets that clients can connect to when they click join.
    	 * In this case, the host will automatically be connected. Of course, we will probably need
    	 * server threads to connect the client thread to the server when they perform their actions.
    	 * I'm not really sure how to integrate all of this, but I think this is the gist of how it
    	 * would work. The servlet serves as a connector between the GUI and database/server while
    	 * the server threads acts as the communication between the Java client and Java server. 
    	 * All of this seems very confusing, so we had best work together on the back-end to get all of
    	 * this functionality down. 
    	 * 
    	 * I'm assuming that for the instantiation we will do it using the IP address of whoever's computer
    	 * so that when we run it, we are actually able to all play through the internet. 
    	 * 
    	 * I don't think normal sockets work for this project at all.
    	 */
    }
}
