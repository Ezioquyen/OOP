package com.example.demo;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Label;

import javafx.scene.control.ListView;
import javafx.stage.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditQuizController {

    @FXML
    private ListView<EditQuesFromQuiz> list;
    @FXML
    private Label quizTitle;
    private DataModel dataModel;
    private BreadCrumbBarModel breadCrumbBarModel;

    public void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    public void initBreadCrumbBarModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
    }

    @FXML
    private void initialize() {
        initDataModel(DataModel.getInstance());
        initBreadCrumbBarModel(BreadCrumbBarModel.getInstance());
        List<EditQuesFromQuiz> list1 = new ArrayList<>();
        ObservableList<EditQuesFromQuiz> tempList = FXCollections.observableList(list1);
        quizTitle.setText("Editing quiz: " + dataModel.getCurrentQuiz().getName());
        for (Question question : dataModel.getQuestionToQuiz(dataModel.getCurrentQuiz().getQuizID())) {
            EditQuesFromQuiz ques = new EditQuesFromQuiz(question);
            ques.getButton().setOnAction(e -> {
                dataModel.removeQuestionToQuiz(question.getId(), dataModel.getCurrentQuiz().getQuizID());
                list.getItems().remove(ques);
            });
            list1.add(ques);
            ques.setIndex("" + (1 + list1.indexOf(ques)));
        }
        list.setItems(tempList);
        tempList.addListener((ListChangeListener<? super EditQuesFromQuiz>) change -> {
            for (EditQuesFromQuiz ques : list.getItems()) ques.setIndex("" + (1 + list.getItems().indexOf(ques)));
        });
    }

    @FXML
    private void openPopup() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("AddQuesFromBankQuestions.fxml")));
        Parent root = fxmlLoader.load();
        AddQuesFromQuestionsBankController controller = fxmlLoader.getController();
        controller.getButton().setOnAction(e -> {
            stage.close();
        });
        controller.getBtnADD().setOnAction(e -> {
            for (CustomCheckBox question : controller.getQuestions()) {
                dataModel.insertQuestionToQuiz(question.getQuestion().getId(), dataModel.getCurrentQuiz().getQuizID());
                controller.getList().getItems().remove(question);
                EditQuesFromQuiz ques = new EditQuesFromQuiz(question.getQuestion());
                ques.getButton().setOnAction(p -> {
                    dataModel.removeQuestionToQuiz(question.getQuestion().getId(), dataModel.getCurrentQuiz().getQuizID());
                    list.getItems().remove(ques);
                });
                list.getItems().add(ques);
            }
            controller.getList().getSelectionModel().clearSelection();
            controller.getQuestions().clear();
        });
        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        stage.initOwner(owner);
        stage.show();
    }

    @FXML
    private void randomQuestions() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("AddRandomQuestion.fxml")));
        Parent root = fxmlLoader.load();
        AddRandomQuestion controller = fxmlLoader.getController();
        controller.getCloseButton().setOnAction(e -> {
            stage.close();
        });
        controller.getBtnAddQues().setOnAction(e -> {
            for (QuestionsView question : controller.getRandomSelection()) {
                dataModel.insertQuestionToQuiz(question.getQuestion().getId(), dataModel.getCurrentQuiz().getQuizID());
                controller.getList().remove(question);
                EditQuesFromQuiz ques = new EditQuesFromQuiz(question.getQuestion());
                ques.getButton().setOnAction(p -> {
                    dataModel.removeQuestionToQuiz(question.getQuestion().getId(), dataModel.getCurrentQuiz().getQuizID());
                    list.getItems().remove(ques);
                });
                list.getItems().add(ques);
            }
            controller.setPagination();
            controller.totalQuestionProperty(controller.getTotalQuestion() - controller.getRandomSelection().size());
            controller.getRandomSelection().clear();
        });
        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        stage.initOwner(owner);
        stage.show();
    }
}

