package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;

public class QuizController {
    @FXML
    private Label quizName;
    private DataModel dataModel;
    private BreadCrumbBarModel breadCrumbBarModel;

    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
    }

    public void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    @FXML
    private void initialize() {
        initModel(BreadCrumbBarModel.getInstance());
        initDataModel(DataModel.getInstance());
        quizName.setText(dataModel.getCurrentQuiz().getName());
    }

    @FXML
    private void btnQuizEditing() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("EditQuiz.fxml"));
    }

    @FXML
    private void openPopup() throws IOException {
        if (dataModel.getCurrentQuiz().getTime() != 0) {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Noti.fxml")));
            Parent root = fxmlLoader.load();
            NotiController controller = fxmlLoader.getController();
            controller.getText().setText("Your attempt wil have time limit of " + dataModel.getCurrentQuiz().getTime() + " minutes. When you start, the timer will begin to count down and cannot paused. You must finish your attempt before it expires. Are you sure you wish start now?");
            controller.getCloseButton().setOnAction(e -> {
                stage.close();
            });
            controller.getStartAttempt().setOnAction(e -> {
                breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("AttemptQuiz.fxml"));
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
        } else
            breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("AttemptQuiz.fxml"));
    }
}
