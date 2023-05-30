package com.example.demo;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

import java.util.Objects;

public class QuestionBankController {
    private BreadCrumbBarModel breadCrumbBarModel;
    @FXML
    private TabPane tabPane;

    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, b, a) -> {
            if (a != b) {
                switch (tabPane.getSelectionModel().getSelectedIndex()) {
                    case 1 -> {
                        breadCrumbBarModel.setCurrentView("1");
                        breadCrumbBarModel.setTabcheck(true);
                    }
                    case 2 -> {
                        breadCrumbBarModel.setCurrentView("2");
                        breadCrumbBarModel.setTabcheck(true);
                    }
                    case 3 -> {
                        breadCrumbBarModel.setCurrentView("3");
                        breadCrumbBarModel.setTabcheck(true);
                    }
                    default -> {
                        breadCrumbBarModel.setCurrentView("questionbank.fxml");
                        breadCrumbBarModel.setTabcheck(true);
                    }
                }

            }
        });
    }

    @FXML
    private void initialize() {
        initModel(BreadCrumbBarModel.getInstance());
        if (Objects.equals(breadCrumbBarModel.getCurrentView(), "questionbank.fxml")) {
            tabPane.getSelectionModel().select(0);
        } else if (!Objects.equals(breadCrumbBarModel.getCurrentView(), "questionbank.fxml"))
            tabPane.getSelectionModel().select(Integer.parseInt(breadCrumbBarModel.getCurrentView()));
    }

    @FXML
    private void btnAddQuestion(ActionEvent event) {
        breadCrumbBarModel.setCurrentView("add-MTPCQ.fxml");
    }
}
