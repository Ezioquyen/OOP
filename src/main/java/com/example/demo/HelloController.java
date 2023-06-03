package com.example.demo;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    @FXML
    private Circle circle_avatar;
    @FXML
    private AnchorPane editingquiz_scene;
    @FXML
    private AnchorPane thigk2CN_scene;
    @FXML
    private MenuButton editingquiz;

    @FXML
    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {

        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
        breadCrumbBarModel.currentViewProperty().addListener((obs, oldView, newView) -> {
            if (!Objects.equals(newView, oldView) && newView != null) {
                this.breadCrumbBarModel.setCurrentTree(this.breadCrumbBarModel.getBreadConnection().get(this.breadCrumbBarModel.getCurrentView()));
                bread.selectedCrumbProperty().set(this.breadCrumbBarModel.getCurrentTree());
            }
        });
        bread.selectedCrumbProperty().addListener((obs, a, b) -> {

            try {
                if (!breadCrumbBarModel.isTabcheck()) {
                    if (breadCrumbBarModel.getBreadConnection().inverse().get(bread.getSelectedCrumb()) != null
                            && !Objects.equals(breadCrumbBarModel.getBreadConnection().inverse().get(bread.getSelectedCrumb().getParent()), "check")) {
                        VBox view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(breadCrumbBarModel.getBreadConnection().inverse().get(bread.getSelectedCrumb()))));
                        pane.getChildren().setAll(view);
                    } else if (Objects.equals(breadCrumbBarModel.getBreadConnection().inverse().get(bread.getSelectedCrumb().getParent()), "check")) {
                        VBox view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("questionbank.fxml")));
                        pane.getChildren().setAll(view);
                    }
                } else breadCrumbBarModel.setTabcheck(false);
            } catch (IOException e) {
                System.out.println("haha");
            }
        });
    }

    @FXML
    private void initialize() throws IOException {
        Pane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("thi-cuoi-ky.fxml")));
        pane.getChildren().setAll(view);
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        initModel(BreadCrumbBarModel.getInstance());
        bread.selectedCrumbProperty().set(breadCrumbBarModel.getCurrentTree());
        //tạo avatar
        Image img = new Image(Objects.requireNonNull(getClass().getResource("/Image/hello.jfif")).toExternalForm());
        circle_avatar.setFill(new ImagePattern(img));
        editingquiz.setOnMouseClicked(mouseEvent -> {
            thigk2CN_scene.setVisible(false);
            editingquiz_scene.setVisible(true);
        });
    }

    @FXML
    private void btnQuestions() {
        breadCrumbBarModel.setTabcheck(false);
        breadCrumbBarModel.setCurrentView("questionbank.fxml");
    }

    @FXML
    private void btnCategory() {
        breadCrumbBarModel.setTabcheck(false);
        breadCrumbBarModel.setCurrentView("1");
    }

    @FXML
    private void btnImport() {
        breadCrumbBarModel.setTabcheck(false);
        breadCrumbBarModel.setCurrentView("2");
    }

    @FXML
    private void btnExport() {
        breadCrumbBarModel.setTabcheck(false);
        breadCrumbBarModel.setCurrentView("3");
    }

    @FXML
    private void btnTurnOnE() {
        breadCrumbBarModel.setTabcheck(false);
        breadCrumbBarModel.setCurrentView("add-quiz.fxml");
    }

}