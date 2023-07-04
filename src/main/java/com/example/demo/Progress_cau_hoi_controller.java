package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Progress_cau_hoi_controller implements Initializable {
    @FXML
    private AnchorPane coloranswered;

    @FXML
    private Label questionnb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setNumber(int a) {
        questionnb.setText(""+a);
    }
    public void setColor() {
        coloranswered.setStyle("-fx-background-color: black;");
    }
    public void setDefalutColor() {
        coloranswered.setStyle("-fx-background-color: white;");
    }
    public void setWrongColor() {
        coloranswered.setStyle("-fx-background-color: red;");
    }
}
