import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Client extends UnicastRemoteObject implements ClientCallbackInterface, Serializable {
    private static final String KICKED_MESSAGE = "You have been kicked from the whiteboard server, terminating.";
    private final String username;
    private final GUI gui;

    public Client(String username, IWhiteboard server) throws RemoteException {
        super();
        this.username = username;
        EventQueue.invokeLater(gui = new GUI(server));
        server.register(this);
    }

    @Override
    public void syncDrawables(List<IDrawable> drawables) throws RemoteException {
        gui.syncDrawablesOnConnect(drawables);
    }

    @Override
    public void sendDrawable(IDrawable drawable) throws RemoteException {
        gui.addDrawableFromServer(drawable);
    }

    @Override
    public void kickFromWhiteboard() throws RemoteException {
        JOptionPane.showMessageDialog(null, KICKED_MESSAGE);
        System.exit(0);
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }

    @Override
    public void sendChatMessage(String message) throws RemoteException {
        //TODO
    }
}
