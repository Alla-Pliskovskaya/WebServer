package threadDispatcher;

import fileWorker.*;
import java.net.Socket;

public class ListTask extends ThreadedTask
{
    private final Socket socket;

    public ListTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void start() {
        CommandFactory.createInstance(CommandFactory.Commands.List, socket).start();
    }
}
