package fileTransmit;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;

public class send {
    FileInputStream fileInputStream;
    int port = 9876;
    DatagramSocket socket;
    Thread[] listOfThread = new Thread[5];
    InetAddress serverAddress;
    private static String lock = "lock";
    private final Dimension size = new Dimension(600, 400);
    private final int monitorWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    private final int MonitorHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    private JFrame jFrame;

    public send(File fileToSend) {
        try {
            serverAddress = InetAddress.getByName("localhost");
            socket = new DatagramSocket();
            long fileSize = fileToSend.length();
            fileInputStream = new FileInputStream(fileToSend);
            System.out.println("file size : " + fileSize);
            initFrame();
            JProgressBar jProgressBar = new JProgressBar(0, (int) (fileSize / 1024));
            jProgressBar.setStringPainted(true);
            jFrame.getContentPane().add(jProgressBar);
            jProgressBar.setValue(0);
            jProgressBar.setBounds(0, 0, jFrame.getContentPane().getWidth(), 100);
            jProgressBar.setVisible(true);
            jProgressBar.setOpaque(true);
            jFrame.repaint();
            for (int i = 0; i < 5; i++) {
                Thread thread = new Thread(() -> {
                    long fifthSize = fileSize / 5;
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    int count = 0;
                    try {
                        while (fifthSize > 0) {
                            synchronized (socket) {
                                try {
                                    Thread.sleep(2);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                if ((bytesRead = fileInputStream.read(buffer)) == -1) break;
                                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, serverAddress, port);
                                socket.send(packet);
                                count++;
                                System.out.println("packet send is : " + count);
                                jProgressBar.setValue(Math.min((int) (fileSize / 1024), jProgressBar.getValue() + 1));
                                fifthSize -= bytesRead;
                            }
                            jFrame.repaint();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                listOfThread[i] = thread;
            }
            jFrame.repaint();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        int i = 0;
        for (Thread thread : listOfThread) {
            thread.start();
            System.out.println("end" + i);
            i++;
        }
        int j = 0;
        for (Thread thread : listOfThread) {
            try {
                thread.join();
                System.out.println("end " + j);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            j++;
        }
        try {
            fileInputStream.close();
            jFrame.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        socket.close();
        jFrame.repaint();
        System.out.println("File sent successfully!");
    }

    private void initFrame() {
        jFrame = new JFrame();
        jFrame.setTitle("Mahdi Drive");
        jFrame.setSize(size.width,100);
        jFrame.setLayout(null);
        jFrame.setLocation(new Point((monitorWidth - jFrame.getWidth()) / 2, (MonitorHeight - jFrame.getHeight()) / 2));
        jFrame.setResizable(false);
        jFrame.setIconImage(new ImageIcon("src\\GUI\\resorces\\Drive.jpg").getImage());
        jFrame.setVisible(true);
        jFrame.repaint();
    }
}
