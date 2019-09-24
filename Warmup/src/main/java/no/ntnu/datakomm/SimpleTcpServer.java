package no.ntnu.datakomm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

/**
 * A Simple TCP client, used as a warm-up exercise for assignment A4.
 */
public class SimpleTcpServer {

    private static final int PORT = 1025;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        SimpleTcpServer server = new SimpleTcpServer();
        log("Simple TCP server starting");
        server.run();
        log("ERROR: the server should never go out of the run() method! After handling one client");
    }

    public void run()
    {

        try
        {
            ServerSocket welcomingSocket = new ServerSocket(PORT);
            System.out.println("Started the server on port " + PORT);

            boolean mustRun = true;

            while(mustRun)
            {
                Socket clientSocket = welcomingSocket.accept();

                InputStreamReader reader = new InputStreamReader(clientSocket.getInputStream());
                BufferedReader bufReader = new BufferedReader(reader);

                String clientInput = bufReader.readLine();

                System.out.println("Client sent " + clientInput);

                String response = clientInput + " was received from the client.";

                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                writer.println(response);

                clientSocket.close();
            }


        } catch (IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }


        // TODO - implement the logic of the server, according to the protocol.
        // Take a look at the tutorial to understand the basic blocks: creating a listening socket,
        // accepting the next client connection, sending and receiving messages and closing the connection
    }

    /**
     * Log a message to the system console.
     *
     * @param message The message to be logged (printed).
     */
    private static void log(String message)
    {
        System.out.println(message);
    }
}
