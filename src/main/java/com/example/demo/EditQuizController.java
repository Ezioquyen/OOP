package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditQuizController {

    @FXML
    private ListView<EditQuesFromQuiz> list;
    @FXML
    private TextField grade;
    @FXML
    private Label totalQuestions;
    @FXML
    private Label quizTitle;
    @FXML
    private HBox btnContainer;
    @FXML
    private Button delete;
    @FXML
    private Button cancel;
    private DataModel dataModel;
    private BreadCrumbBarModel breadCrumbBarModel;
    private double totalMarkValue = 0;
    @FXML
    private Label totalMark;
    @FXML
    private CheckBox shuffle;
    private int totalQuestion;

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
        btnContainer.getChildren().removeAll(delete, cancel);
        initDataModel(DataModel.getInstance());
        initBreadCrumbBarModel(BreadCrumbBarModel.getInstance());
        grade.setText(String.format("%,.2f", dataModel.getCurrentQuiz().getMaxGrade()));
        totalQuestions.setText("" + dataModel.getCurrentQuiz().getTotalQuestion());
        totalQuestion = dataModel.getCurrentQuiz().getTotalQuestion();
        List<EditQuesFromQuiz> list1 = new ArrayList<>();
        ObservableList<EditQuesFromQuiz> tempList = FXCollections.observableList(list1);
        quizTitle.setText("Editing quiz: " + dataModel.getCurrentQuiz().getName());
        shuffle.setSelected(dataModel.getCurrentQuiz().getShuffle());
        for (Question question : dataModel.getQuestionToQuiz(dataModel.getCurrentQuiz().getQuizID())) {
            totalMarkValue += question.getMark();
            totalQuestion++;
            EditQuesFromQuiz ques = new EditQuesFromQuiz(question);
            ques.getButton().setOnAction(e -> {
                dataModel.removeQuestionToQuiz(question.getId(), dataModel.getCurrentQuiz().getQuizID());
                list.getItems().remove(ques);
            });
            list1.add(ques);
            ques.setIndex("" + (1 + list1.indexOf(ques)));
        }
        dataModel.getCurrentQuiz().setTotalMarks(totalMarkValue);
        dataModel.getCurrentQuiz().setTotalQuestion(totalQuestion);
        totalQuestions.setText("" + totalQuestion);
        totalMark.setText("Total mark: " + String.format("%,.2f", totalMarkValue));

        list.setItems(tempList);
        tempList.addListener((ListChangeListener<? super EditQuesFromQuiz>) change -> {
            for (EditQuesFromQuiz ques : list.getItems()) ques.setIndex("" + (1 + list.getItems().indexOf(ques)));
        });
        shuffle.selectedProperty().addListener(event -> dataModel.getCurrentQuiz().setShuffle(shuffle.isSelected()));
        delete.setOnAction(e -> {
            for (EditQuesFromQuiz qu : list.getSelectionModel().getSelectedItems()) {
                totalMarkValue -= qu.getQuestion().getMark();
                totalQuestion--;
                dataModel.removeQuestionToQuiz(qu.getQuestion().getId(), dataModel.getCurrentQuiz().getQuizID());
            }
            List<EditQuesFromQuiz> ques = new ArrayList<>(list.getSelectionModel().getSelectedItems());
            list.getSelectionModel().clearSelection();
            list.getItems().removeAll(ques);
            dataModel.getCurrentQuiz().setTotalQuestion(totalQuestion);
            totalQuestions.setText("" + totalQuestion);
            dataModel.getCurrentQuiz().setTotalMarks(totalMarkValue);
            totalMark.setText("Total of marks: " + String.format("%,.2f", totalMarkValue));
        });
        cancel.setOnAction(event -> {
            list.getSelectionModel().clearSelection();
            list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            btnContainer.getChildren().removeAll(delete, cancel);
        });
    }

    @FXML
    private void openPopup() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("AddQuesFromBankQuestions.fxml")));
        Parent root = fxmlLoader.load();
        AddQuesFromQuestionsBankController controller = fxmlLoader.getController();
        controller.getButton().setOnAction(e -> stage.close());
        controller.getBtnADD().setOnAction(e -> {
            for (CustomCheckBox question : controller.getQuestions()) {
                totalMarkValue += question.getQuestion().getMark();
                dataModel.insertQuestionToQuiz(question.getQuestion().getId(), dataModel.getCurrentQuiz().getQuizID());
                controller.getList().getItems().remove(question);
                EditQuesFromQuiz ques = new EditQuesFromQuiz(question.getQuestion());
                ques.getButton().setOnAction(p -> {
                    totalMarkValue -= question.getQuestion().getMark();
                    dataModel.removeQuestionToQuiz(question.getQuestion().getId(), dataModel.getCurrentQuiz().getQuizID());
                    list.getItems().remove(ques);
                    dataModel.getCurrentQuiz().setTotalMarks(totalMarkValue);
                    totalMark.setText("Total of marks: " + String.format("%,.2f", totalMarkValue));
                });
                list.getItems().add(ques);
            }
            controller.getList().getSelectionModel().clearSelection();
            controller.getQuestions().clear();
            dataModel.getCurrentQuiz().setTotalMarks(totalMarkValue);
            totalMark.setText("Total of marks: " + String.format("%,.2f", totalMarkValue));
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
        controller.getCloseButton().setOnAction(e -> stage.close());
        controller.getBtnAddQues().setOnAction(e -> {
            for (QuestionsView question : controller.getRandomSelection()) {
                totalMarkValue += question.getQuestion().getMark();
                dataModel.insertQuestionToQuiz(question.getQuestion().getId(), dataModel.getCurrentQuiz().getQuizID());
                controller.getList().remove(question);
                EditQuesFromQuiz ques = new EditQuesFromQuiz(question.getQuestion());
                ques.getButton().setOnAction(p -> {
                    totalMarkValue -= question.getQuestion().getMark();
                    totalMark.setText("Total of marks: " + String.format("%,.2f", totalMarkValue));
                    dataModel.removeQuestionToQuiz(question.getQuestion().getId(), dataModel.getCurrentQuiz().getQuizID());
                    list.getItems().remove(ques);
                    dataModel.getCurrentQuiz().setTotalMarks(totalMarkValue);

                });
                list.getItems().add(ques);

            }
            controller.totalQuestionProperty(controller.getTotalQuestion() - controller.getRandomSelection().size());
            controller.getRandomSelection().clear();
            controller.setPagination();
            dataModel.getCurrentQuiz().setTotalMarks(totalMarkValue);
            totalMark.setText("Total of marks: " + String.format("%,.2f", totalMarkValue));
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
    private void btnSelectMultipleItem() {
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        btnContainer.getChildren().addAll(delete, cancel);
    }

    @FXML
    private void btnSave() {
        dataModel.getCurrentQuiz().setMaxGrade(Double.parseDouble(grade.getText()));
    }
}

