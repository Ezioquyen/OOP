package com.example.demo;

import com.jfoenix.controls.JFXSnackbar;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.util.Objects;

public class AddQuizController {
    @FXML
    private TextField textField;
    @FXML
    private TextField name;
    @FXML
    private CheckBox checkBox;
    @FXML
    private ComboBox minutes;
    @FXML
    private VBox rootPane;
    private float time = 0;
    private DataModel dataModel;
    private BreadCrumbBarModel breadCrumbBarModel;
    private ValidationSupport validationSupportForName = new ValidationSupport();
    private ValidationSupport validationSupportForTime = new ValidationSupport();

    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
    }

    private void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    @FXML
    private void initialize() {
        initModel(BreadCrumbBarModel.getInstance());
        initDataModel(DataModel.getInstance());
        validationSupportForName.registerValidator(name, Validator.createRegexValidator("Invalid", "(^\\S.*\\S$)|(^\\S+$)", Severity.ERROR));
        validationSupportForTime.registerValidator(textField, Validator.createRegexValidator("Invalid", "^(?=[+]?\\d+(\\.\\d+)?$)(?:0*(?:1000(?:\\.0*)?|\\d{0,3}(?:\\.\\d*)?))$", Severity.ERROR));
        validationSupportForTime.setErrorDecorationEnabled(false);
        checkBox.selectedProperty().addListener(e -> {
            if (checkBox.isSelected()) {
                validationSupportForTime.setErrorDecorationEnabled(true);
                minutes.setDisable(false);
            } else {
                validationSupportForTime.setErrorDecorationEnabled(false);
                minutes.setDisable(true);
            }
        });
    }

    @FXML
    private void btnCreate() {
        boolean check = true;
        for (Quiz quiz : dataModel.getQuizs()) {
            if (Objects.equals(quiz.getName(), name.getText())) {
                snackBarNoti("Quiz existed", false);
                check = false;
                break;
            }
        }
        if (checkBox.isSelected()) {
            time = Float.parseFloat(textField.getText());
            check = validationSupportForTime.getValidationResult().getErrors().isEmpty();
        }

        if (validationSupportForName.getValidationResult().getErrors().isEmpty() && check) {
            dataModel.insertQuiz(name.getText(), time);
            breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("thi-cuoi-ky.fxml"));
        } else snackBarNoti("Invalid value", false);
    }

    @FXML
    private void btnCancel() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("thi-cuoi-ky.fxml"));
    }

    private void snackBarNoti(String text, boolean check) {
        JFXSnackbar snackbar = new JFXSnackbar(rootPane);
        if (check) {
            snackbar.setId("snack1");
        } else snackbar.setId("snack2");
        snackbar.show(text, 4000);

    }
}
