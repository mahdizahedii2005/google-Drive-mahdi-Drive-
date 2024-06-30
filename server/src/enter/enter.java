package enter;

import java.io.File;

public abstract class enter extends Thread {
    protected String filePath = "src\\userName.txt";
    protected File file = new File (filePath);
    public abstract byte[] play ();
}
