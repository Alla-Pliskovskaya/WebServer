package threadDispatcher;

import java.util.*;

public class ThreadDispatcher
{
    protected static List<ThreadedTask> threadedTask; //запущенные задачи
    private static volatile ThreadDispatcher instance;
    protected static int maxSize = 1; //максимальное количество потоков, которые могут обрабатывать очередь задач
    protected static int workerCount; //количество активных рабочих потоков в данный момент времени
    private static final ThreadMonitor monitor = new ThreadMonitor();

    private ThreadDispatcher() {
        for (int i = 0; i < maxSize; i++)
            new Thread(new ThreadWorker()).start();
        threadedTask = new ArrayList<>();
        add(new ThreadMonitor());
    }

    public static ThreadDispatcher getInstance()
    {
        if (instance == null) {
            synchronized (ThreadDispatcher.class) {
                if (instance == null)
                    instance = new ThreadDispatcher();
            }
        }
        return instance;
    }

    public void add(ThreadedTask task) //данный метод запускает на выполнение задачу в обход очереди задач
    {
        synchronized (threadedTask) {
            threadedTask.add(task);
        }
        new Thread(task).start();
        monitor.update();
    }

    public void addInQueue(ThreadedTask task) //данный метод либо немедленно запускает задачу на выполнение, либо, если нет свободных потоков, ставит в очередь
    {
        synchronized (threadedTask) {
            threadedTask.add(task);
        }
        GlobalQueue queue = GlobalQueue.getInstance();
        synchronized (ThreadDispatcher.GlobalQueue.getInstance()) {
            queue.add(task);
        }
        task.status = "waiting in Queue";
        monitor.update();
    }

    public void setMaxPoolSize(int max_Size)
    {
        maxSize = max_Size;
        for (int i = 0; i < maxSize; i++)
            new Thread(new ThreadWorker()).start();
    }

    public static ThreadMonitor getMonitor()
    {
        return monitor;
    }

    public static class GlobalQueue
    {
        private static GlobalQueue instance;
        private static Queue<ThreadedTask> globalQueue;

        private GlobalQueue() {
            globalQueue = new LinkedList<>();
        }

        public static GlobalQueue getInstance()
        {
            if (instance == null) {
                instance = new GlobalQueue();
            }
            return instance;
        }

        public void add(ThreadedTask threadedTask)
        {
            globalQueue.add(threadedTask);
        }

        public ThreadedTask remove()
        {
            if (globalQueue.size() != 0)
                return globalQueue.remove();
            else
                return null;
        }
    }
}
