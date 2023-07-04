package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddMTPCQController {
    @FXML
    private VBox showChoices;
    @FXML
    private TreeView<String> root;
    @FXML
    private Label label;
    @FXML
    private TextField quesName;
    @FXML
    private TextField mark;
    private int currentChoice = 0;
    private DataModel dataModel;
    private BreadCrumbBarModel breadCrumbBarModel;
    private final List<ChoiceBoxController> controllers = new ArrayList<>();

    public void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
    }

    @FXML
    private void initialize() throws IOException {
        initDataModel(DataModel.getInstance());
        initModel(BreadCrumbBarModel.getInstance());
        for (int i = 1; i <= 2; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("choice-box.fxml"));
            Parent parent = fxmlLoader.load();
            ChoiceBoxController view = fxmlLoader.getController();
            view.setChoice("Choice " + (i + currentChoice));
            controllers.add(view);
            showChoices.getChildren().add(parent);
        }
        currentChoice = 2;
        root.setRoot(dataModel.getRoot());
        root.getSelectionModel().selectedItemProperty().addListener(e -> {
            label.setText(root.getSelectionModel().getSelectedItem().getValue());
        });
    }

    @FXML
    private void btnAddThreeMoreChoices() throws IOException {
        for (int i = 1; i <= 3; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("choice-box.fxml"));
            Parent parent = fxmlLoader.load();
            ChoiceBoxController view = fxmlLoader.getController();
            view.setChoice("Choice " + (i + currentChoice));
            controllers.add(view);
            showChoices.getChildren().add(parent);
        }
        currentChoice += 3;
    }

    @FXML
    private void btnSaveAndContinueEditing() throws NullPointerException {
        Question question = new Question();
        question.addTitle(quesName.getText());
        question.setMark(Double.valueOf(mark.getText()));
        for (ChoiceBoxController controller : controllers) {
            if (!Objects.equals(controller.getTextArea().getText(), "")) {
                question.getOptions().add(controller.getTextArea().getText());
                if (Objects.equals(controller.getComboBox().getSelectionModel().getSelectedItem(), "None") || controller.getComboBox().getSelectionModel().getSelectedItem() == null) {
                    question.getPercent().add((double) 0);
                } else
                    question.getPercent().add(Double.valueOf(controller.getComboBox().getSelectionModel().getSelectedItem().substring(0, controller.getComboBox().getSelectionModel().getSelectedItem().length() - 1)));
            }
        }
        question.typeDetect();
        dataModel.insertQuestion(root.getSelectionModel().getSelectedItem(), question.getTitle(), question.isType(), Double.valueOf(mark.getText()));
        dataModel.insertAnswers(question.getOptions(), question.getPercent(), 0);
        dataModel.setCount(1);
        dataModel.updateCategory(root.getSelectionModel().getSelectedItem());
        dataModel.setCount(0);
    }

    @FXML
    private void btnSaveChanges() throws NullPointerException {
        Question question = new Question();
        question.addTitle(quesName.getText());
        question.setMark(Double.valueOf(mark.getText()));
        for (ChoiceBoxController controller : controllers) {
            if (!Objects.equals(controller.getTextArea().getText(), "")) {
                question.getOptions().add(controller.getTextArea().getText());
                if (Objects.equals(controller.getComboBox().getSelectionModel().getSelectedItem(), "None") || controller.getComboBox().getSelectionModel().getSelectedItem() == null) {
                    question.getPercent().add((double) 0);
                } else
                    question.getPercent().add(Double.valueOf(controller.getComboBox().getSelectionModel().getSelectedItem().substring(0, controller.getComboBox().getSelectionModel().getSelectedItem().length() - 1)));
            }
        }
        question.typeDetect();
        dataModel.insertQuestion(root.getSelectionModel().getSelectedItem(), question.getTitle(), question.isType(), Double.valueOf(mark.getText()));
        dataModel.insertAnswers(question.getOptions(), question.getPercent(), 0);
        dataModel.setCount(1);
        dataModel.updateCategory(root.getSelectionModel().getSelectedItem());
        dataModel.setCount(0);
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("questionbank.fxml"));
    }

    @FXML
    private void btnCancel() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("questionbank.fxml"));
    }

}
