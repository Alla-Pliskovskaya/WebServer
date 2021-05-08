package fileWorker;

import java.io.*;
import java.security.*;

public class MD5Executor implements IExecutable {

    @Override
    public String process(File file)
    {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ignored) {
        }

        byte[] dataBytes = new byte[1024];

        int numRead = 0;
        while (true) {
            try {
                assert fis != null;
                if ((numRead = fis.read(dataBytes)) == -1)
                    break;
            } catch (IOException ignored) {
            }

            assert md != null;
            md.update(dataBytes, 0, numRead);
        }

        assert md != null;
        byte[] mdbytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte mdbyte : mdbytes) {
            sb.append(Integer.toString((mdbyte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
