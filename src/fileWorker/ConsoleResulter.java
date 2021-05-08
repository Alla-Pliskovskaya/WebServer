package fileWorker;

import java.util.List;

public class ConsoleResulter extends Resulter {

    public ConsoleResulter(FileWorker fileWorker, IExecutable ex) {
        super(fileWorker, ex);
    }

    @Override
    public void dump(List<String> list) {
        for (String res : list)
            System.out.println(res);
    }
}
