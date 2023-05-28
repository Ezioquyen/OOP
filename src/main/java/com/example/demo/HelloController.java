package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.BreadCrumbBar;

import java.io.IOException;

public class HelloController {

    @FXML
    private VBox pane;
    @FXML
    private BreadCrumbBar<String> bread;
    private BreadCrumbBarModel breadCrumbBarModel;

    @FXML
    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
        breadCrumbBarModel.currentViewProperty().addListener((obs, oldView, newView) -> {
            if (newView != oldView && newView != null) {
                this.breadCrumbBarModel.setCurrentTree(this.breadCrumbBarModel.getBreadConnection().get(this.breadCrumbBarModel.getCurrentView()));
                bread.setSelectedCrumb(this.breadCrumbBarModel.getCurrentTree());
            }
        });
    }

    @FXML
    private void initialize() throws IOException {
        Pane view = FXMLLoader.load(getClass().getResource("thi-cuoi-ky.fxml"));
        pane.getChildren().setAll(view);
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        initModel(BreadCrumbBarModel.getInstance());
        bread.setSelectedCrumb(breadCrumbBarModel.getCurrentTree());
    }

    @FXML
    private void btnQuestions(ActionEvent event) throws IOException {
        breadCrumbBarModel.setCurrentView("questionbank.fxml");
        VBox view = FXMLLoader.load(getClass().getResource("questionbank.fxml"));
        view.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        pane.getChildren().setAll(view);
    }

    @FXML
    private void btnThiCuoiKy() throws IOException {

        VBox view = FXMLLoader.load(getClass().getResource("thi-cuoi-ky.fxml"));
        pane.getChildren().setAll(view);

    }

    @FXML
    private void btnTurnOnE(ActionEvent event) throws IOException {

        VBox view = FXMLLoader.load(getClass().getResource("add-quiz.fxml"));
        pane.getChildren().setAll(view);
    }
    /*@FXML
    private void btnConnectDB(ActionEvent event) {

    }*/
}