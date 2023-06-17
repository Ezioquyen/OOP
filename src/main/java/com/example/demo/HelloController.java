package com.example.demo;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.image.Image;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.controlsfx.control.BreadCrumbBar;
import java.io.IOException;
import java.util.Objects;

public class HelloController {

    @FXML
    private VBox pane;
    @FXML
    private BreadCrumbBar<String> bread;
    private BreadCrumbBarModel breadCrumbBarModel;
    private DataModel dataModel;
    @FXML
    private Circle circle_avatar;
    private VBox tmp;
    private int toggle = 0;

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

        breadCrumbBarModel.currentViewProperty().addListener((obs, oldView, newView) -> {
            if (toggle != 1) {
                toggle = 2;
                if (!Objects.equals(newView, oldView) && newView != null) {
                    this.breadCrumbBarModel.setCurrentTree(this.breadCrumbBarModel.getBreadConnection().get(this.breadCrumbBarModel.getCurrentView()));
                    bread.selectedCrumbProperty().set(this.breadCrumbBarModel.getCurrentTree());
                    try {
                        VBox view = null;
                        if (Objects.equals(breadCrumbBarModel.getBreadConnection().get(newView).getParent().getValue(), "Question Bank")) {
                            if (!breadCrumbBarModel.isTabCheck()) {
                                view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("questionbank.fxml")));
                                pane.getChildren().setAll(view);
                            } else breadCrumbBarModel.setTabCheck(false);
                        } else {
                            view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(newView)));
                            pane.getChildren().setAll(view);
                        }


                    } catch (IOException | NullPointerException e) {
                        throw new RuntimeException(e);
                    }

                }

            } else toggle = 0;

        });
        bread.selectedCrumbProperty().addListener((obs, a, b) -> {
            if (toggle != 2) {
                if (Objects.equals(breadCrumbBarModel.getBreadConnection().inverse().get(b), null)) {

                    toggle = 2;
                    bread.selectedCrumbProperty().set(a);
                    toggle = 0;
                } else {

                    try {
                        VBox view = null;
                        if (Objects.equals(b.getParent().getValue(), "Question Bank")) {
                            if (!breadCrumbBarModel.isTabCheck()) {
                                view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("questionbank.fxml")));
                                pane.getChildren().setAll(view);
                            } else breadCrumbBarModel.setTabCheck(false);
                        } else {
                            view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(breadCrumbBarModel.getBreadConnection().inverse().get(b))));
                            pane.getChildren().setAll(view);
                        }


                    } catch (IOException | NullPointerException e) {
                        throw new RuntimeException(e);
                    }

                }
            } else toggle = 0;

        });

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
        bread.selectedCrumbProperty().set(breadCrumbBarModel.getCurrentTree());
        //tạo avatar
        Image img = new Image(Objects.requireNonNull(getClass().getResource("/hello.jfif")).toExternalForm());
        circle_avatar.setFill(new ImagePattern(img));
        toggle = 0;
    }

    @FXML
    private void btnQuestions() {
        breadCrumbBarModel.setCurrentView("questionbank.fxml");
    }

    @FXML
    private void btnCategory() {
        breadCrumbBarModel.setCurrentView("1");
    }

    @FXML
    private void btnImport() {
        breadCrumbBarModel.setCurrentView("2");
    }

    @FXML
    private void btnExport() {
        breadCrumbBarModel.setCurrentView("3");
    }

    @FXML
    private void btnTurnOnE() {
        breadCrumbBarModel.setCurrentView("add-quiz.fxml");
    }

}