package sample.common;

public interface Connection {

    int PORT = 3333;

    void send(Message msg);
    void close();
    public String getName();
    public void setName(String name);
}
