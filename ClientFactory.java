import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Frank on 3/5/14.
 *
 */
public class ClientFactory implements Runnable {

    final private byte choice;          // menu option
    final private String server;        // RMI registry
    public final Thread clientThread;

    private volatile long elapsedTime;  // keep thread response time.


    public ClientFactory(byte choice, String server) {

        this.choice = choice;           // choice byte cannot be changed.
        this.server = server;           // RMI registry to use.


        clientThread = new Thread(this);
        clientThread.start();
    }

    public void run() {

        long timeStart = System.currentTimeMillis();

        try {
            String name = "TaskBuilder";
            Registry registry = LocateRegistry.getRegistry(server);

            TaskBuilder builder = (TaskBuilder) registry.lookup(name);

            // run a new diagnostic task on the RMI server.
            System.out.println("Process output: ");
            Diagnostics task = new Diagnostics(makeCommand(choice));

            // show the program output to the user.
            System.out.println(builder.runTask(task));

        } catch (Exception e) {
            System.err.println("RMIClient exception:");
            e.printStackTrace();
        }

        long timeStop = System.currentTimeMillis();

        elapsedTime = timeStop - timeStart;
    } // end RMI client thread


    private String makeCommand(byte choice) {

        String command;

        switch (choice) {

            case 6:
                command = "ps -ef";
                break;
            case 5:
                command = "who";
                break;
            case 4:
                command = "netstat -n";
                break;
            case 3:
                command = "free -h";
                break;
            case 2:
                command = "uptime";
                break;
            case 1:
                command = "date";
                break;
            default:
                throw new IllegalArgumentException("Not an accepted command.");


        }  // end switch on command


        return command;
    } // end makeCommand

    public long getElapsedTime() {

        return elapsedTime;
    }


} // end ClientFactory class
