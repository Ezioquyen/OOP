package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.BreadCrumbBar;

import java.io.IOException;
import java.util.Map;

public class HelloController {
    @FXML
    private BreadCrumbBar<String> bread;

    @FXML
    private BorderPane pane;
    private TreeItem<String> tree, tree1, tree2;

    @FXML
    private void initialize() throws IOException {
        tree = new TreeItem<>("Home");
        tree1 = new TreeItem<>("My Course");
        tree2 = new TreeItem<>("THI CUỐI KỲ");
        tree.getChildren().addAll(tree1);
        tree1.getChildren().addAll(tree2);
        bread.setSelectedCrumb(tree2);
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
        if (tree2 == bread.getSelectedCrumb()) {
            VBox view = FXMLLoader.load(getClass().getResource("thi-cuoi-ky.fxml"));
            pane.setCenter(view);
        }
    }
}