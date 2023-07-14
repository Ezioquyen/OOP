package com.example.demo;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.controlsfx.validation.ValidationSupport;

import java.io.IOException;
import java.util.Objects;

public class HelloController {

    @FXML
    private VBox pane;
    @FXML
    private VBox pane1;
    @FXML
    private HBox bar;
    private BreadCrumbBarModel breadCrumbBarModel;
    private DataModel dataModel;
    @FXML
    private MenuButton menu;
    @FXML
    private Button editButton;
    @FXML
    private HBox menuContainer;
    @FXML
    private HBox buttonEditionContainer;
    @FXML
    private Circle circle_avatar;

    public void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    @FXML
    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {

        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
    }

    @FXML
    private void initialize() throws IOException {
        Pane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("thi-cuoi-ky.fxml")));
        pane.getChildren().setAll(view);
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        initDataModel(DataModel.getInstance());
        initModel(BreadCrumbBarModel.getInstance());
        bar.getChildren().add(breadCrumbBarModel.getBreadCrumbBar());
        //Listener
        breadCrumbBarModel.getBreadCrumbBar().selectedCrumbProperty().addListener((obs, oldVal, newVal) -> {
            if (!breadCrumbBarModel.isToggle()) {
                if (Objects.equals(oldVal.getValue(), "Edit quiz")) {
                    dataModel.updateQuiz(dataModel.getCurrentQuiz());
                }
                if (Objects.equals(oldVal.getValue(), "THI CUỐI KỲ")) {
                    menuContainer.getChildren().remove(menu);
                    buttonEditionContainer.getChildren().remove(editButton);
                }
                if (Objects.equals(newVal.getValue(), "THI CUỐI KỲ")) {
                    menuContainer.getChildren().add(menu);
                    buttonEditionContainer.getChildren().add(editButton);
                }
                if (Objects.equals(oldVal.getValue(), "Edit multiple choice question")) {
                    pane.setVisible(true);
                    pane1.setVisible(false);
                }
                if (breadCrumbBarModel.getBreadConnection().inverse().get(newVal) == null) {
                    breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(oldVal);
                } else {
                    if (Objects.equals(newVal.getParent().getValue(), "Question Bank") && (!(Objects.equals(oldVal.getValue(), "Edit multiple choice question") && Objects.equals(newVal.getValue(), "Questions")))) {
                        try {
                            breadCrumbBarModel.setCurrentView(breadCrumbBarModel.getBreadConnection().inverse().get(newVal));
                            VBox vBox = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("questionbank.fxml")));
                            pane.getChildren().setAll(vBox);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (Objects.equals(newVal.getValue(), "Edit multiple choice question")) {
                        pane.setVisible(false);
                        pane1.setVisible(true);
                        VBox vBox;
                        try {
                            vBox = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(breadCrumbBarModel.getBreadConnection().inverse().get(newVal))));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        pane1.getChildren().setAll(vBox);
                    } else if (Objects.equals(oldVal.getValue(), "Edit multiple choice question") && Objects.equals(newVal.getValue(), "Questions")) {
                        pane.setVisible(true);
                        pane1.setVisible(false);
                        pane1.getChildren().removeAll();
                    } else {
                        VBox vBox;
                        try {
                            vBox = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(breadCrumbBarModel.getBreadConnection().inverse().get(newVal))));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        pane.getChildren().setAll(vBox);
                    }
                }
            }
            breadCrumbBarModel.setToggle(false);

        });
        //tạo avatar
        Image img = new Image(Objects.requireNonNull(getClass().getResource("/hello.jfif")).toExternalForm());
        circle_avatar.setFill(new ImagePattern(img));
    }

    @FXML
    private void btnQuestions() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("questionbank.fxml"));
    }

    @FXML
    private void btnCategory() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("1"));
    }

    @FXML
    private void btnImport() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("2"));
    }

    @FXML
    private void btnExport() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("3"));
    }

    @FXML
    private void btnTurnOnE() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("add-quiz.fxml"));
    }

}