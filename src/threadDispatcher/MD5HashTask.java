package threadDispatcher;

import fileWorker.CommandFactory;
import java.net.Socket;

public class MD5HashTask extends ThreadedTask
{
    String fileName;
    Socket socket;

    public MD5HashTask(String fileName, Socket socket)
    {
        this.fileName = fileName;
        this.socket = socket;
    }

    @Override
    public void start() {
        CommandFactory.createInstance(CommandFactory.Commands.MD5Hash, socket).start(fileName);
    }
}

