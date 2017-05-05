package sample.server;

import sample.common.Connection;
import sample.common.ConnectionImpl;
import sample.common.ConnectionListener;
import sample.common.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashSet;
import java.util.Set;

public class Server implements ConnectionListener {

    private Set<Connection> connections;
    private ServerSocket serverSocket;

    public Server() {
        try {
            serverSocket = new ServerSocket(Connection.PORT);
            connections = new LinkedHashSet<Connection>();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startServer() {
        System.out.println("Server started");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
//                connections.add(new ConnectionImpl(socket, this));
                connectionCreated(new ConnectionImpl("", socket, this));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void connectionCreated(Connection c) {
        connections.add(c);
        System.out.println("Connection was added");
    }

    public synchronized void connectionClosed(Connection c) {
        connections.remove(c);
        c.close();
        System.out.println("Connection was closed");
    }

    public synchronized void connectionException(Connection c, Exception ex) {
        connections.remove(c);
        c.close();
        System.out.println("Connection was closed");
        ex.printStackTrace();
    }

    public synchronized void recievedContent(Message msg) {
        String errorName = msg.getName().trim();
        if (msg.getType() == Message.CONTENT_TYPE) {
            for (Connection connection : connections) {
                connection.send(msg);
            }
        }
        if (msg.getType() == Message.CONNECT_TYPE) {
            for (Connection connection : connections) {
//                if (connection.getName().equals(errorName)) {
//                  return;
//                }
                if (connection.getName().equals("")) {
                    connection.setName(msg.getName());
                }
                connection.send(msg);
            }
        }
        if (msg.getType() == Message.CLOSE_TYPE) {
            Connection removeConnection = null;
            for (Connection connection : connections) {
                if (connection.getName().equals(msg.getName())) {
                    removeConnection = connection;
                } else {
                    connection.send(msg);
                }
            }
            if(removeConnection!=null){
                connectionClosed(removeConnection);
            }
        }
    }
}
