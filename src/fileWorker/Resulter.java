package fileWorker;

import java.util.*;

public abstract class Resulter
{
    protected FileWorker fileWorker;
    protected ArrayList<String> result;

    public Resulter(FileWorker fileWorker, IExecutable ex)
    {
        this.fileWorker = fileWorker;
        result = fileWorker.execute(ex);
    }

    public Resulter()
    {

    }

    abstract void dump(List<String> list);
}
