package GUI;

import backEnd.control.ClientHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Scanner;

public class programPanel extends JPanel {
    private final String userName;
    private JFileChooser jFileChooserForUpLoud;
    private JButton ListOfFileForDownLoud;
    private Integer port = 8887;
    private Dimension fileChooserDim = new Dimension(450, 50);
    private Dimension upLoudDim = new Dimension(50, 50);
    private Dimension listOfYourFile = new Dimension(fileChooserDim.width - upLoudDim.width, 50);
    private ClientHandler enterHandler;
    private JButton upLoud;

    public programPanel(Dimension size, String userName) {
        this.userName = userName;
        enterHandler = new ClientHandler(port);
        setSize(size);
        setVisible(true);
        setOpaque(true);
        setLayout(null);
        ListOfFileForDownLoud = new JButton("List Of Your File On The server");
        upLoud = new JButton("UpLoud File On The server");
        loginPanel.build(ListOfFileForDownLoud, getWidth() / 6, getHeight() / 3, listOfYourFile, this);
        loginPanel.build(upLoud, getWidth() / 6, (getHeight() * 4) / 6, listOfYourFile, this);
        upLoud.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFileChooserForUpLoud = new JFileChooser();
                jFileChooserForUpLoud.setBounds(10, (getHeight() * 4) / 6, fileChooserDim.width, fileChooserDim.height);
                jFileChooserForUpLoud.setVisible(true);
                jFileChooserForUpLoud.setOpaque(true);
                add(jFileChooserForUpLoud);
                jFileChooserForUpLoud.setFileSelectionMode(0);
                jFileChooserForUpLoud.showOpenDialog(null);
                File targetFile = jFileChooserForUpLoud.getSelectedFile();
                if (targetFile != null) {
                    System.out.println(targetFile);
                    ClientHandler.getServerHandler().sendFile(targetFile, userName);
                }
            }
        });
        ListOfFileForDownLoud.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane textArea = new JTextPane();
                textArea.setText(ClientHandler.getServerHandler().seeTheFile(userName));
                JFrame jFrame = new JFrame();
                JPanel jPanel = new JPanel();
                jFrame.setLayout(null);
                jPanel.setLayout(null);
                jFrame.setBounds(500, 300, 400, 300);
                jPanel.setSize(new Dimension(400,300));
                jFrame.setContentPane(jPanel);
                jPanel.add(textArea);
                jPanel.setVisible(true);
                jFrame.setVisible(true);
                textArea.setBounds(25, 5, 350, 150);
                textArea.setVisible(true);
                JButton jButton = new JButton("DownLoud");
                JTextArea textField = new JTextArea();
                jButton.setVisible(true);
                jButton.setBounds(200, 175, 150, 50);
                textField.setVisible(true);
                textField.setBounds(70, 175, 50, 50);
                jPanel.add(jButton);
                jPanel.add(textField);
                jButton.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                jFrame.dispose();
                                jFrame.repaint();
                                Scanner sc = new Scanner(textArea.getText());
                                for (int i = 1; i < Integer.parseInt(textField.getText()); i++) {
                                    sc.nextLine();
                                }
                                String name = sc.nextLine();
                                name = name.substring(2);
                                ClientHandler.getServerHandler().getFile(userName, textField.getText(), name);
                                System.out.println("uploud");
                            }
                        }).start();
                    }
                });
            }
        });
    }
}
