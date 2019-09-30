package no.ntnu.datakomm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread
{
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        InputStreamReader reader = null;
        try
        {
            reader = new InputStreamReader(clientSocket.getInputStream());

        BufferedReader bufReader = new BufferedReader(reader);

        String clientInput = bufReader.readLine();

        System.out.println("Client sent " + clientInput);

        String response = clientInput + " was received from the client.";

        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
        writer.println(response);

        clientSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
