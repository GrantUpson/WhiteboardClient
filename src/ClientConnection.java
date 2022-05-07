import java.rmi.RemoteException;

public class ClientConnection implements IClientConnection {
    private String username;

    public ClientConnection(String message) {
        this.username = message;
    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }
}
