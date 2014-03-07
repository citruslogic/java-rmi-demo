import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Frank on 3/5/14.
 * RMI Client for the RMI/RPC project.
 *
 */
public class RMIClient {



    public static void main(String[] args) throws InterruptedException, IOException {

        byte choice;
        short nclients;

        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        if (args.length < 1) {
            // tell the user he did not give an ip address.
            System.err.println("No server to connect to, quitting.");
            System.exit(1);
        }
        /* first, issue the menu to the user.
           same as before.
         */
        while (true) {

            System.out.println("\nRMI Diagnostics Client\n"
                    + " connecting to " + args[0] + "\n"
                    + "\nEnter your selection (1-7):\n"
            + "===================================\n"
            + "1. Host Current Date and Time\n"
            + "2. Host uptime\n"
            + "3. Host memory use\n"
            + "4. Host Netstat\n"
            + "5. Host current users\n"
            + "6. Host running processes\n"
            + "7. Quit\n"
            + "===================================\n"
            + "\n");

            Scanner kb = new Scanner(System.in);
            // get the first byte.
            try {
                choice = kb.nextByte();

                if (choice == 7) {
                    System.out.println("Exiting to console.");
                    System.exit(0);
                }

                if (choice > 7 | choice < 1) {
                    System.err.println("\nMenu option is out of bounds. Try again with\n" +
                                       "any menu option, 1 through 7.\n");
                    continue;
                }
            } catch (InputMismatchException e) {
                System.err.println("Only single digit numbers are accepted at this time.");
                continue;
            }

            System.out.print("Number of client threads to make? ");

            // get the number of client threads to spawn for testing.
            try {
                nclients = kb.nextShort();

                if (nclients < 1) {
                    System.err.println("Less than 1 client is not allowed for this test.");
                    continue;     // make the user start over from the menu.
                }
            }  catch (InputMismatchException e) {
                // the user should try again from the menu.
                System.err.println("\nNot integer input! Try again with a number instead.\n");
                continue;
            }  // finished getting the number of clients to make.

            // make an array for the clients, so their average thread times can be taken.
            ClientFactory[] theClients = new ClientFactory[nclients];

            // fill it and run the threads.
            for (int i = 0; i < nclients; i++)
                theClients[i] = new ClientFactory(choice, args[0]);


            // force main() to wait until all threads are finished.

            for (ClientFactory clients : theClients) {
                clients.clientThread.join();
            }
            System.out.println("RMI Thread Processing Test finished for " + nclients + " threads.");


            writeThreadLogFile(theClients, nclients, choice);

        } // end while
    }   // end main

    /*
        compute the mean time among all of the clients
        and return the average to the calling method.
     */
    public static long computeMeanTime(ClientFactory[] theClients, short nclients) {

        long averageElapsedTime = 0L;

        for (ClientFactory clients : theClients)
            averageElapsedTime += clients.getElapsedTime();

        return averageElapsedTime / nclients;
    }  // end computeMeanTime

    /*

        write the log file for every array of client threads.
     */
    public static void writeThreadLogFile(ClientFactory[] clients, short nclients, byte choice) throws IOException {

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();

        PrintWriter logfile = new PrintWriter(new BufferedWriter(
                new FileWriter("thread-elapsed-times.txt", true)));

        logfile.println(dateFormat.format(calendar.getTime()) + " [total clients " + nclients + "]\n"
                + "Run option: " + choice
                + "\tThread time (ms): " + "\t" + computeMeanTime(clients, nclients)
                + "\n");

        // finished writing to file.
        logfile.close();

    } // end writeThreadLogFile


}  // end RMIClient class
