/*
 * Name: Grant Upson
 * ID: 1225133
 */

import javax.swing.*;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ClientConnection {
    private static final String CONNECTION_ERROR = "Connection error: Incorrect hostname or port may have been given or the RMI registry is down";

    private final String registryHostname;
    private final int port;
    private final String username;

    public ClientConnection(String RMIHostname, int port, String username) {
        this.registryHostname = RMIHostname;
        this.port = port;
        this.username = username;
    }

    public void connect() {
        try {
            Registry registry = LocateRegistry.getRegistry(registryHostname, port);
            IWhiteboardServer server = (IWhiteboardServer) registry.lookup("Whiteboard");
            IClientCallback client = new Client(username, server);
        } catch(RemoteException | NotBoundException e) {
            EventQueue.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, CONNECTION_ERROR);
                System.exit(0);
            });
        }
    }
}
