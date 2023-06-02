package com.example.demo;
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
                        breadCrumbBarModel.setTabcheck(true);
                        breadCrumbBarModel.setCurrentView("1");

                    }
                    case 2 -> {
                        breadCrumbBarModel.setTabcheck(true);
                        breadCrumbBarModel.setCurrentView("2");

                    }
                    case 3 -> {
                        breadCrumbBarModel.setTabcheck(true);
                        breadCrumbBarModel.setCurrentView("3");

                    }
                    default -> {
                        breadCrumbBarModel.setTabcheck(true);
                        breadCrumbBarModel.setCurrentView("questionbank.fxml");

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
    private void btnAddQuestion() {
        breadCrumbBarModel.setCurrentView("add-MTPCQ.fxml");
    }
}
