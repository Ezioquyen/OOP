package com.example.demo;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import org.controlsfx.control.BreadCrumbBar;

import java.io.IOException;


public class QuestionBankController {
    @FXML
    private VBox vBox;

    @FXML
    private void initialize() {

    }

    @FXML
    private void btnAddQuestion(ActionEvent event) throws IOException {
        VBox view = FXMLLoader.load(getClass().getResource("add-MTPCQ.fxml"));
        view.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        vBox.getChildren().setAll(view);
    }
}
