package sample;

import sample.server.Server;

public class ServerRunner {
    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }
}
