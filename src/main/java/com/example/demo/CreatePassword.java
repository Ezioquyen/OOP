package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class CreatePassword {
    @FXML
    private PasswordField ownPass;
    @FXML
    private PasswordField pass;
    @FXML
    private Button createPass;
    @FXML
    private Button noPass;
    @FXML
    private Button cancel;
    private final ValidationSupport validationSupportPass = new ValidationSupport();
    private final ValidationSupport validationSupportOwnPass = new ValidationSupport();

    @FXML
    private void initialize() {
        validationSupportPass.registerValidator(pass, Validator.createEmptyValidator("require", Severity.ERROR));
        validationSupportOwnPass.registerValidator(ownPass, Validator.createEmptyValidator("require", Severity.ERROR));
    }

    public PasswordField getOwnPass() {
        return ownPass;
    }

    public PasswordField getPass() {
        return pass;
    }

    public Button getCreatePass() {
        return createPass;
    }

    public Button getNoPass() {
        return noPass;
    }

    public Button getCancel() {
        return cancel;
    }

    public ValidationSupport getValidationSupportPass() {
        return validationSupportPass;
    }

    public ValidationSupport getValidationSupportOwnPass() {
        return validationSupportOwnPass;
    }
}
