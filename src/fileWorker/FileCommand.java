package fileWorker;

import java.util.ArrayList;

public class FileCommand implements ICommand
{
    private final Resulter resulter;
    public static String directory = "C:\\Users\\пк\\IdeaProjects\\WebServer\\MyFolder";
    public boolean isRecursive;
    public IExecutable executor;

    public FileCommand(IExecutable executor, boolean isRecursive, Resulter resulter)
    {
        this.executor = executor;
        this.resulter = resulter;
        this.isRecursive = isRecursive;
    }
    @Override
    public void start(String... params)
    {
        FileWorker fileWorker = new FileWorker(directory);
        fileWorker.setIsRecursive(isRecursive);
        ArrayList<String> result;

        if (params.length == 0)
            result = fileWorker.execute(executor);
        else
            result = fileWorker.execute(executor, params[0]);

        resulter.dump(result);
    }
}
