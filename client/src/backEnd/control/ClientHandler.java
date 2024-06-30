package backEnd.control;

import backEnd.control.fileTransmit.UoLoud;
import backEnd.control.fileTransmit.receive;
import backEnd.control.fileTransmit.send;

import java.io.File;

public class ClientHandler {
    private Integer port = 8887;
    private static ClientHandler serverHandler = new ClientHandler(8887);
    public static String signIn = "signIn";
    private static String aa = null;
    private static String aaaaa = null;

    public ClientHandler(int port) {
        serverHandler = this;
        this.port = port;
    }

    private static String ababa = "km";

    public boolean signIn(String userName, String password) {

        Thread a = new Thread(new Runnable() {

            @Override
            public void run() {
                String massage = "signIn|" + userName + "|" + password + "|";
                MessageTransmitter a = new MessageTransmitter(massage, port);
                a.start();
                try {
                    synchronized (a) {
                        a.wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("my wait is over");
                aa = new String(a.getAnswer());
                synchronized (serverHandler) {
                    serverHandler.notify();
                }
            }
        });
        a.start();
        synchronized (serverHandler) {
            try {
                serverHandler.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(aa);
        return Boolean.parseBoolean(aa);
    }

    public String signUp(String userName, String password) {
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                String massage = "signUp|" + userName + "|" + password + "|";
                MessageTransmitter messageTransmitter = new MessageTransmitter(massage, port);
                messageTransmitter.start();
                synchronized (messageTransmitter) {
                    try {
                        messageTransmitter.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("my wait is over");
                aaaaa = new String(messageTransmitter.getAnswer());
                synchronized (serverHandler) {
                    serverHandler.notify();
                }
            }
        });
        a.start();
        synchronized (serverHandler) {
            try {
                serverHandler.wait();
                System.out.println("my wait is over");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(aaaaa);
        return aaaaa;
//        while (true) {
//            if (aaaaa != null) {
//                System.out.println (aaaaa);
//                String aaa = aaaaa;
//                aaaaa = null;
//                return aaa;
//            }
//        }
    }

    public void sendFile(File file, String userName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread a = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String massage = "ULoud|" + userName + "|" + file.getName() + "|";
                        MessageTransmitter a = new MessageTransmitter(massage, port);
                        a.start();
                        try {
                            synchronized (a) {
                                a.wait();
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("my wait is over");
                        aa = new String(a.getAnswer());
                        synchronized (serverHandler) {
                            serverHandler.notify();
                        }
                    }
                });
                a.start();
                synchronized (serverHandler) {
                    try {
                        serverHandler.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (aa.equals("ready")) {
                    new send(file);
                }
            }
        }).start();
    }

    public String seeTheFile(String user) {
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                String massage = "see|" + user + "|";
                MessageTransmitter a = new MessageTransmitter(massage, port);
                a.start();
                try {
                    synchronized (a) {
                        a.wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("my wait is over");
                aa = new String(a.getAnswer());
                synchronized (serverHandler) {
                    serverHandler.notify();
                }
            }
        });
        a.start();
        synchronized (serverHandler) {
            try {
                serverHandler.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return aa;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public static ClientHandler getServerHandler() {
        if (serverHandler != null) {
            serverHandler = new ClientHandler(8887);
        }
        return serverHandler;
    }

    public boolean getFile(String user, String numberFile, String fileName) {
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                String massage = "DLoud|" + user + "|" + numberFile + "|";
                new receive(new UoLoud(fileName).getFilePath()).start();
                MessageTransmitter a = new MessageTransmitter(massage, port);
                a.start();
                try {
                    synchronized (a) {
                        a.wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("my wait is over");
                aa = new String(a.getAnswer());
                synchronized (serverHandler) {
                    serverHandler.notify();
                }
            }
        });
        a.start();
        synchronized (serverHandler) {
            try {
                serverHandler.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Boolean.parseBoolean(aa);
    }
}
