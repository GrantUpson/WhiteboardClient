/*
 * Name: Grant Upson
 * ID: 1225133
 */



import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientConnection {
    private String hostname;
    private int port;
    private String username;

    public ClientConnection(String hostname, int port, String username) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
    }

    public void connect() throws RemoteException, NotBoundException {
        System.setProperty("java.rmi.server.hostname", hostname);
        Registry registry = LocateRegistry.getRegistry("localhost");
        IWhiteboard server = (IWhiteboard) registry.lookup("Whiteboard");
        ClientCallbackInterface client = new Client(username, server);
    }
}
