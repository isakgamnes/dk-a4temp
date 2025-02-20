package no.ntnu.datakomm;

import java.io.*;
import java.net.Socket;
import java.net.InetSocketAddress;

/**
 * A Simple TCP client, used as a warm-up exercise for assignment A4.
 */
public class SimpleTcpClient
{

    Socket socket = new Socket();
    // Remote host where the server will be running
    private static final String HOST = "127.0.0.1";
    // TCP port
    private static final int PORT = 1025;

    private PrintWriter writer;

    /**
     * Run the TCP Client.
     *
     * @param args Command line arguments. Not used.
     */
    public static void main(String[] args)
    {
        SimpleTcpClient client = new SimpleTcpClient();
        try
        {
            client.run();
        } catch (InterruptedException e)
        {
            log("Client interrupted");
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Run the TCP Client application. The logic is already implemented, no need to change anything in this method.
     * You can experiment, of course.
     *
     * @throws InterruptedException The method sleeps to simulate long client-server conversation.
     *                              This exception is thrown if the execution is interrupted halfway.
     */
    public void run() throws InterruptedException
    {
        log("Simple TCP client started");

        if (connectToServer(HOST, PORT))
        {
            log("Connection to the server established");
            int a = (int) (1 + Math.random() * 10);
            int b = (int) (1 + Math.random() * 10);
            String request = a + "+" + b;
            if (sendRequestToServer(request))
            {
                log("Sent " + request + " to server");
                String response = readResponseFromServer();
                if (response != null)
                {
                    log("Server responded with: " + response);
                    int secondsToSleep = 2 + (int) (Math.random() * 5);
                    log("Sleeping " + secondsToSleep + " seconds to allow simulate long client-server connection...");
                    Thread.sleep(secondsToSleep * 1000);
                    request = "bla+bla";
                    if (sendRequestToServer(request))
                    {
                        log("Sent " + request + " to server");
                        response = readResponseFromServer();
                        if (response != null)
                        {
                            log("Server responded with: " + response);
                            if (sendRequestToServer("game over") && closeConnection())
                            {
                                log("Game over, connection closed");
                                // When the connection is closed, try to send one more message. It should fail.
                                if (!sendRequestToServer("2+2"))
                                {
                                    log("Sending another message after closing the connection failed as expected");
                                } else
                                {
                                    log("ERROR: sending a message after closing the connection did not fail!");
                                }
                            } else
                            {
                                log("ERROR: Failed to stop conversation");
                            }
                        } else
                        {
                            log("ERROR: Failed to receive server's response!");
                        }
                    } else
                    {
                        log("ERROR: Failed to send invalid message to server!");
                    }
                } else
                {
                    log("ERROR: Failed to receive server's response!");
                }
            } else
            {
                log("ERROR: Failed to send valid message to server!");
            }
        } else
        {
            log("ERROR: Failed to connect to the server");
        }

        log("Simple TCP client finished");
    }

    /**
     * Close the TCP connection to the remote server.
     *
     * @return True on success, false otherwise
     */
    private boolean closeConnection()
    {
        boolean connectionClosed;
        try
        {
            socket.close();
            connectionClosed = true;
        } catch (IOException e)
        {
            System.out.println("ERROR: " + e.getMessage());
            connectionClosed = false;
        }
        return connectionClosed;
    }

    /**
     * Try to establish TCP connection to the server (the three-way handshake).
     *
     * @param host The remote host to connect to. Can be domain (localhost, ntnu.no, etc), or IP address
     * @param port TCP port to use
     * @return True when connection established, false otherwise
     */
    private boolean connectToServer(String host, int port)
    {
        boolean connected;

        InetSocketAddress serverAddress = new InetSocketAddress(host, port);

        try
        {
            socket.connect(serverAddress);
            connected = true;
        } catch (IOException e)
        {
            System.out.println("Error: " + e.getMessage());
            connected = false;
        }


        // TODO - implement this method
        // Remember to catch all possible exceptions that the Socket class can throw.
        return connected;
    }

    /**
     * Send a request message to the server (newline will be added automatically)
     *
     * @param request The request message to send. Do NOT include the newline in the message!
     * @return True when message successfully sent, false on error.
     */
    private boolean sendRequestToServer(String request)
    {
        boolean result = false;
        try
        {
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(request);
            result = true;
        } catch (IOException e)
        {
            System.out.println("ERROR :" + e.getMessage());
        }

        // TODO - implement this method
        // Hint: What can go wrong? Several things:
        // * Connection closed by remote host (server shutdown)
        // * Internet connection lost, timeout in transmission
        // * Connection not opened.
        // * What is the request is null or empty?
        return result;
    }

    /**
     * Wait for one response from the remote server.
     *
     * @return The response received from the server, null on error. The newline character is stripped away
     * (not included in the returned value).
     */
    private String readResponseFromServer()
    {
        String response = null;
        try
        {
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            response = reader.readLine();
        }
        catch(IOException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        // TODO - implement this method
        // Similarly to other methods, exception can happen while trying to read the input stream of the TCP Socket
        return response;
    }

    /**
     * Log a message to the system console.
     *
     * @param message The message to be logged (printed).
     */
    private static void log(String message)
    {
        String threadId = "THREAD #" + Thread.currentThread().getId() + ": ";
        System.out.println(threadId + message);
    }
}
