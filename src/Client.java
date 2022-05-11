/*
 * Name: Grant Upson
 * ID: 1225133
 */

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;


public class Client extends UnicastRemoteObject implements ClientCallbackInterface, Serializable {
    private final String username;
    private final GUI gui;

    public Client(String username, IWhiteboard server) throws RemoteException {
        super();
        this.username = username;
        EventQueue.invokeLater(gui = new GUI(server, this));
        server.register(this);
    }

    @Override
    public void sendDrawable(IDrawable drawable) throws RemoteException {
        gui.addDrawableFromServer(drawable);
    }

    @Override
    public void onKick(String message) throws RemoteException {
        JOptionPane.showMessageDialog(null, message);
        System.exit(0);
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }

    @Override
    public void onServerShutdown(String message) throws RemoteException {
        JOptionPane.showMessageDialog(null, message);
        System.exit(0);
    }

    @Override
    public void onConnectionAccepted(List<IDrawable> drawables, List<String> users) throws RemoteException {
        gui.syncDrawables(drawables);
        gui.syncConnectedUsers(users);
        gui.enableServerCommunication();
    }

    @Override
    public void sendChatMessage(String message) throws RemoteException {
        gui.addChatMessage(message);
    }
}
