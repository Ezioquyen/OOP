package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ResultController {
    @FXML
    private Label startTime;
    @FXML
    private Label endTime;
    @FXML
    private Label timeTaken;
    @FXML
    private Label marks;
    @FXML
    private Label marks1;
    @FXML
    private Label marks2;

    @FXML
    private void initialize() {
    }

    public void showInformation(double totalMarksSelected, double totalMarks, String start, String end, String time) {
        startTime.setText(start);
        endTime.setText(end);
        timeTaken.setText(time);
        marks.setText(String.format("%,.2f", totalMarksSelected) + "/" + String.format("%,.2f", totalMarks));
        marks1.setText(String.format("%,.2f", (totalMarksSelected / totalMarks) * 10));
        marks2.setText(String.format("%,.2f", (totalMarksSelected / totalMarks) * 100));
    }
}
