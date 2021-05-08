package threadDispatcher;

import fileWorker.FileCommand;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPWebTask extends ThreadedTask
{
    private final Socket socket;

    public HTTPWebTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void start() {
        try {
            while (true) {
                StringBuilder builder = new StringBuilder();
                byte[] data = new byte[1024];
                int bytes = socket.getInputStream().read(data);
                builder.append(new String(data, StandardCharsets.UTF_8), 0, bytes);
                String command = getCommand(builder.toString().toLowerCase());

                if (command.contains("."))
                {
                    byte[] file = Files.readAllBytes(Path.of(FileCommand.directory + "\\" + command));
                    String response = "HTTP/1.1 200 OK\r\n" +
                            "Server: WebServer/2020-04-20\r\n" +
                            "Content-Type: file\r\n" +
                            "Content-Length: " + file.length + "\r\n" +
                            "Connection: close\r\n\r\n";
                    try {
                        socket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
                        socket.getOutputStream().write(file);
                    } catch (IOException ignored) {

                    }
                } else {
                    String HTMLString = "<!DOCTYPE html>"
                            + "<html>"
                            + "<head>"
                            + "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">"
                            + "</head>"
                            + "<body>"
                            + "<table><tr><td><h1>Hi, I am your web file explorer!</h1></tr>"
                            + "</table>"
                            + pathToHtml(FileCommand.directory + "\\" + command)
                            + "</body>"
                            + "</html>";
                    writeResponse(HTMLString);
                }
            }
        } catch (Exception ignored) {
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {ex.printStackTrace(); }
        }
    }

    private void writeResponse(String s)
    {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: WebServer/2020-04-20\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + s.length() + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + s;
        try {
            socket.getOutputStream().write(result.getBytes(StandardCharsets.UTF_8));
            socket.getOutputStream().flush();
        } catch (IOException ignored) {
        }
    }

    private String pathToHtml(String path)
    {
        StringBuilder result = new StringBuilder("<a href=\"/\">/</a>");
        File directory = new File(path);
        if (!directory.exists())
            return result.toString();
        for (File dir : getDirectories(directory))
        {
            String name = dir.getName().replace(FileCommand.directory, "").replace('\\','/');
            result.append("<p><a href=\"").append(name).append("/\">").append(dir.getName()).append("</a></p>");
        }

        for (File file : Objects.requireNonNull(directory.listFiles()))
        {
            if (!file.isDirectory()) {
                String name = file.getName().replace(FileCommand.directory, "").replace('\\', '/');
                result.append("<p><a href=\"").append(name).append("\" download>").append(file.getName()).append("\n")
                        .append("\t<button> Download file </button>\n")
                        .append("</a><p>");
            }
        }
        return result.toString();
    }

    private File[] getDirectories(File file)
    {
        ArrayList<File> directories = new ArrayList<>();
        for (File s : Objects.requireNonNull(file.listFiles()))
        {
            if (s.isDirectory())
                directories.add(s);
        }
        return directories.toArray(new File[0]);
    }

    private String getCommand(String response)
    {
        String regex = "get (.*) http/1.1";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(response);
        String str = "";
        while (matcher.find()) {
            str = response.substring(matcher.start(), matcher.end());
        }
        ArrayList<String> arr = new ArrayList<>(Arrays.asList(str.split(" ")));
        arr.remove(0);
        arr.remove(arr.size()-1);
        StringBuilder p = new StringBuilder();
        for (String s : arr)
        {
            p.append(s).append(" ");
        }
        str = p.toString().trim();
        return(str);
    }
}
