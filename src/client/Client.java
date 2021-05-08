package client;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Client
{
    public static void main(String[] args)
    {
        int port = 8081;
        Socket clientSocket;
        try {
            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port));
            String message = "";

            while (!message.equals("exit"))
            {
                System.out.print("Input a command to server: ");
                Scanner scanner = new Scanner(System.in);
                message = scanner.nextLine();
                byte[] data = message.getBytes(StandardCharsets.UTF_8);
                clientSocket.getOutputStream().write(data);

                StringBuilder builder = new StringBuilder();
                data = new byte[1024];
                int bytes = clientSocket.getInputStream().read(data);
                builder.append(new String(data, StandardCharsets.UTF_8), 0, bytes);

                System.out.println("Server's answer: " + builder.toString());
            }
            clientSocket.shutdownInput();
            clientSocket.shutdownOutput();
            clientSocket.close();
        } catch (IOException ignored) {
        }
    }
}
