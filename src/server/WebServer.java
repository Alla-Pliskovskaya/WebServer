package server;

import threadDispatcher.*;
import java.io.*;
import java.net.*;

public class WebServer implements Runnable
{
    private final int port;
    private ServerSocket serverSocket;
    public boolean isStopped = false;

    public WebServer(int port) {
        this.port = port;
    }

    @Override
    public void run()
    {
        try {
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName("127.0.0.1")); //параметр backlog определяет количество ожидающих соединений, которые будут храниться в очереди
        } catch (IOException ignored) {
        }

        ThreadDispatcher td = ThreadDispatcher.getInstance();
        try {
            while (true) {
                while (!isStopped) {
                    Socket client = serverSocket.accept(); //блокировка потока, пока не появится входящее соединение
                    if (port == 8081) {
                        td.add(new WebTask(client));
                    } else {
                        td.addInQueue(new HTTPWebTask(client));
                    }
                } Thread.sleep(1000);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
