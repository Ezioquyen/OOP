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
    private DataModel dataModel;
    private BreadCrumbBarModel breadCrumbBarModel;

    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
    }

    private void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    @FXML
    private void initialize() {
        initModel(BreadCrumbBarModel.getInstance());
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
