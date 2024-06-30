package GUI;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private static final Frame frame = new Frame ();
    private final Dimension size = new Dimension (600, 400);
    private final int monitorWidth = Toolkit.getDefaultToolkit ().getScreenSize ().width;
    private final int MonitorHeight = Toolkit.getDefaultToolkit ().getScreenSize ().height;

    private Frame () {
        setTitle ("Mahdi Drive");
        loginPanel panel = new loginPanel (size);
        setSize (size);
        setLayout(null);
        setLocation (new Point ((monitorWidth-getWidth ())/2,(MonitorHeight - getHeight ())/2));
        setResizable (false);
        setContentPane (panel);
        setIconImage (new ImageIcon ("src\\GUI\\resorces\\Drive.jpg").getImage ());
        setVisible (true);
        repaint ();
    }

    public static Frame getFrame () {
        return frame;
    }
}
