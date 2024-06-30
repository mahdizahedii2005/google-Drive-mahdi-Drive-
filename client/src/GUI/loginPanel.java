package GUI;

import backEnd.control.ClientHandler;
import backEnd.control.ProgramPanel;

import javax.management.remote.JMXConnectionNotification;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class loginPanel extends JPanel {
    private JLabel user;
    private JLabel pass;
    private JTextField username;
    private JTextField password;
    private JButton signUp;
    private JButton signIn;
    private String usernameText = "enter your username";
    private String passwordText = "enter your password";
    private String enter = "r";

    public loginPanel(Dimension size) {
        setSize(size);
        setVisible(true);
        setLayout(null);
        user = new JLabel("User Name :");
        pass = new JLabel("Password  :");
        username = new JFormattedTextField();
        password = new JFormattedTextField();
        signIn = new JButton("sign In");
        signUp = new JButton("sign Up");
        build(pass, getSize().width / 7, getSize().height / 4 + getSize().height / 4, new Dimension(getWidth() / 8, getHeight() / 8), this);
        build(user, getSize().width / 7, getSize().height / 4, new Dimension(getWidth() / 8, getHeight() / 8), this);
        build(username, getSize().width / 7 + user.getWidth(), user.getY(), new Dimension((int) (getWidth() / 2), getHeight() / 8), this);
        build(password, getSize().width / 7 + pass.getWidth(), pass.getY(), new Dimension((int) (getWidth() / 2), getHeight() / 8), this);
        build(signUp, getSize().width / 6, pass.getY() + (2 * pass.getHeight()), new Dimension(username.getWidth() / 2, username.getHeight()), this);
        build(signIn, (3 * (getSize().width / 6)) + 75, pass.getY() + (2 * pass.getHeight()), new Dimension(username.getWidth() / 2, username.getHeight()), this);
        signIn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!password.getText().equals(passwordText) && !user.getText().equals(usernameText)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
//                            Frame.getFrame ().setContentPane (new programPanel (size,"ali"));
                            synchronized (enter) {
                                if (ClientHandler.getServerHandler().signIn(username.getText(), password.getText())) {
                                    Frame.getFrame().setContentPane(new programPanel(size, username.getText()));
                                    Frame.getFrame().repaint();
                                    ProgramPanel programPanel = new ProgramPanel(username.getText());
                                } else {
                                    JOptionPane.showMessageDialog(Frame.getFrame(), "invalid username or password");

                                }
                            }
                        }
                    }).start();
                }
            }
        });
        signUp.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!password.getText().equals(passwordText) && !user.getText().equals(usernameText)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String message = ClientHandler.getServerHandler().signUp(username.getText(), password.getText());
                            System.out.println("message is : " + message);
                            JOptionPane.showMessageDialog(Frame.getFrame(), "result of creating account : " + message);
                        }
                    }).start();
                }
            }
        });
        password.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (password.getText().equals(passwordText)) {
                    password.setText("");
                    password.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (password.getText().isEmpty()) {
                    password.setForeground(Color.GRAY);
                    password.setText(passwordText);
                }
            }
        });
        username.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (username.getText().equals(usernameText)) {
                    username.setText("");
                    username.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (username.getText().isEmpty()) {
                    username.setForeground(Color.GRAY);
                    username.setText(usernameText);
                }
            }
        });
    }

    public static <T extends JComponent> void build(T com, int x, int y, Dimension dimension, JPanel panel) {
        com.setBounds(x, y, dimension.width, dimension.height);
        com.setVisible(true);
        com.setOpaque(true);
        panel.add(com);
    }
}
