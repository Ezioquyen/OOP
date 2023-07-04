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

import java.util.ArrayList;
import java.util.List;

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
    @FXML
    private Button btnADD;
    private DataModel dataModel;
    private final ListView<CustomCheckBox> list = new ListView<>();


    private final List<CustomCheckBox> questions = new ArrayList<>();

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
        list.setOnMouseClicked(null);
        showQuesFromCate.selectedProperty().addListener(e -> {
            if (showQuesFromCate.isSelected()) {
                showInfor.setVisible(true);
                showQuestion();
            } else {
                list.getItems().clear();
                showInfor.setVisible(false);
            }
        });
        root.setRoot(dataModel.getRoot());
        root.getSelectionModel().selectedItemProperty().addListener(e -> {
            if (showQuesFromCate.isSelected()) {
                list.getItems().clear();
                showQuestion();
            }
            label.setText(root.getSelectionModel().getSelectedItem().getValue());
        });
        showQuestion.getChildren().add(list);
        VBox.setVgrow(list, Priority.ALWAYS);
    }

    public Button getButton() {
        return button;
    }

    private void showQuestion() {

        if (root.getSelectionModel().getSelectedItem() != null) {
            for (Question question : dataModel.getQuestionExcept(dataModel.getCategoryMap().get(root.getSelectionModel().getSelectedItem()), dataModel.getCurrentQuiz().getQuizID())) {
                CustomCheckBox customCheckBox = new CustomCheckBox(question);
                customCheckBox.getBox().getChildren().remove(customCheckBox.getButton());
                FontIcon icon = new FontIcon("fas-plus");
                icon.setIconColor(Color.valueOf("#00ACEA"));
                FontIcon icon2 = new FontIcon("fas-search-plus");
                icon2.setIconColor(Color.valueOf("#00ACEA"));
                customCheckBox.getBox().getChildren().add(0, icon);
                customCheckBox.getChildren().add(icon2);
                list.getItems().add(customCheckBox);
                customCheckBox.getCheckBox().selectedProperty().addListener(e -> {
                    if (customCheckBox.getCheckBox().isSelected()) {
                        questions.add(customCheckBox);
                    } else questions.remove(customCheckBox);
                });
            }
            /*list.getSelectionModel().selectedItemProperty().addListener(e -> {
                list.getSelectionModel().getSelectedItem().getCheckBox().setSelected(true);
            });*/
        }
    }

    public ListView<CustomCheckBox> getList() {
        return list;
    }

    public List<CustomCheckBox> getQuestions() {
        return questions;
    }

    public Button getBtnADD() {
        return btnADD;
    }

    @FXML
    private void btnAdd() {
        for (CustomCheckBox question : questions) {
            dataModel.insertQuestionToQuiz(question.getQuestion().getId(), dataModel.getCurrentQuiz().getQuizID());
            list.getItems().remove(question);
        }
        list.getSelectionModel().clearSelection();
        questions.clear();
    }
}
