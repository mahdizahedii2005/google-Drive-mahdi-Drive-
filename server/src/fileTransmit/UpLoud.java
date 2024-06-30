package fileTransmit;

import java.io.File;
import java.io.IOException;

public class UpLoud {
    private final String fileName;
    private final String userName;
    private String filePath;

    public UpLoud(String fileName, String userName) {
        this.fileName = fileName;
        this.userName = userName;
        creatFile(userName, fileName);
    }

    private void creatFile(String user, String fileNAme) {
        if (!new File("src\\userFile\\" + user).exists()){
            new File("src\\userFile\\" + user).mkdir();
        }
        File fi = new File("src\\userFile\\" + user + "\\" + fileName);
        try {
            System.out.println("creating the file ==> " + fi.createNewFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        filePath = fi.getPath();
        System.out.println("file path is : " + filePath);
    }

    public String getFilePath() {
        return filePath;
    }
}
