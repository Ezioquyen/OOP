package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class thiCuoiKyController {
    @FXML
    private VBox contentArea;
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
        for (String title : dataModel.getQuizTitle()) {
            contentArea.getChildren().add(new QuizCheckBox(title));
        }
    }
}
