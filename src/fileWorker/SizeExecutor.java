package fileWorker;

import java.io.File;

public class SizeExecutor implements IExecutable
{
    @Override
    public String process(File f) {
        return String.valueOf(f.length());
    }
}
