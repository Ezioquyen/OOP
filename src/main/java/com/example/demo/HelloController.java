package com.example.demo;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.image.Image;

import javafx.scene.layout.HBox;
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
    private HBox bar;
    private BreadCrumbBarModel breadCrumbBarModel;
    private DataModel dataModel;
    @FXML
    private Circle circle_avatar;
    /*private VBox tmp;*/
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
                if (breadCrumbBarModel.getBreadConnection().inverse().get(newVal) == null) {
                    breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(oldVal);
                } else {
                    if (Objects.equals(newVal.getParent().getValue(), "Question Bank")) {
                        try {
                            breadCrumbBarModel.setCurrentView(breadCrumbBarModel.getBreadConnection().inverse().get(newVal));
                            VBox vBox = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("questionbank.fxml")));
                            pane.getChildren().setAll(vBox);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        VBox vBox = null;
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
        ;
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