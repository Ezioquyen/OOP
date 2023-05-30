package com.example.demo;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class QuestionBankController {
    @FXML
    private VBox vBox;
    @FXML
    private TextField textField;
    private BreadCrumbBarModel breadCrumbBarModel;

    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
    }

    @FXML
    private void initialize() {
        initModel(BreadCrumbBarModel.getInstance());
    }

    @FXML
    private void btnAddQuestion(ActionEvent event) throws IOException {
        breadCrumbBarModel.setCurrentView("add-MTPCQ.fxml");

        breadCrumbBarModel.setCurrentTree(breadCrumbBarModel.getBreadConnection().get("add-MTPCQ.fxml"));
        VBox view = FXMLLoader.load(getClass().getResource("add-MTPCQ.fxml"));
        view.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        vBox.getChildren().setAll(view);
    }
}
