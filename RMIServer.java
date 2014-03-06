import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Frank on 3/5/14.
 * RMI Server for the RMI/RPC project.
 *
 */
public class RMIServer implements TaskBuilder {


    public <T> T runTask(Task<T> t) {

        return t.execute();
    }

    // http://docs.oracle.com/javase/tutorial/rmi/implementing.html
    public static void main(String[] args) {

        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            String name = "TaskBuilder";
            TaskBuilder builder = new RMIServer();
            TaskBuilder stub = (TaskBuilder) UnicastRemoteObject.exportObject(builder, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);

            System.out.println(name + " bound.");


        } catch (RemoteException e) {
            System.err.println("RMIServer exception:");
            e.printStackTrace();

        }

    } // end main

}  // end RMIServer class
