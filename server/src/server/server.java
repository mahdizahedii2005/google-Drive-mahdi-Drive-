package server;

import enter.signIn;
import enter.*;
import fileTransmit.UpLoud;
import fileTransmit.downLoud;
import fileTransmit.receive;
import fileTransmit.send;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class server extends Thread {
    private ServerSocket serverSocket;
    private Socket socket;
    private static server server;
    private int port;

    public static void ResetServer() {
        server.server = null;
    }

    public server() {
        this(8887);
    }

    public server(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
//        while (true) {
        try {
            socket = serverSocket.accept();
            while (socket.getInputStream().available() == 0) {
                Thread.sleep(4);
                System.out.println("wAAAAIT");
            }

            System.out.println("socket find :" + socket.getPort());
            byte[] answer = ChoseMethod(socket.getInputStream());
            System.out.println("answer is :" + new String(answer));
            socket.getOutputStream().write(answer);
            socket.close();
            serverSocket.close();
            System.out.println("===============================================");
        } catch (IOException e) {
            throw new RuntimeException(e);
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] ChoseMethod(InputStream inputStream) {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        try {
            int len = dataInputStream.available();
            System.out.println("len of rec :" + len);
            byte[] bytes = new byte[len];
            for (int i = 0; i < len; i++) {
                bytes[i] = dataInputStream.readByte();
            }
            String line = new String(bytes);
            System.out.println("the rec is :" + line);
            if (line.isEmpty()) {
                System.out.println("message was empty and i crash");
                return "unsuccessful".getBytes();
            }
            outPutOfRegex method = getFirstWord(line);
            line = method.getNewLine();
            System.out.println("line now is :" + line);
            System.out.println("method found is :" + method.getResult());
            if (method.getResult().equals("signIn")) {
                System.out.println("i found rec is sign in");
                outPutOfRegex userName = getFirstWord(line);
                line = userName.getNewLine();
                outPutOfRegex pass = getFirstWord(line);
                line = pass.getNewLine();
                var a = new signIn(userName.getResult(), pass.getResult()).play();
                System.out.println("start returning");
                return a;
            } else if (method.getResult().equals("signUp")) {
                System.out.println("i found rec is sign up");
                outPutOfRegex userName = getFirstWord(line);
                line = userName.getNewLine();
                outPutOfRegex pass = getFirstWord(line);
                line = pass.getNewLine();
                return new signUp(userName.getResult(), pass.getResult()).play();
            } else if (method.getResult().equals("DLoud")) {
                //DLoud|username|fileName|
                outPutOfRegex user = getFirstWord(line);
                line = user.getNewLine();
                File f = new downLoud(user.getResult()).findFile(Integer.parseInt(getFirstWord(line).getResult()));
                if (f != null) {
                    new send(f);
                    return "true".getBytes();
                } else {
                    return "false".getBytes();
                }
            } else if (method.getResult().equals("ULoud")) {
                //ULoud|username|fileName|
                outPutOfRegex user = getFirstWord(line);
                line = user.getNewLine();
                String filePath = new UpLoud(getFirstWord(line).getResult(), user.getResult()).getFilePath();
                new receive(filePath).start();
                return "ready".getBytes();
            } else if (method.getResult().equals("see")) {
                outPutOfRegex user = getFirstWord(line);
                return new downLoud(user.getResult()).getLF().getBytes();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static outPutOfRegex getFirstWord(String line) {
        ArrayList<Character> commond = new ArrayList<>();
        int i = 0;
        while (line.charAt(i) != '|') {
            commond.add(line.charAt(i));
            i++;
        }
        line = line.substring(i + 1);
        return new outPutOfRegex(line, ArrayToString(commond));
    }

    public static boolean StringByChar(ArrayList<Character> characterArrayList, String a) {
        try {
            for (int i = 0; i < characterArrayList.size(); i++) {
                if (characterArrayList.get(i) != a.charAt(i)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String ArrayToString(ArrayList<Character> arrayList) {
        String result = "";
        for (int i = 0; i < arrayList.size(); i++) {
            result += arrayList.get(i);
        }
        return result;
    }

    public static server getServer() {
        if (server == null) {
            server = new server();
            System.out.println("make new one ");
        }
        return server;
    }
}
