package classes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class sendData {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;


    public sendData(Socket socket, ObjectInputStream ois, ObjectOutputStream oos)
    {
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
    }

    

    public void send(Object obj)
    {
        try
        {
            oos.writeObject(obj);
            oos.flush();
        }
        catch(IOException ioe)
        {
            System.out.println("ioe.getMessage()");
            close();
        }
    }

    public Object receive()
    {
        try
        {
            return ois.readObject();
        }
        catch(IOException ioe)
        {
        	System.out.println("ioe.getMessage()");
        }
        catch(ClassNotFoundException cnfe)
        {
            System.out.println("cnfe.getMessage()");
        }
        return null;
    }


    public void close()
    {
        try
        {
            ois.close();
            oos.close();
            socket.close();
        }
        catch(IOException ioe)
        {
            System.out.println("ioe.getMessage()");
        }
    }
}