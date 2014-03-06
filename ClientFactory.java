import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * Created by Frank on 3/5/14.
 *
 */
public class ClientFactory implements Runnable {

    final private byte choice;          // menu option
    final private String server;    // RMI registry
    public final Thread clientThread;

    private volatile long elapsedTime;  // keep thread response time.

    public ClientFactory(byte choice, String server) {

        this.choice = choice;           // choice byte cannot be changed.
        this.server = server;   // RMI registry to use.


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


    public String makeCommand(byte choice) throws IOException {

        String fullcommand, args;


        switch (choice) {

            case 6:
                fullcommand = "ps";
                break;
            case 5:
                fullcommand = "who";
                break;
            case 4:
                fullcommand = "netstat";
                break;
            case 3:
                fullcommand = "free";
                break;
            case 2:
                fullcommand = "uptime";
                break;
            case 1:
                fullcommand = "date";
                break;
            default:
                throw new IllegalArgumentException("Not an accepted command.");


        }  // end switch on command

        System.out.println("Add any extra arguments for " + fullcommand + ": ");

        Scanner getArgs = new Scanner(System.in);

        // get the list of arguments from the user.
        args = getArgs.nextLine();


        return fullcommand + " " + args.trim();
    } // end makeCommand

    public long getElapsedTime() {

        return elapsedTime;
    }

} // end ClientFactory class
