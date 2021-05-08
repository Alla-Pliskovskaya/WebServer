package fileWorker;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class WebResulter extends Resulter
{
    private final Socket Socket;

    public WebResulter(Socket socket)
    {
        Socket = socket;
    }

    @Override
    void dump(List<String> list)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : list)
        {
            stringBuilder.append(str);
            stringBuilder.append(" ");
        }

        String result = stringBuilder.toString();

        try {
            Socket.getOutputStream().write(result.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ignored) {
        }
    }
}
