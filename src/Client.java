/*
 * Name: Grant Upson
 * ID: 1225133
 */

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client {

    public Client() {

    }

    public void connect() throws RemoteException, NotBoundException {
        IClientConnection client = new ClientConnection("Grant");
        UnicastRemoteObject.exportObject(client, 0);
        Registry registry = LocateRegistry.getRegistry("localhost");

        //Retrieve the stub/proxy for the remote math object from the registry
        IWhiteboard whiteboard = (IWhiteboard) registry.lookup("Whiteboard");

        //System.out.println(whiteboard.getUsernames());
        whiteboard.sendMessage("Hello server!");
        whiteboard.login(client);

        while(true) {
            System.out.println(whiteboard.messagesSent());
        }
    }
}
