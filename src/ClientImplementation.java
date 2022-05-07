import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImplementation extends UnicastRemoteObject implements ClientCallbackInterface, Serializable {
    private String hostname;
    private int port;
    private String username;

    public ClientImplementation(String hostname, int port, String username) throws RemoteException {
        super();
        this.hostname = hostname;
        this.port = port;
        this.username = username;
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }

    @Override
    public void message(String message) throws RemoteException {
        System.out.println(message);
    }
}
