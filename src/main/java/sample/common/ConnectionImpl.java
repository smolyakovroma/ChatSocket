package sample.common;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ConnectionImpl implements Connection, Runnable {

    private Socket socket;
    private ConnectionListener connectionListener;
    private boolean needToRun =true;
    private OutputStream out;
    private InputStream in;
    private String name;

    public ConnectionImpl(String name, Socket socket, ConnectionListener connectionListener) {

        try {
            this.name = name;
            this.socket = socket;
            this.connectionListener = connectionListener;
            this.out = socket.getOutputStream();
            this.in = socket.getInputStream();
            Thread thread = new Thread(this);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void send(Message msg) {
        try{
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(msg);
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void close() {
        needToRun = false;
    }

    public void run() {
        while (needToRun){
            try {

                int amount = in.available();
                if(amount!=0){
                    ObjectInputStream objIn = new ObjectInputStream(in);
                    Message msg = (Message) objIn.readObject();
                    connectionListener.recievedContent(msg);
                }else {
                    Thread.sleep(200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
