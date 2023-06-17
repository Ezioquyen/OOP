package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AddMTPCQController {
    @FXML
    private VBox showChoices;
    private int currentChoice = 0;

    @FXML
    private void initialize() {
    }

    @FXML
    private void btnAddThreeMoreChoices() throws IOException {
        ArrayList<Parent> views = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("choice-box.fxml"));
            Parent parent = fxmlLoader.load();
            ChoiceBoxController view = fxmlLoader.getController();
            view.setChoice("Choice " + (i + currentChoice));
            views.add(parent);
        }
        currentChoice += 3;
        showChoices.getChildren().addAll(views);
    }

    private void btnSaveAndContinueEditing() {

    }

    private void btnSaveChanges() {

    }

    private void btnCancel() {

    }
}
