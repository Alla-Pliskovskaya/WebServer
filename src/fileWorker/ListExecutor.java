package fileWorker;

import java.io.File;

public class ListExecutor implements IExecutable
{
    @Override
    public String process(File f) {
        return f.getName();
    }
}
