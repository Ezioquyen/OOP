package com.example.demo;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class AddQuesFromQuestionsBankController {
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
        ListView<CustomCheckBox> list = new ListView<>();
        if (root.getSelectionModel().getSelectedItem() != null) {
            for (Question question : dataModel.getQuestion(dataModel.getCategoryMap().get(root.getSelectionModel().getSelectedItem()))) {
                CustomCheckBox customCheckBox = new CustomCheckBox(question);
                customCheckBox.getBox().getChildren().remove(customCheckBox.getButton());
                FontIcon icon = new FontIcon("fas-plus");
                icon.setIconColor(Color.valueOf("#00ACEA"));
                FontIcon icon2 = new FontIcon("fas-search-plus");
                icon2.setIconColor(Color.valueOf("#00ACEA"));
                customCheckBox.getBox().getChildren().add(0, icon);
                customCheckBox.getChildren().add(icon2);
                list.getItems().add(customCheckBox);
            }
            list.getSelectionModel().selectedItemProperty().addListener(observable -> {
                list.getSelectionModel().getSelectedItem().getCheckBox().setSelected(!list.getSelectionModel().getSelectedItem().getCheckBox().isSelected());
            });
            showQuestion.getChildren().add(list);
            VBox.setVgrow(list, Priority.ALWAYS);
        }
    }
}
