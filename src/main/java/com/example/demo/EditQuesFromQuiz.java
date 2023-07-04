package com.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class EditQuesFromQuiz extends HBox {

    private Question question;
    private final Label index;

    private final Label questionTitle;

    private Label point;

    private Button button;

    public EditQuesFromQuiz(Question question) {
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(6));
        setSpacing(10);
        prefHeight(35);
        setStyle("-fx-background-color:#E6E6E6");
        setPrefWidth(Region.USE_COMPUTED_SIZE);
        setPrefHeight(Region.USE_COMPUTED_SIZE);
        FontIcon arrowAlt = new FontIcon("fas-arrows-alt");
        arrowAlt.setIconSize(15);
        arrowAlt.setIconColor(Color.valueOf("#029fe5"));
        index = new Label();
        index.prefHeight(20);
        index.prefWidth(20);
        index.minHeight(USE_PREF_SIZE);
        index.minWidth(USE_PREF_SIZE);
        index.setStyle("-fx-background-color: #D3D3D3");
        index.setAlignment(Pos.CENTER_LEFT);
        index.setStyle("-fx-text-fill: black");
        FontIcon listAlt = new FontIcon("fas-list");
        FontIcon cog = new FontIcon("fas-cog");
        cog.setIconColor(Color.valueOf("#029fe5"));
        cog.setIconSize(15);
        questionTitle = new Label(question.getTitle());
        questionTitle.setText(question.getTitle());
        questionTitle.setMaxWidth(800);
        questionTitle.setMaxHeight(20);
        questionTitle.setWrapText(true);
        questionTitle.setTextOverrun(OverrunStyle.ELLIPSIS);
        questionTitle.setStyle("-fx-text-fill: black");
        Region region = new Region();
        FontIcon bin = new FontIcon("fas-trash-alt");
        bin.setIconColor(Color.valueOf("#029fe5"));
        button = new Button();
        button.setGraphic(bin);
        button.setText("");
        button.setStyle("-fx-background-color: transparent");
        FontIcon pen = new FontIcon("fas-pen");
        pen.setIconColor(Color.valueOf("#029fe5"));
        point = new Label("" + question.getMark());
        point.setAlignment(Pos.CENTER);
        point.setContentDisplay(ContentDisplay.RIGHT);
        point.setGraphic(pen);
        point.prefWidth(50);
        point.prefHeight(30);
        point.setStyle("-fx-background-color: white");
        point.setStyle("-fx-text-fill: black");
        FontIcon search = new FontIcon("fas-search-plus");
        search.setIconColor(Color.valueOf("#029fe5"));
        getChildren().addAll(arrowAlt, index, listAlt, cog, questionTitle, region, button, search, point);
        HBox.setHgrow(region, Priority.ALWAYS);
    }

    public Label getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index.setText(index);
    }

    public Label getQuestionTitle() {
        return questionTitle;
    }


    public Label getPoint() {
        return point;
    }

    public void setPoint(Label point) {
        this.point = point;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
