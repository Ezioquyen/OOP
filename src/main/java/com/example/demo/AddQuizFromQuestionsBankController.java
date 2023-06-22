package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

public class AddQuizFromQuestionsBankController {
    @FXML
    private Button button;
    @FXML
    private TreeView<String> root;
    @FXML
    private VBox showInfor;
    @FXML
    private CheckBox showQuesFromCate;
    @FXML
    private VBox showQuestion;
    @FXML
    private Label label;
    private DataModel dataModel;

    private void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    @FXML
    private void initialize() {
        initDataModel(DataModel.getInstance());
        showInfor.setVisible(false);
        showQuesFromCate.selectedProperty().addListener(e -> {
            if (showQuesFromCate.isSelected()) {
                showInfor.setVisible(true);
                showQuestion();
            } else {
                showQuestion.getChildren().clear();
                showInfor.setVisible(false);

            }
        });
        root.setRoot(dataModel.getRoot());
        root.getSelectionModel().selectedItemProperty().addListener(e -> {
            if (showQuesFromCate.isSelected()) {
                showQuestion.getChildren().clear();
                showQuestion();
            }
            label.setText(root.getSelectionModel().getSelectedItem().getValue());
        });
    }

    public Button getButton() {
        return button;
    }

    private void showQuestion() {

        int i = 0;
        if (root.getSelectionModel().getSelectedItem() != null) {
            for (Question question : dataModel.getQuestion(dataModel.getCategoryMap().get(root.getSelectionModel().getSelectedItem()))) {

                CustomCheckBox customCheckBox = new CustomCheckBox(question);
                if (i % 2 == 0) {
                    customCheckBox.setStyle("-fx-background-color: white");
                } else customCheckBox.setStyle("-fx-background-color: lightgray");
                showQuestion.getChildren().add(customCheckBox);
                i++;
            }
        }
    }
}
