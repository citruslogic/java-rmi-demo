import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Frank on 3/5/14.
 *
 */

// for use with the RMIServer class.
public interface TaskBuilder extends Remote {

    <T> T runTask(Task<T> t) throws RemoteException;
}
