package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Confirmation {
    @FXML
    private Button cancel;
    @FXML
    private Button startAttempt;

    @FXML
    private void initialize() {
    }

    public Button getCancel() {
        return cancel;
    }

    public Button getStartAttempt() {
        return startAttempt;
    }
}
