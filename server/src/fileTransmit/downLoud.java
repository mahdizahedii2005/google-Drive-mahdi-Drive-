package fileTransmit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class downLoud {
    String user;

    public downLoud(String user) {
        this.user = user;
    }

    public File findFile(int i) {
        File file = new File("src\\userFile\\" + user);
        int k = 1;
        for (File file1 : file.listFiles()) {
            if (k == i) return file1;
            else k++;
        }
        return null;
    }

    public String getLF() {
        String res = "";
        try {
            File D = new File("src\\userFile\\" + user);
            int i = 1;
            for (File d : D.listFiles()) {
                res += i + " " + d.getName() + "\n";
                i++;
            }
        } catch (Exception e) {
        }
        return res;
    }
}
