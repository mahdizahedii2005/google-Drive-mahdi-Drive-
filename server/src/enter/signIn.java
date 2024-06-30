package enter;

import server.*;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class signIn extends enter {
    private String Password;
    private String userName;
    private String message = null;

    public signIn(String userName,String password) {
        this.Password = password;
        this.userName = userName;
    }

    @Override
    public byte[] play() {
        start();
        System.out.println("im waiting server");
        try {
            synchronized (server.getServer()) {
                server.getServer().wait();
                System.out.println("my server wait is over");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("im about to leave");
        return message.getBytes();
    }

    @Override
    public void run() {
        if (validPassWord(userName, Password)) {
            message = "true";
        } else {
            message = "false";
        }
        synchronized (server.getServer()) {
            System.out.println("im about to notify server");
            server.getServer().notifyAll();
        }
    }

    public boolean validPassWord(String userName, String password) {
        try {
            Scanner sc = new Scanner(file);
            System.out.println("user is : " + userName);
            System.out.println("pass is : " + StringToHash.convert(password));
            System.out.println("----------------------");
            while (sc.hasNextLine()) {
                try {
                    String line = sc.nextLine();
                    outPutOfRegex firstWord = server.getFirstWord(line);
                    String user = firstWord.getResult();
                    line = firstWord.getNewLine();
                    String pass = server.getFirstWord(line).getResult();
                    if (pass.equals(StringToHash.convert(password)) && user.equals(userName)) {
                        System.out.println("i found it valid");
                        return true;
                    } else {
                        System.out.println("userName was : " + user);
                        System.out.println("pass was : " + pass);
                        System.out.println(user.equals(StringToHash.convert(userName)));
                        System.out.println(pass.equals(password));
                        System.out.println("----------------------");
                    }
                } catch (StringIndexOutOfBoundsException a) {
                    continue;
                }
            }
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
