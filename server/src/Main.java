import server.server;

public class Main {
    public static void main(String[] args) {
        while (true) {
            server.getServer().start();
            try {
                System.out.println("wait");
                server.getServer().join();
                System.out.println("conti");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            server.ResetServer();
//            Main.main(args);
        }
    }
}