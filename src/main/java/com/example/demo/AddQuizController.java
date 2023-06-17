package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class AddQuizController {
    @FXML
    private TextField textField;
    @FXML
    private TextField name;
    @FXML
    private CheckBox checkBox;
    private float time = 0;
    DataModel dataModel;

    private void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    @FXML
    private void initialize() {
        initDataModel(DataModel.getInstance());
        checkBox.selectedProperty().addListener(e -> {
            if (checkBox.isSelected()) {
                if (textField.getText() != null) time = Float.parseFloat(textField.getText());
            } else time = 0;
        });
    }

    @FXML
    private void btnCreate() {
        dataModel.insertQuiz(name.getText(), time);
    }
}
