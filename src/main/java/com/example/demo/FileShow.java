package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

public class FileShow extends HBox {
    private final Button remove = new Button();

    public FileShow(File file) {
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        Label label = new Label();
        label.setText(file.getName());
        label.setWrapText(true);
        remove.setStyle("-fx-background-color: transparent");
        remove.setGraphic(new FontIcon("fas-times"));
        getChildren().addAll(label, remove);
    }

    public Button getRemove() {
        return remove;
    }
}
