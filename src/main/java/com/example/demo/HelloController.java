package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.BreadCrumbBar;

import java.io.IOException;

public class HelloController {
    @FXML
    private BreadCrumbBar<String> bread;

    @FXML
    private BorderPane pane;
    private TreeItem<String> home, myCourse, thiCuoiKy, questionBank, question, addMTPCQ;

    @FXML
    private void initialize() throws IOException {
        home = new TreeItem<>("Home");
        myCourse = new TreeItem<>("My Course");
        thiCuoiKy = new TreeItem<>("THI CUỐI KỲ");
        questionBank = new TreeItem<>("Question Bank");
        question = new TreeItem<>("Questions ");
        addMTPCQ = new TreeItem<>("Editing a Multiple choice question");
        home.getChildren().addAll(myCourse);
        myCourse.getChildren().addAll(thiCuoiKy);
        thiCuoiKy.getChildren().add(questionBank);
        questionBank.getChildren().add(question);
        question.getChildren().add(addMTPCQ);
        bread.setSelectedCrumb(thiCuoiKy);

        Pane view = FXMLLoader.load(getClass().getResource("thi-cuoi-ky.fxml"));
        pane.setCenter(view);
    }

    @FXML
    private void btnQuestions(ActionEvent event) throws IOException {
        VBox view = FXMLLoader.load(getClass().getResource("questionbank.fxml"));
        view.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        pane.setCenter(view);
    }

    @FXML
    private void btnThiCuoiKy() throws IOException {
        if (thiCuoiKy == bread.getSelectedCrumb()) {
            VBox view = FXMLLoader.load(getClass().getResource("thi-cuoi-ky.fxml"));
            pane.setCenter(view);
        }
    }
}