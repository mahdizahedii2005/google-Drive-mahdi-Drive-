package fileTransmit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class receive extends Thread {
    private int serverPort = 7988;
    private String path;
    private File file;

    public receive(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket =null;
            try {
                socket = new DatagramSocket(serverPort);
            } catch (BindException e) {
            }
            File receivedFile = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(receivedFile);
            byte[] buffer = new byte[1024];
            int o = 1;
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                fileOutputStream.write(packet.getData(), 0, packet.getLength());
                System.out.println("get : " + o);
                o++;
                if (packet.getLength() < buffer.length) {
                    System.out.println("File transfer complete!");
                    break;
                }
            }
            file = receivedFile;
            synchronized (receive.class) {
                receive.class.notify();
            }
            socket.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        start();
        synchronized (receive.class) {
            try {
                receive.class.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(file.getName());
        return file;
    }
}
