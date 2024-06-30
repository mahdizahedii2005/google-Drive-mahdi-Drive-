package backEnd.control.fileTransmit;

import java.io.File;
import java.io.IOException;

public class UoLoud {
    private final String fileName;
    private String filePath;

    public UoLoud(String fileName) {
        this.fileName = fileName;
        creatFile();
    }

    private void creatFile() {
        if (!new File("src\\backEnd\\control\\fileTransmit\\files").exists()) {
            System.out.println("makePath" + new File("src\\backEnd\\control\\fileTransmit\\files").mkdirs());
        }else {
            System.out.println("already exist");
        }
        File fi = new File("src\\backEnd\\control\\fileTransmit\\files\\" + fileName);
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
