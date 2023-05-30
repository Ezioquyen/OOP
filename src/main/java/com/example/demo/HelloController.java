package com.example.demo;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
        breadCrumbBarModel.setCurrentView("add-quiz.fxml");
    }

}