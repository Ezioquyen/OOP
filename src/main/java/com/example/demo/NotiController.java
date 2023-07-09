package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class NotiController {
    @FXML
    private Button closeButton;
    @FXML
    private Button cancel;
    @FXML
    private Button startAttempt;
    @FXML
    private Label text;

    @FXML
    private void initialize() {
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public Button getCancel() {
        return cancel;
    }

    public Button getStartAttempt() {
        return startAttempt;
    }

    public Label getText() {
        return text;
    }
}
