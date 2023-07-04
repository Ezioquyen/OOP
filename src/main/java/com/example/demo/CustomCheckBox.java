package com.example.demo;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.scene.control.Button;

public class CustomCheckBox extends StackPane {
    private final CheckBox checkBox = new CheckBox("");
    private final Button button = new Button("Edit");
    private final Label label = new Label();
    private Question question;
    private final HBox box = new HBox();

    public CustomCheckBox(Question question) {
        setAlignment(Pos.CENTER_RIGHT);
        setPrefWidth(Region.USE_COMPUTED_SIZE);
        setPrefHeight(Region.USE_COMPUTED_SIZE);
        setPadding(new Insets(0, 5, 0, 0));
        this.question = question;

        box.setPrefHeight(30);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(0, 0, 0, 5));
        box.setSpacing(5);

        label.setText(question.getTitle());
        label.setMaxWidth(1000);
        label.setStyle("-fx-text-fill: black");
        label.setWrapText(true);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);

        button.setTextFill(Color.valueOf("#00ACEA"));
        button.getStyleClass().add("checkBox-MenuButton");
        button.setContentDisplay(ContentDisplay.RIGHT);
        Region region = new Region();
        FontIcon fontIcon = new FontIcon("fas-list-ul");
        FontIcon fontIcon1 = new FontIcon("fas-caret-down");
        fontIcon1.setIconColor(Color.valueOf("#00ACEA"));
        button.setGraphic(fontIcon1);

        box.getChildren().addAll(checkBox, fontIcon, label, region, button);
        HBox.setHgrow(region, Priority.ALWAYS);
        getChildren().add(box);
    }

    public Button getButton() {
        return button;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public Label getLabel() {
        return label;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setLabel(String text) {
        this.label.setText(text);
    }

    public HBox getBox() {
        return box;
    }
}
