package threadDispatcher;

import server.*;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class WebTask extends ThreadedTask
{
    private final Socket socket;

    public WebTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void start()
    {
        try {
            while (true) {
                StringBuilder builder = new StringBuilder();
                byte[] data = new byte[1024];
                int bytes = socket.getInputStream().read(data);
                builder.append(new String(data, StandardCharsets.UTF_8), 0, bytes);
                String[] command = builder.toString().toLowerCase().split(" ");

                switch (command[0]) {
                    case "list":
                        ThreadDispatcher.getInstance().addInQueue(new ListTask(socket));
                        break;
                    case "hash":
                        if (command.length > 1) {
                            ThreadDispatcher.getInstance().addInQueue(new MD5HashTask(command[1], socket));
                            break;
                        }
                    case "size":
                        if (command.length > 1) {
                            ThreadDispatcher.getInstance().addInQueue(new SizeTask(command[1], socket));
                            break;
                        }
                    case "status":
                        String s = Server.getHTTPServerStatus().toString();
                        socket.getOutputStream().write(s.getBytes(StandardCharsets.UTF_8));
                        break;
                    case "stop":
                        s = "Server stopped";
                        Server.setHTTPServerStatus(Server.HttpServerStatus.Stopped);
                        socket.getOutputStream().write(s.getBytes(StandardCharsets.UTF_8));
                        break;
                    case "start":
                        s = "Server started";
                        Server.CreateHTTPServer();
                        Server.setHTTPServerStatus(Server.HttpServerStatus.Active);
                        socket.getOutputStream().write(s.getBytes(StandardCharsets.UTF_8));
                        break;
                    case "exit":
                        s = "Goodbye!";
                        socket.getOutputStream().write(s.getBytes(StandardCharsets.UTF_8));
                        socket.shutdownOutput();
                        socket.shutdownInput();
                        break;
                    default:
                        s = "Error: Command not found";
                        socket.getOutputStream().write(s.getBytes(StandardCharsets.UTF_8));
                        break;
                }
            }
        } catch (IOException ignored) {
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {ex.printStackTrace(); }
        }
    }
}
