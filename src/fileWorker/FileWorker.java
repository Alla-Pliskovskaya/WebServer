package fileWorker;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileWorker
{
    private final String path;
    private Boolean isRecursive;

    public FileWorker(String path)
    {
        this.path = path;
    }

    public void setIsRecursive(Boolean isRecursive)
    {
        this.isRecursive = isRecursive;
    }

    public ArrayList<String> execute(IExecutable ex)
    {
        ArrayList<String> result = new ArrayList<>();
        File file_or_dir = new File(path);
        File[] files = file_or_dir.listFiles();

        if (isRecursive) {
            try {
                Files.walk(Paths.get(path))
                        .map(Path::toFile)
                        .forEach(f -> {
                            if (!f.isDirectory())
                                result.add(ex.process(f));
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (file_or_dir.isDirectory()) {
            for (File file : files)
                if (file.isFile()) {
                    result.add(ex.process(file));
                }
            return result;
        }
        return result;
    }

    public ArrayList<String> execute(IExecutable ex, String filename)
    {
        String result = null;
        if (new File(path).isDirectory())
            result = ex.process(new File(path + "\\" + filename));
        ArrayList<String> arr = new ArrayList<>();
        arr.add(result);
        return arr;
    }
}
