package server;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Map;

public class Configurator extends ServerEndpointConfig.Configurator
{
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response)
    {
        HttpSession http = (HttpSession)request.getHttpSession();
        if(http == null)
        	{
        		config.getUserProperties().put("http", 0);
        	}
        	
        else 
        	{
        		config.getUserProperties().put("http", http);
        	}
    }
}
