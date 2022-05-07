import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public Client() {

    }

    public void connect() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost");

        //Retrieve the stub/proxy for the remote math object from the registry
        IWhiteboard whiteboard = (IWhiteboard) registry.lookup("WhiteboardServer");

        System.out.println(whiteboard.getUsernames());
        whiteboard.sendMessage("Hello server!");
    }

}
