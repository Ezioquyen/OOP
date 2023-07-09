package com.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.javafx.FontIcon;

public class QuestionsView extends HBox {
    private final Label title;
    private final Question question;

    public QuestionsView(Question question) {
        setPrefHeight(45);
        setPrefWidth(Region.USE_COMPUTED_SIZE);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(6, 6, 6, 20));
        setSpacing(10);
        setStyle("-fx-background-color: white");
        setStyle("-fx-border-color: black");
        this.question = question;
        title = new Label(question.getTitle());
        title.setMaxWidth(1000);
        title.setStyle("-fx-font-size: 15");
        title.setWrapText(true);
        title.setTextOverrun(OverrunStyle.ELLIPSIS);
        FontIcon listIcon = new FontIcon("fas-list");
        listIcon.setIconSize(15);
        getChildren().addAll(listIcon, title);
    }
    public Question getQuestion() {
        return question;
    }

}
