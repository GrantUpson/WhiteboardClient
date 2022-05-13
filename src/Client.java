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


public class Client extends UnicastRemoteObject implements IClientCallback, Serializable {
    private static final String USERNAME_ALREADY_EXISTS_MESSAGE = "A user with this username is already logged in.";
    private final String username;
    private final GUI gui;

    public Client(String username, IWhiteboardServer server) throws RemoteException {
        super();
        this.username = username;
        EventQueue.invokeLater(gui = new GUI(server, this));

        //Will return false if a user with the clients' username already exists on the server.
        if(!server.connect(this)) {
            onForcedDisconnect(USERNAME_ALREADY_EXISTS_MESSAGE);
        }
    }

    @Override
    public void sendDrawable(IDrawable drawable) throws RemoteException {
        gui.addDrawableFromServer(drawable);
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }

    @Override //Called when the server accepts the users request to join the whiteboard.
    public void onConnectionAccepted() throws RemoteException {
        gui.enableServerCommunication();
    }

    @Override
    public void synchronizeCurrentUsers(List<String> users) throws RemoteException {
        gui.updateConnectedUsers(users);
    }

    @Override
    public void synchronizeDrawables(List<IDrawable> drawables) throws RemoteException {
        gui.updateDrawables(drawables);
    }

    @Override
    public void sendChatMessage(String message) throws RemoteException {
        gui.addChatMessage(message);
    }


    @Override //Called when the server shuts down or kicks the user. Goes in new thread, so it doesn't block the server.
    public void onForcedDisconnect(String message) throws RemoteException {
        new Thread(() -> {
            JOptionPane.showMessageDialog(null, message);
            System.exit(0);
        }) {
        }.start();
    }
}
