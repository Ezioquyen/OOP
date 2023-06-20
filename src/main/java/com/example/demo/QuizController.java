package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class QuizController {
    @FXML
    private Label quizName;
    private DataModel dataModel;
    private BreadCrumbBarModel breadCrumbBarModel;

    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
    }

    public void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    @FXML
    private void initialize() {
        initModel(BreadCrumbBarModel.getInstance());
        initDataModel(DataModel.getInstance());
        quizName.setText(dataModel.getCurrentQuizName());
    }

    @FXML
    private void btnQuizEditing() {
        breadCrumbBarModel.setCurrentView("EditQuiz.fxml");
    }
}
