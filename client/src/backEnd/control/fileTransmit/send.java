package backEnd.control.fileTransmit;

import GUI.Frame;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;

public class send {
    JProgressBar jProgressBar;
    FileInputStream fileInputStream;
    int port = 7988;
    DatagramSocket socket;
    Thread[] listOfThread = new Thread[5];
    InetAddress serverAddress;
    private static String lock = "lock";

    public send(File fileToSend) {
        try {
            serverAddress = InetAddress.getByName("localhost");
            socket = new DatagramSocket();
            long fileSize = fileToSend.length();
            fileInputStream = new FileInputStream(fileToSend);
            System.out.println("file size : " + fileSize);
             jProgressBar = new JProgressBar(0, (int) (fileSize / 1024));
            jProgressBar.setStringPainted(true);
            Frame.getFrame().getContentPane().add(jProgressBar);
            jProgressBar.setValue(0);
            jProgressBar.setBounds(0, 0, Frame.getFrame().getContentPane().getWidth(), 100);
            jProgressBar.setVisible(true);
            jProgressBar.setOpaque(true);
            Frame.getFrame().repaint();
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
                            Frame.getFrame().repaint();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                listOfThread[i] = thread;
            }
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        socket.close();
        Frame.getFrame().getContentPane().remove(jProgressBar);
        Frame.getFrame().repaint();
        System.out.println("File sent successfully!");
    }

}
