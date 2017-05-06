package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.common.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements ConnectionListener, Initializable{
    @FXML
    TextField txtConnection;
    @FXML
    TextField txtSend;
    @FXML
    TextField txtName;
    @FXML
    TextArea txtChat;
    @FXML
    Button btnConnection;
    @FXML
    Button btnSend;
    @FXML
    Button btnClose;

    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setState(false);
    }

    public void onConnection(ActionEvent actionEvent) {
        try {
            String name = txtName.getText().trim();
            if(name.isEmpty()){
                txtChat.setText("Укажите имя!");
                return;
            }
            InetAddress address = InetAddress.getByName(txtConnection.getText().trim());
            Socket socket = new Socket(address, Connection.PORT);
            this.connection = new ConnectionImpl(name, socket, this);
            Message msg = new MessageImpl(name, "Пользователь вошел", Message.CONNECT_TYPE);
            connection.send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setState(true);
    }

    public void onSend(ActionEvent actionEvent) {
        String content = txtSend.getText().trim();
        Message msg = new MessageImpl(txtName.getText(),content, Message.CONTENT_TYPE);
        connection.send(msg);
        txtSend.clear();
    }

    public void onClose(ActionEvent actionEvent) {

        Message msg = new MessageImpl(txtName.getText()," Пользователь вышел", Message.CLOSE_TYPE);
        connection.send(msg);
        txtSend.clear();
        txtChat.clear();
        connectionClosed(connection);

        setState(false);
    }

    @Override
    public void connectionCreated(Connection c) {
        System.out.println("Client connection was created");
        setState(true);
    }

    @Override
    public void connectionClosed(Connection c) {
        connection.close();
        System.out.println("Client connection was closed");

    }

    @Override
    public void connectionException(Connection c, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void recievedContent(Message msg) {
        txtChat.appendText(msg.toString());
        txtChat.appendText("\n");
    }

    private void setState(boolean isConnected){
        txtChat.setDisable(!isConnected);
        txtSend.setDisable(!isConnected);
        btnSend.setDisable(!isConnected);
        btnClose.setDisable(!isConnected);
        btnConnection.setDisable(isConnected);
        txtConnection.setDisable(isConnected);
        txtName.setDisable(isConnected);

    }


}
