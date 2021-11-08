package com.company;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RoomController {
    DataInputStream dis;
    DataOutputStream dos;
    @FXML
    private Text countDown;

    @FXML
    private Label countQuesion;

    @FXML
    private ToggleGroup group;

    @FXML
    private RadioButton option1;

    @FXML
    private RadioButton option2;

    @FXML
    private RadioButton option3;

    @FXML
    private RadioButton option4;

    @FXML
    private Label question;
    @FXML
    private Label roomId;
    @FXML
    private Label numberPlayer;
    @FXML
    private Stage stage;
    private Parent root;
    String[] questions = {
            "Bất kì công dân nào vi phạm pháp luật đều phải chịu trách nhiệm về hành vi vi phạm của mình và phải bị xử lí theo quy định của pháp luật là nội dung của khái niệm nào dưới đây?",
            "Which year was Java created?",
            "What was Java originally called?",
            "Who is credited with creating Java?"
    };
    String[][] options = {
            {"Cùng có các điều kiện như nhau nhưng công ty X phải đóng thuế còn công ty Y không phải đóng thuế.", "Nữ từ đủ 18 tuổi được kết hôn nhưng nam giới phải đủ 20 tuổi mới được kết hôn.", "Học sinh là con em thương binh, liệt sĩ, học sinh nghèo được miễn, giảm học phí.", "Học sinh đang sống ở các địa bàn khó khăn như miền núi, vùng sâu, vùng xa, vùng biên giới và hải đảo được cộng điểm ưu tiên khi thi đại học."},
            {"1989", "1996", "1972", "1492"},
            {"Apple", "Latte", "Oak", "Koffing"},
            {"Steve Jobs", "Bill Gates", "James Gosling", "Mark Zuckerburg"}
    };
    String[][] answer = {{"true", "false", "false", "false"}, {"true", "false", "false", "false"}, {"false", "false", "true", "false"}, {"true", "false", "false", "false"}};
    int counter = 0, no_of_ques = questions.length;
    int seconds = 10;
    @FXML
    Timeline line = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
        seconds--;
        countDown.setText(String.valueOf(seconds));
        if (seconds < 10) {
            countDown.setText("0" + seconds);
        }
        if (seconds <= 0) {
            countDown.setText("00");
            displayAnswer();

        }
    }));

    public void nextQuestion() {
        if (counter >= no_of_ques) {
            displayResult();
        } else {
            displayquestion();
            line.setCycleCount(Timeline.INDEFINITE);
            line.play();
        }
    }

    private void displayquestion() {
        countQuesion.setText("Question " + (counter + 1));
        question.setText(questions[counter]);
        option1.setText(options[counter][0]);
        option2.setText(options[counter][1]);
        option3.setText(options[counter][2]);
        option4.setText(options[counter][3]);
        option1.setStyle("-fx-text-fix: black");
        option2.setStyle("-fx-text-fix: black");
        option3.setStyle("-fx-text-fix: black");
        option4.setStyle("-fx-text-fix: black");
        option1.setSelected(false);
        option2.setSelected(false);
        option3.setSelected(false);
        option4.setSelected(false);
        line.setCycleCount(Timeline.INDEFINITE);
        line.play();
    }

    public void displayResult() {

    }

    public void actionPerformed(ActionEvent event) {
        option1.setDisable(true);
        option2.setDisable(true);
        option3.setDisable(true);
        option4.setDisable(true);
        if (option1.isSelected()) {
            if (answer[counter][0] == "true") {
                option1.setStyle("-fx-text-fill: green");
                option1.setDisable(false);
            }
        }
        if (option2.isSelected()) {
            if (answer[counter][1] == "true") {
                option2.setStyle("-fx-text-fill: green");
                option2.setDisable(false);

            }
        }
        if (option3.isSelected()) {
            if (answer[counter][2] == "true") {
                option3.setStyle("-fx-text-fill: green");
                option3.setDisable(false);

            }
        }
        if (option4.isSelected()) {
            if (answer[counter][3] == "true") {
                option4.setStyle("-fx-text-fill: green");
                option4.setDisable(false);

            }
        }
        displayAnswer();
    }

    public void displayAnswer() {
        line.setCycleCount(Timeline.INDEFINITE);
        line.stop();
        option1.setDisable(true);
        option2.setDisable(true);
        option3.setDisable(true);
        option4.setDisable(true);
        if (answer[counter][0] != "true") {
            option1.setStyle("-fx-text-fill: red");
        } else {
            option1.setSelected(true);
            option1.setStyle("-fx-text-fill: green");
            option1.setDisable(false);
        }
        if (answer[counter][1] != "true") {
            option2.setStyle("-fx-text-fill: red");
        } else {
            option2.setSelected(true);
            option2.setStyle("-fx-text-fill: green");
            option2.setDisable(false);
        }
        if (answer[counter][2] != "true") {
            option3.setStyle("-fx-text-fill: red");
        } else {
            option3.setSelected(true);
            option3.setStyle("-fx-text-fill: green");
            option3.setDisable(false);
        }
        if (answer[counter][3] != "true") {
            option4.setStyle("-fx-text-fill: red");
        } else {
            option4.setSelected(true);
            option4.setStyle("-fx-text-fill: green");
            option4.setDisable(false);
        }
        Timeline pause = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            seconds = 10;
            countDown.setText(String.valueOf(seconds));
            option1.setDisable(false);
            option2.setDisable(false);
            option3.setDisable(false);
            option4.setDisable(false);
            counter++;
            nextQuestion();
        }));
        pause.play();
    }

    public void setDataStream(DataInputStream dis, DataOutputStream dos) throws IOException {
        this.dis = dis;
        this.dos = dos;

        System.out.println("Question list");
        System.out.println(dis.readUTF());
        nextQuestion();
    }

}
