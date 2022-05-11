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
    private static final String USERNAME_ALREADY_EXISTS_MESSAGE = "A user with this username is already logged in.";
    private final String username;
    private final GUI gui;

    public Client(String username, IWhiteboard server) throws RemoteException {
        super();
        this.username = username;
        EventQueue.invokeLater(gui = new GUI(server, this));

        if(!server.register(this)) {
            JOptionPane.showMessageDialog(null, USERNAME_ALREADY_EXISTS_MESSAGE);
            System.exit(0);
        }
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
    public void onConnectionAccepted() throws RemoteException {
        gui.enableServerCommunication();
    }

    @Override
    public void synchronizeCurrentUsers(List<String> users) throws RemoteException {
        gui.syncConnectedUsers(users);
    }

    @Override
    public void synchronizeDrawables(List<IDrawable> drawables) throws RemoteException {
        gui.syncDrawables(drawables);
    }

    @Override
    public void sendChatMessage(String message) throws RemoteException {
        gui.addChatMessage(message);
    }
}
