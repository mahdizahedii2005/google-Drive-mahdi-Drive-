package enter;
import server.*;
import java.io.*;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.Scanner;

public class signUp extends enter {
    private String userName;
    private String Password;
    private String message = null;
    private String MessageLock = "lock";
    public signUp( String userName,String password) {
        this.Password = password;
        this.userName = userName;
        System.out.println("user name is : " + userName);
        System.out.println("password is : " + password);
    }

    @Override
    public void run() {
        synchronized (MessageLock) {
            System.out.println("user name is :" + userName);
            if (!repetedUserName(userName)) {
                add(userName, Password);
            } else {
                message = "this user name is already used";
            }
        }
    }


    @Override
    public byte[] play() {
        start();
        while (true) {
            synchronized (MessageLock) {
                if (message != null) break;
            }
        }
        System.out.println("im about to leave");
        return message.getBytes();
    }

    private void add(String userName, String password) {
        try {
            Scanner sc = new Scanner(file);
            String total = "";
            while (sc.hasNextLine()) {
                total += sc.nextLine() + "\n";
            }
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(total);
            printWriter.println(userName + '|' + StringToHash.convert(password) + "|");
            printWriter.flush();
            printWriter.close();
            message = "successful";
            System.out.println("message set :" + message);
        } catch (FileNotFoundException e) {
            System.out.println("cant find file");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized boolean repetedUserName(String userName) {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.isEmpty())continue;
            if (server.getFirstWord(line).getResult().equals(userName)){
                System.out.println("i found this tekrari");
                return true;
            }
        }
        System.out.println("found this onTekrari");
        return false;
    }
}
