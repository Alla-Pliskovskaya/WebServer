package fileWorker;

import java.net.Socket;

public class CommandFactory
{
    public enum Commands
    {
        MD5Hash,
        Size,
        List,
    }

    public static ICommand createInstance(Commands command, Socket socket) {
        return switch (command) {
            case MD5Hash -> new FileCommand(new MD5Executor(), false, new WebResulter(socket));
            case Size -> new FileCommand(new SizeExecutor(), false, new WebResulter(socket));
            case List -> new FileCommand(new ListExecutor(), false, new WebResulter(socket));
        };
    }
}
