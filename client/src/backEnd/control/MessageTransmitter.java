
package backEnd.control;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mahdi
 */
public class MessageTransmitter extends Thread {

    private Socket s;
    private byte[] bytes;
    private byte[] answer;

    public MessageTransmitter (String message, int port) {
        try {
            this.bytes = message.getBytes ();
            s = new Socket ("localhost", port);
        } catch (UnknownHostException e) {
            throw new RuntimeException (e);
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
    }

    public synchronized byte[] getAnswer () {
        System.out.println ("im out and answer is : " + new String (answer));
        return answer;

    }

    @Override
    public void run () {
        try {
            System.out.println (" the rec is : " + new String (bytes));
            s.getOutputStream ().write (bytes);
            System.out.println ("message that is send :" + new String (bytes));
            DataInputStream dataInputStream = new DataInputStream (s.getInputStream ());
            sleep (300);
            int len = dataInputStream.available ();
            while (len == 0) {
                len = dataInputStream.available ();
            }
            System.out.println ("len of answer is : " + len);
            answer = new byte[len];
            for (int i = 0 ; i < len ; i++) {
                answer[i] = dataInputStream.readByte ();
            }
            System.out.println ("answer is : " + new String (answer));
            synchronized (this) {
                System.out.println ("im about to notify");
                this.notifyAll ();
            }
            s.close ();
        } catch (IOException ex) {
            System.out.println ("error");
            Logger.getLogger (MessageTransmitter.class.getName ()).log (Level.SEVERE, null, ex);
        } catch (InterruptedException e) {
            throw new RuntimeException (e);
        }
    }
}