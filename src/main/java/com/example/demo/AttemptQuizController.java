package com.example.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class AttemptQuizController {

    private int hour;
    private int minute;
    private int second;
    @FXML
    private Label timerLabel;
    @FXML
    private HBox container;
    @FXML
    private FlowPane navigationContainer;
    private DataModel dataModel;
    @FXML
    private Circle avatar;
    private double totalMark = 0.0;
    @FXML
    private HBox timerContainer;
    @FXML
    private Button export;
    @FXML
    private Button finish;
    @FXML
    private StackPane stackPane;
    private LocalTime startTime;
    private BreadCrumbBarModel breadCrumbBarModel;

    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
    }

    private void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    @FXML
    private ListView<QuestionBox> listQuestion;
    private Timeline timeline;
    private String start;
    private final Map<QuestionBox, QuestionNavigation> map = new HashMap<>();

    @FXML
    private void initialize() {
        initDataModel(DataModel.getInstance());
        initModel(BreadCrumbBarModel.getInstance());
        Boolean shuffle = dataModel.getCurrentQuiz().getShuffle();
        start = getDate();
        if (dataModel.getCurrentQuiz().getTime() != 0) {
            convertMinutesToHoursMinutesSeconds(dataModel.getCurrentQuiz().getTime());
            timerLabel.setText(formatTime(hour, minute, second));
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                this.updateTimer();
                timerLabel.setText(formatTime(hour, minute, second));
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
        startTime = LocalTime.now();
        Image img = new Image(Objects.requireNonNull(getClass().getResource("/hello.jfif")).toExternalForm());
        avatar.setFill(new ImagePattern(img));
        for (Question question : dataModel.getQuestionToQuiz(dataModel.getCurrentQuiz().getQuizID())) {
            question.typeDetect();
            question.match();
            if (shuffle) question.shuffle();
            QuestionBox questionBox = new QuestionBox(question);
            listQuestion.getItems().add(questionBox);
            QuestionNavigation questionNavigation = new QuestionNavigation();
            map.put(questionBox, questionNavigation);
            navigationContainer.getChildren().add(questionNavigation);
            questionNavigation.setOnMouseClicked(e -> listQuestion.scrollTo(listQuestion.getItems().indexOf(questionBox)));
            questionBox.getFlag().setOnAction(event -> {
                if (questionBox.isTriggered()) questionNavigation.getFlag().setFill(Color.valueOf("transparent"));
                else questionNavigation.getFlag().setFill(Color.RED);
                questionBox.setTriggered(!questionBox.isTriggered());
            });

            questionBox.selectedProperty().addListener(e -> {
                if (questionBox.selectedProperty().get()) {
                    questionNavigation.getColor().setStyle("-fx-background-color: gray");
                } else questionNavigation.getColor().setStyle("-fx-background-color: transparent");
            });
            questionNavigation.getNumber().setText("" + (1 + listQuestion.getItems().indexOf(questionBox)));
            questionBox.getNumber().setText("" + (1 + listQuestion.getItems().indexOf(questionBox)));
        }

    }

    private void updateTimer() {
        if (hour == 0 && minute == 0 && second == 0) {
            showResult();
            return;
        }

        second--;
        if (second < 0) {
            second = 59;
            minute--;
            if (minute < 0) {
                minute = 59;
                hour--;
            }
        }
    }

    private String formatTime(int hour, int minute, int second) {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public void convertMinutesToHoursMinutesSeconds(double minutes) {
        int totalMinutes = (int) minutes; // Chuyển đổi sang kiểu int
        this.hour = totalMinutes / 60; // Số giờ
        this.minute = totalMinutes % 60; // Số phút còn lại
        this.second = (int) ((minutes - totalMinutes) * 60); // Số giây
    }

    private void showResult() {
        if (dataModel.getCurrentQuiz().getTime() != 0) timeline.stop();
        LocalTime endTime = LocalTime.now();
        int totalSeconds = (endTime.toSecondOfDay() - startTime.toSecondOfDay());
        int min = totalSeconds / 60;
        int sec = totalSeconds % 60;

        String timeTaken;
        timeTaken = min + " min";
        if (min > 1) timeTaken += "s";
        timeTaken += " ";
        timeTaken += sec + " sec";
        if (sec > 1) timeTaken += "s";

        for (QuestionBox q : listQuestion.getItems()) {
            q.showAns();
            StringBuilder correctAns = new StringBuilder();
            int i = 0;
            for (OptionsPacket pack : q.getQuestion().getPackets()) {
                if (pack.getPercent() > 0) {
                    if (!pack.getOption().isEmpty() && pack.getOption() != null)
                        correctAns.append(" ").append(pack.getOption()).append(",");
                    else correctAns.append(" ").append((char) (65 + i++)).append(",");
                }
            }
            q.getLabel().setText("Correct answer: " + correctAns.substring(0, correctAns.length() - 1) + ".");
            double mark = 0;
            for (OptionsPacket op : q.getSelectedOps()) {
                totalMark += op.getPercent() * q.getQuestion().getMark() / (100);
                mark += op.getPercent() * q.getQuestion().getMark() / (100);
            }
            if ((q.getQuestion().getMark() - mark) < 0.00001) {
                map.get(q).getColor().setStyle("-fx-background-color: green");
            } else map.get(q).getColor().setStyle("-fx-background-color: red");
            map.get(q).getFlag().setFill(Color.TRANSPARENT);
            q.setDisable(true);
        }
        timerContainer.setVisible(false);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("result.fxml"));
        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        ResultController view = fxmlLoader.getController();
        String end = getDate();
        view.showInformation(totalMark, dataModel.getCurrentQuiz().getTotalMarks(), start, end, timeTaken, dataModel.getCurrentQuiz().getMaxGrade());
        stackPane.getChildren().add(parent);
        container.getChildren().remove(finish);
        export.setVisible(true);
    }

    @FXML
    private void btnFinish() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoadera = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Confirmation.fxml")));
        Parent root = fxmlLoadera.load();
        Confirmation controller = fxmlLoadera.getController();
        controller.getStartAttempt().setOnAction(e -> {
            showResult();
            stage.close();
        });
        controller.getCancel().setOnAction(e -> {
            stage.close();
        });
        Scene scene = new Scene(root, 600, 275);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        stage.initOwner(owner);
        stage.show();
    }

    private String getDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy, h:mm a");
        return now.format(formatter);
    }

    @FXML
    private void btnExport() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("quiz.fxml"));
    }
}
