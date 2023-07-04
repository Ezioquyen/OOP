package com.example.demo;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class QuizCheckBox extends CheckBox {
    private final Button button;
    private final String text;

    QuizCheckBox(String text) {
        this.text = text;
        button = new Button(text);
        setText("");
        getStyleClass().add("checkbox");
        button.getStyleClass().add("checkboxbutton");
        setPadding(new Insets(2));
        setGraphic(button);
    }

    public Button getButton() {
        return button;
    }

    public String getChildText() {
        return text;
    }
}
