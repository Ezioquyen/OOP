package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ResultController {
    private DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
    private final DecimalFormat decimalFormat = new DecimalFormat("#0.00", decimalFormatSymbols);
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

    public void showInformation(double totalMarksSelected, double totalMarks, String start, String end, String time, double grade) {
        startTime.setText(start);
        endTime.setText(end);
        timeTaken.setText(time);
        if (totalMarksSelected < 0) totalMarksSelected = 0;
        marks.setText(decimalFormat.format(totalMarksSelected) + "/" + decimalFormat.format(totalMarks));
        marks1.setText(decimalFormat.format((totalMarksSelected / totalMarks) * grade));
        marks2.setText(decimalFormat.format((totalMarksSelected / totalMarks) * 100));
    }
}
