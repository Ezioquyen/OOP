package com.example.demo;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QuestionBox extends HBox {
    private final Question question;
    private final Label number = new Label();
    private final VBox box1 = new VBox(15);

    private final Button flag = new Button("Flag");
    private Label label;
    private Label answered = new Label("Not answered");
    private VBox questionContainer = new VBox();
    private VBox optionsContainer = new VBox(15);
    private SimpleBooleanProperty selected = new SimpleBooleanProperty(false);
    private boolean triggered = false;

    private List<String> selectedOps = new ArrayList<>();

    public void showAns() {
        HBox ans = new HBox();
        ans.setAlignment(Pos.CENTER_LEFT);
        ans.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        ans.setStyle("-fx-background-color: #FCEFDC");
        ans.setPadding(new Insets(0, 0, 0, 20));
        label = new Label();
        label.setMinHeight(50);
        label.setWrapText(true);
        label.setPrefWidth(720);
        label.setStyle("-fx-text-fill: #695442");
        ans.getChildren().add(label);
        box1.getChildren().add(ans);
    }

    private String getAns() {
        return "";
    }

    public Label getLabel() {
        return label;
    }

    public QuestionBox(Question question) {
        this.question = question;
        setId("box");
        //
        setPrefWidth(877.0);
        setSpacing(20);
        setPrefHeight(USE_COMPUTED_SIZE);
        //
        VBox box = new VBox();
        box.setPrefSize(116, 288.0);

        VBox boxChild = new VBox(5);
        boxChild.setPadding(new Insets(10));
        boxChild.setStyle("-fx-border-color: #DFDFDF");

        HBox boxChild1 = new HBox();
        boxChild1.setAlignment(Pos.BOTTOM_LEFT);
        Label questionLabel = new Label("Question");
        questionLabel.setStyle("-fx-font-size: 14");
        questionLabel.setStyle("-fx-text-fill:#c02424");
        number.setAlignment(Pos.BOTTOM_LEFT);
        number.setStyle("-fx-font-size: 20");
        number.setStyle("-fx-text-fill: #c02424");
        /*number.setStyle("-fx-font: ");*/
        boxChild1.getChildren().addAll(questionLabel, number);

        answered.setStyle("-fx-text-fill: black");

        Label markForQues = new Label("Marked out of " + String.format("%,.2f", question.getMark()));
        markForQues.setWrapText(true);
        markForQues.setStyle("-fx-text-fill: black");
        flag.setPrefSize(97, 26);
        FontIcon flagIcon = new FontIcon("fas-flag");
        flag.setGraphic(flagIcon);
        flag.setContentDisplay(ContentDisplay.LEFT);
        flag.setStyle("-fx-font-size: 10.0");
        flag.setStyle("-fx-background-color: white");

        Label editQuestion = new Label("Edit question");
        editQuestion.setStyle("-fx-text-fill: #009fe5");
        FontIcon cog = new FontIcon("fas-cog");
        cog.setIconColor(Color.valueOf("#009fe5"));
        cog.setIconSize(15);
        editQuestion.setGraphic(cog);

        boxChild.getChildren().addAll(boxChild1, answered, markForQues, flag, editQuestion);

        box.getChildren().add(boxChild);
        //
        setHgrow(box1, Priority.ALWAYS);

        questionContainer.setSpacing(5);
        questionContainer.setPrefSize(592, 225);
        questionContainer.setStyle("-fx-background-color: #E7F3F5");
        questionContainer.setPadding(new Insets(10));

        HBox hBox = new HBox();
        Label title = new Label();
        title.setPrefWidth(720);
        title.setStyle("-fx-font-size: 15");
        title.setWrapText(true);
        title.setStyle("-fx-text-fill: black");

        hBox.getChildren().add(title);

        optionsContainer.setPadding(new Insets(0, 0, 0, 15));

        questionContainer.getChildren().addAll(hBox, optionsContainer);

        box1.getChildren().add(questionContainer);


        //
        title.setText(question.getTitle());
        if (question.isType() == 1) {
            ToggleGroup group = new ToggleGroup();
            for (String option : question.getOptions()) {
                RadioButton radioButton = new RadioButton(option);
                radioButton.setStyle("-fx-text-fill: black");
                group.getToggles().add(radioButton);
                optionsContainer.getChildren().add(radioButton);
            }
            group.selectedToggleProperty().addListener(e -> {
                selectedOps.clear();
                selected.set(group.getSelectedToggle() != null);
                selectedOps.add(((RadioButton) group.getSelectedToggle()).getText());
            });
        } else if (question.isType() == 0) {
            List<CheckBox> list = new ArrayList<>();
            ObservableList<CheckBox> checkBoxObservableList = FXCollections.observableList(list);
            for (String option : question.getOptions()) {
                CheckBox checkBox = new CheckBox(option);
                checkBox.setStyle("-fx-text-fill: black");
                optionsContainer.getChildren().add(checkBox);
                checkBox.selectedProperty().addListener(e -> {
                    if (checkBox.isSelected()) {
                        checkBoxObservableList.add(checkBox);
                        selectedOps.add(checkBox.getText());
                    } else {
                        checkBoxObservableList.remove(checkBox);
                        selectedOps.remove(checkBox.getText());
                    }
                });
                checkBoxObservableList.addListener((ListChangeListener<? super CheckBox>) e -> {

                    selected.set(!checkBoxObservableList.isEmpty());

                });
            }


        }
        getChildren().addAll(box, box1);
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public Label getNumber() {
        return number;
    }

    public Button getFlag() {
        return flag;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public Question getQuestion() {
        return question;
    }

    public List<String> getSelectedOps() {
        return selectedOps;
    }
}
