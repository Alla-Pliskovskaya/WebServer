package threadDispatcher;

import fileWorker.*;
import java.net.Socket;

public class SizeTask extends ThreadedTask
{
    String fileName;
    Socket socket;

    public SizeTask(String fileName, Socket socket)
    {
        this.fileName = fileName;
        this.socket = socket;
    }

    @Override
    public void start() {
        CommandFactory.createInstance(CommandFactory.Commands.Size, socket).start(fileName);
    }
}
