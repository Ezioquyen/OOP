package com.example.demo;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class QuestionNavigation extends VBox {
    private VBox color = new VBox();
    private Polygon flag = new Polygon();
    private Label number = new Label();

    QuestionNavigation() {
        /*number.setStyle("-fx-font: bold");*/
        setPrefSize(35, 45);
        setStyle("-fx-border-color: black");
        setStyle("-fx-border-style: solid");
        color.setPrefSize(35, 23);
        flag.getPoints().setAll(-421.5999755859375, -251.79986572265625, -388.20001220703125, -227.7998809814453, -388.20001220703125, -251.7998809814453);
        flag.setFill(Color.TRANSPARENT);
        flag.setStroke(Color.TRANSPARENT);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(number, flag);
        getChildren().addAll(stackPane, color);
    }

    public VBox getColor() {
        return color;
    }

    public Polygon getFlag() {
        return flag;
    }

    public Label getNumber() {
        return number;
    }
}
