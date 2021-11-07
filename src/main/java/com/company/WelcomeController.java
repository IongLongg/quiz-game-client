package com.company;

import com.company.constant.Constant;
import com.company.constant.RequestCode;
import com.company.model.ClientRequest;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WelcomeController implements Initializable {
    @FXML
    private TextField nameText;
    @FXML
    private Button joinButton;
    @FXML
    private Label alertText;

    private boolean isJoined = false;

    Socket client;
    DataInputStream dis;
    DataOutputStream dos;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    protected void joinGame(ActionEvent event) {
        String name = nameText.getText();
        if (name.isEmpty()) {
            alertText.setText("Name is required");
            alertText.setVisible(true);
        } else {
            createConnection();
            try {
                dos = new DataOutputStream(client.getOutputStream());
                dis = new DataInputStream(client.getInputStream());
                ClientRequest request = new ClientRequest(RequestCode.USER_JOIN_GAME, name);
                Gson gson = new Gson();
                String jsonRequest = gson.toJson(request);
                dos.writeUTF(jsonRequest);
                dos.flush();

                System.out.println("User: " + name);
                FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("waiting-view.fxml")));
                Parent waitingViewParent = (Parent) fxmlLoader.load();

                WaitingController waitingController = fxmlLoader.getController();
                waitingController.setDataStream(dis, dos);

                Scene waitingViewScene = new Scene(waitingViewParent);
                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                window.setScene(waitingViewScene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createConnection() {
        InetAddress address;
        try {
            address = InetAddress.getByName(Constant.SERVER_IP);
            client = new Socket(address, Constant.SERVER_PORT);

            alertText.setVisible(false);
        } catch (Exception ex) {
            alertText.setVisible(true);
            alertText.setText("Cannot connect to server");
            Logger.getLogger(WelcomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}