import java.io.*;

/**
 * Created by Frank on 3/5/14.
 *
 */
public class Diagnostics implements Task<String>, Serializable {

    // remember to generate a new serial UID! each time we change something here,
    // it has to be incremented or the application breaks.
    //
    private static final long serialVersionUID = 114L;

    private String command;

    // accept any command.
    public Diagnostics(String command) {

        this.command = command;

    }

    public String execute() {

        try {
        return runProcess(command);
        } catch (IOException e) {
            e.printStackTrace();
            return "Program failed to execute.";
        }
    }

    public String runProcess(String command) throws IOException  {

        char[] chars = new char[32768];

        // ProcessBuilder needs an array or set of arguments.
        ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
        Process process = processBuilder.start();

        StringBuilder commandOutput = new StringBuilder();
        Reader reader = new InputStreamReader(process.getInputStream());

         while (true) {

            int length;
            try {

                length = reader.read(chars);

                if (length == -1)
                    break;

            } catch (EOFException e) {
                break;
            }


            commandOutput.append(chars, 0, length);

        }

        // turn the command output into a string to be sent back to the client.
        reader.close();
        return commandOutput.toString();

    }

}
