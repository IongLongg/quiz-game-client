package com.company;

import com.company.constant.RequestCode;
import com.company.model.ClientRequest;
import com.company.model.Room;
import com.company.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class WaitingController {
    DataInputStream dis;
    DataOutputStream dos;
    ArrayList<Room> roomList;
    User currentUser;
    Gson gson = new Gson();

    @FXML
    public Label userIdLabel;

    @FXML
    public Label userNameLabel;

    @FXML
    private ListView<String> topicTitleListView;

    @FXML
    private Button joinRoomBtn;

    public void setDataStream(DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        try {
            // display user info
            Type typeUser = new TypeToken<User>(){}.getType();
            String jsonUser = dis.readUTF();
            currentUser = gson.fromJson(jsonUser, typeUser);
            System.out.println("Current User: " + currentUser.toString());
            userIdLabel.setText("ID: " + currentUser.getUserId());
            userNameLabel.setText("Display Name: " + currentUser.getUserName());

            // display room list
            String roomListJson = dis.readUTF();
            System.out.println(roomListJson);
            Type typeRoomList = new TypeToken<ArrayList<Room>>(){}.getType();
            roomList = gson.fromJson(roomListJson, typeRoomList);
            List<String> roomNames = new ArrayList<>();
            roomList.forEach(room -> {
                roomNames.add(room.getRoomName());
            });
            topicTitleListView.getItems().addAll(roomNames);
            topicTitleListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void joinRoom(ActionEvent event) {
        Integer selectedIndex = topicTitleListView.getSelectionModel().getSelectedIndex();
        Integer selectedTopicId = roomList.get(selectedIndex).getRoomId();
        ClientRequest request = new ClientRequest(RequestCode.USER_JOIN_ROOM, currentUser.getUserId() + "," + selectedTopicId.toString());
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(request);
        try {
            dos.writeUTF(jsonRequest);
            dos.flush();

            int roomCode = Integer.parseInt(dis.readUTF());
            System.out.println(roomCode);
            System.out.println("======");
            if (roomCode == RequestCode.ROOM_FULL) {
                // thong bao khong vao duoc
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Quiz game");
                alert.setHeaderText("This room is not available");
                alert.show();
            } else if (roomCode == RequestCode.ROOM_WAIT) {
                // chuyen man hinh cho
                FXMLLoader fxmlLoader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource("loading-view.fxml"))
                );
                Parent loadingViewParent = (Parent) fxmlLoader.load();

                LoadingController loadingController = fxmlLoader.getController();
                loadingController.setDataStream(dis, dos);

                Scene loadingScene = new Scene(loadingViewParent);
                switchScene(event, loadingScene);
            } else if (roomCode == RequestCode.ROOM_START) {
                // chuyen man hinh cau hoi
                FXMLLoader fxmlLoader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource("room-view.fxml"))
                );
                Parent roomViewParent = (Parent) fxmlLoader.load();

                RoomController roomController = fxmlLoader.getController();
                roomController.setDataStream(dis, dos);

                Scene roomScene = new Scene(roomViewParent);
                switchScene(event, roomScene);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void switchScene(ActionEvent event, Scene nextScene){
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(nextScene);
        window.show();
    }
}
