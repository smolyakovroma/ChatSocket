package sample.common;

public interface ConnectionListener {
    void connectionCreated(Connection c);
    void connectionClosed(Connection c);
    void connectionException(Connection c, Exception ex);
    void recievedContent(Message msg);

}
