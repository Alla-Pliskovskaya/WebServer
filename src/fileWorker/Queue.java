package fileWorker;

import java.util.*;

public class Queue
{
    private static Queue instance;
    private static java.util.Queue<ICommand> queue;

    private Queue() {
        queue = new LinkedList<>();
    }

    public static Queue getInstance()
    {
        if (instance == null) {
            instance = new Queue();
        }
        return instance;
    }

    public void enqueue(ICommand... commands)
    {
        queue.addAll(Arrays.asList(commands));
    }

    public void dequeue_start()
    {
        queue.poll().start();
    }
}
