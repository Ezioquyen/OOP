package com.example.demo;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class QuizCheckBox extends CheckBox {
    QuizCheckBox(String text) {
        setText("");
        getStyleClass().add("checkbox");
        Button button = new Button(text);
        button.getStyleClass().add("checkboxbutton");
        setPadding(new Insets(2));
        setGraphic(button);
    }
}
