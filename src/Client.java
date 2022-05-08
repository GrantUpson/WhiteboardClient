/*
 * Name: Grant Upson
 * ID: 1225133
 */


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class Client {
    private String hostname;
    private int port;
    private String username;

    public Client(String hostname, int port, String username) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
    }

    public void connect() throws RemoteException, NotBoundException {
        System.setProperty("java.rmi.server.hostname", hostname);
        Registry registry = LocateRegistry.getRegistry("localhost");
        IWhiteboard whiteboard = (IWhiteboard) registry.lookup("Whiteboard");
        ClientCallbackInterface client = new ClientImplementation(hostname, port, username);
        whiteboard.register(client);

        List<ClientCallbackInterface> list = whiteboard.getClients();

        whiteboard.broadcastMessage("This is a broadcast message.");

        whiteboard.unregister(client);
    }
}
