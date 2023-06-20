package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.stage.*;

import java.io.IOException;
import java.util.Objects;

public class EditQuizController {
    @FXML
    private Label quizTitle;
    private DataModel dataModel;

    public void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    @FXML
    private void initialize() {
        initDataModel(DataModel.getInstance());
        quizTitle.setText("Editing quiz: " + dataModel.getCurrentQuizName());
    }

    @FXML
    private void openPopup() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("AddQuesFromBankQuestions.fxml")));
        Parent root = fxmlLoader.load();
        AddQuizFromQuestionsBankController controller = fxmlLoader.getController();
        controller.getButton().setOnAction(e -> {
            stage.close();
        });
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        stage.initOwner(owner);
        stage.show();
    }
}

