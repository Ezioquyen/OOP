package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChoiceBoxController {
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private VBox dropZone;
    @FXML
    private Label choice;
    @FXML
    private TextArea textArea;
    @FXML
    private void initialize() {
        List<String> options_String = Arrays.asList("None", "100%", "90%", "83.33333%", "80%", "75%", "70%", "66.66667%", "60%",
                "50%", "40%", "33.33333%", "30%", "25%", "20%", "16.66667%", "14.28571%",
                "12.5%", "11.11111%", "10%", "5%", "-5%", "-10%", "-11.11111%", "-12.5%",
                "-14.28571%", "-16.66667%", "-20%", "-25%", "-30%", "-33.33333%", "-40%",
                "-50%", "-60%", "-66.66667%", "-70%", "-75%", "-80%", "-83.33333%");
        comboBox.getItems().addAll(options_String);
    }

    public void setChoice(String text) {
        this.choice.setText(text);
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public ComboBox<String> getComboBox() {
        return comboBox;
    }
}
