package com.example.demo;

import com.jfoenix.controls.JFXSnackbar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.abs;

public class EditMTPCQController {
    @FXML
    private VBox showChoices;
    @FXML
    private TreeView<String> root;
    @FXML
    private MenuButton category;
    @FXML
    private Label label;
    @FXML
    private TextField mark;
    @FXML
    private TextField quesName;
    @FXML
    private StackPane dropZone;
    @FXML
    private VBox dropShow;
    @FXML
    private VBox dropFace;
    @FXML
    private VBox rootPane;
    private int preNumberOfChoice = 0;
    private int currentChoice = 0;
    private DataModel dataModel;
    private BreadCrumbBarModel breadCrumbBarModel;
    private final List<ChoiceBoxController> controllers = new ArrayList<>();
    private final List<File> files = new ArrayList<>();
    private final List<ValidationSupport> validationSupportList = new ArrayList<>();


    public void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
    }

    @FXML
    private void initialize() throws IOException {
        initDataModel(DataModel.getInstance());
        initModel(BreadCrumbBarModel.getInstance());

        ValidationSupport validationSupportForQuestionName = new ValidationSupport();
        ValidationSupport validationSupportForMark = new ValidationSupport();
        validationSupportForQuestionName.registerValidator(quesName, Validator.createRegexValidator("Wrong input", "(^\\S.*\\S$)|(^\\S+$)", Severity.ERROR));

        validationSupportForMark.registerValidator(mark, Validator.createRegexValidator("Mark must be positive", "^(?=[+]?\\d+(\\.\\d+)?$)(?:0*(?:1000(?:\\.0*)?|\\d{0,3}(?:\\.\\d*)?))$", Severity.ERROR));
        validationSupportList.add(validationSupportForQuestionName);
        validationSupportList.add(validationSupportForMark);
        quesName.setText(dataModel.getCurrentQuestion().getTitle());
        mark.setText("" + dataModel.getCurrentQuestion().getMark());

        for (String string : dataModel.getCurrentQuestion().getImageFilePath()) {
            if (string != null) {
                File file = new File(string);
                FileShow fileShow = new FileShow(file);
                fileShow.getRemove().setOnAction(e -> {
                    dropShow.getChildren().remove(fileShow);
                    files.remove(file);
                    if (dropShow.getChildren().isEmpty()) {
                        dropShow.setVisible(false);
                        dropFace.setVisible(true);
                    }
                });
                dropShow.getChildren().add(fileShow);
                files.add(file);
            }
        }
        if (!dropShow.getChildren().isEmpty()) {
            dropShow.setVisible(true);
            dropFace.setVisible(false);
        }
        for (String option : dataModel.getCurrentQuestion().getOptions()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("choice-box.fxml"));
            Parent parent = fxmlLoader.load();
            ChoiceBoxController view = fxmlLoader.getController();
            view.setChoice("Choice " + (1 + currentChoice));
            view.getTextArea().setText(option);
            ValidationSupport validationSupport = new ValidationSupport();
            validationSupport.registerValidator(view.getTextArea(), Validator.createRegexValidator("Invalid Value", "(^\\S.*\\S$)|(^$)|(^\\S+$)", Severity.ERROR));
            showChoices.getChildren().add(parent);
            validationSupportList.add(validationSupport);
            view.getTextArea().promptTextProperty().addListener(e -> {
                validationSupport.setErrorDecorationEnabled(!view.getTextArea().getText().isEmpty());
            });
            if (dataModel.getCurrentQuestion().getImageOptionPath().get(currentChoice) != null) {
                view.showImage(new File(dataModel.getCurrentQuestion().getImageOptionPath().get(currentChoice)));
            }
            if (dataModel.getCurrentQuestion().getPercent().get(currentChoice) == 0.0)
                view.getComboBox().getSelectionModel().select("None");
            else {
                DecimalFormat decimalFormat = new DecimalFormat("#.######");
                decimalFormat.setGroupingUsed(false);
                view.getComboBox().getSelectionModel().select(decimalFormat.format(dataModel.getCurrentQuestion().getPercent().get(currentChoice)) + "%");
            }
            currentChoice++;
            controllers.add(view);
        }
        root.setRoot(dataModel.getRoot());
        root.getSelectionModel().select(dataModel.getCurrentCategory());
        label.setText(root.getSelectionModel().getSelectedItem().getValue());
        root.getSelectionModel().selectedItemProperty().addListener(e -> label.setText(root.getSelectionModel().getSelectedItem().getValue()));
        preNumberOfChoice = currentChoice;
        category.setDisable(true);
        dropZone.setOnDragOver(event -> {
            if (event.getGestureSource() != dropZone && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
                event.consume();
            }

        });

        dropZone.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;

                List<File> dropFile = new ArrayList<>(db.getFiles());
                isImageFile(dropFile);
                if (!dropFile.isEmpty()) {
                    dropShow.setVisible(true);
                    for (File file : dropFile) {
                        FileShow fileShow = new FileShow(file);
                        fileShow.getRemove().setOnAction(e -> {
                            dropShow.getChildren().remove(fileShow);
                            files.remove(file);
                            if (dropShow.getChildren().isEmpty()) {
                                dropShow.setVisible(false);
                                dropFace.setVisible(true);
                            }
                        });
                        dropShow.getChildren().add(fileShow);
                    }
                    dropFace.setVisible(false);
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }

    private void isImageFile(List<File> files) {
        boolean check = false;
        for (File file : files) {
            try {
                new Image(file.getPath());
            } catch (Exception e) {
                e.printStackTrace();
                check = true;
                files.remove(file);
            }
        }
        if (check) snackBarNoti("Wrong file format please try again", false);
    }

    private boolean isImageFile(File file) {
        try {
            Image image = new Image(file.getPath());
            return !image.isError();
        } catch (Exception e) {
            e.printStackTrace();
            snackBarNoti("Wrong file format please try again", false);
            return false;
        }
    }

    @FXML
    private void btnAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn tệp");
        // Lấy stage của scene hiện tại từ nút
        Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);

        // Hiển thị hộp thoại chọn tệp và lấy tệp được chọn
        File selectedFile = fileChooser.showOpenDialog(owner);

        if (selectedFile != null && isImageFile(selectedFile)) {
            FileShow fileShow = new FileShow(selectedFile);
            if (dropShow.getChildren().isEmpty()) {
                dropShow.setVisible(true);
                dropFace.setVisible(false);
            }
            fileShow.getRemove().setOnAction(e -> {
                dropShow.getChildren().remove(fileShow);
                files.remove(selectedFile);
                if (dropShow.getChildren().isEmpty()) {
                    dropShow.setVisible(false);
                    dropFace.setVisible(true);
                }
            });
            dropShow.getChildren().add(fileShow);
            files.add(selectedFile);
        }
    }

    @FXML
    private void btnAddThreeMoreChoices() throws IOException {
        for (int i = 1; i <= 3; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("choice-box.fxml"));
            Parent parent = fxmlLoader.load();
            ChoiceBoxController view = fxmlLoader.getController();
            ValidationSupport validationSupport = new ValidationSupport();
            validationSupport.registerValidator(view.getTextArea(), Validator.createRegexValidator("Invalid Value", "(^\\S.*\\S$)|(^$)|(^\\S+$)", Severity.ERROR));
            view.setChoice("Choice " + (i + currentChoice));
            validationSupportList.add(validationSupport);
            controllers.add(view);
            showChoices.getChildren().add(parent);
        }
        currentChoice += 3;
    }

    private boolean validChecker() {
        for (ValidationSupport validSupport : validationSupportList) {
            if (!validSupport.getValidationResult().getErrors().isEmpty()) {
                snackBarNoti("Invalid value please check again", false);
                return false;
            }
        }
        List<String> options = new ArrayList<>();
        List<Double> percent = new ArrayList<>();
        List<String> opPath = dataModel.getCurrentQuestion().getImageOptionPath();
        int index = 0;
        for (ChoiceBoxController controller : controllers) {
            if (!(controller.getTextArea().getText().isEmpty() && controller.getDropShow().getChildren().isEmpty())) {
                options.add(controller.getTextArea().getText());
                if (opPath.size() <= index) opPath.add(index, controller.getFilePath());
                else opPath.set(index, controller.getFilePath());
                if (Objects.equals(controller.getComboBox().getSelectionModel().getSelectedItem(), "None") || controller.getComboBox().getSelectionModel().getSelectedItem() == null) {
                    percent.add((double) 0);
                } else
                    percent.add(Double.valueOf(controller.getComboBox().getSelectionModel().getSelectedItem().substring(0, controller.getComboBox().getSelectionModel().getSelectedItem().length() - 1)));
            }
            index++;
        }
        if (options.size() < 2) {
            snackBarNoti("Question must have atleast two options", false);
            return false;
        }
        double sum = 0;
        for (Double value : percent) {
            if (value > 0) sum = sum + value;
        }
        if (abs(100.0 - sum) < 0.00001) {
            dataModel.getCurrentQuestion().setTitle(quesName.getText());
            dataModel.getCurrentQuestion().setMark(Double.valueOf(mark.getText()));
            dataModel.getCurrentQuestion().setOptions(options);
            dataModel.getCurrentQuestion().setPercent(percent);
            dataModel.getCurrentQuestion().setImageOptionPath(opPath);
            index = 0;
            dataModel.getCurrentQuestion().getImageFilePath().clear();
            for (File file : files) {
                dataModel.getCurrentQuestion().getImageFilePath().add(index, file.getAbsolutePath());
                index++;
            }
            dataModel.getCurrentQuestion().typeDetect();
            if (dataModel.getCurrentQuestion().getAnsID().size() > options.size()) {
                dataModel.getCurrentQuestion().getAnsID().subList(options.size(), dataModel.getCurrentQuestion().getAnsID().size()).clear();
            }
            dataModel.updateQuestion(dataModel.getCurrentQuestion());
            if (options.size() > preNumberOfChoice) {
                List<String> newOption = new ArrayList<>();
                List<Double> newPercent = new ArrayList<>();
                List<String> newPath = new ArrayList<>();
                for (int i = preNumberOfChoice; i < options.size(); i++) {
                    newOption.add(dataModel.getCurrentQuestion().getOptions().get(i));
                    newPercent.add(dataModel.getCurrentQuestion().getPercent().get(i));
                    newPath.add(dataModel.getCurrentQuestion().getImageOptionPath().get(i));
                }
                dataModel.insertAnswers(newOption, newPercent, dataModel.getCurrentQuestion().getId(), newPath);
            } else if (options.size() < preNumberOfChoice) {
                dataModel.deleteAns(dataModel.getCurrentQuestion());
            }
            if (files.size() > 0) dataModel.updateImagePath(dataModel.getCurrentQuestion());
            if (files.size() > dataModel.getCurrentQuestion().getImageID().size()) {
                for (int i = dataModel.getCurrentQuestion().getImageID().size() + 1; i <= files.size(); i++) {
                    dataModel.insertImage(files.get(i - 1).getPath(), dataModel.getCurrentQuestion().getId());
                }
            } else if (files.size() < dataModel.getCurrentQuestion().getImageID().size()) {
                dataModel.deleteImagePath(dataModel.getCurrentQuestion());
            }
        } else {
            snackBarNoti("Invalid total grade (max of total grade must be 100%)", false);
            return false;
        }
        return true;
    }

    @FXML
    private void btnSaveAndContinueEditing() throws NullPointerException {
        if (validChecker()) {
            snackBarNoti("Saved", true);
        }
    }

    @FXML
    private void btnSaveChanges() throws NullPointerException {
        if (validChecker())
            breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("questionbank.fxml"));
    }

    @FXML
    private void btnCancel() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("questionbank.fxml"));
    }

    private void snackBarNoti(String text, boolean check) {
        JFXSnackbar snackbar = new JFXSnackbar(rootPane);
        if (check) {
            snackbar.setId("snack1");
        } else snackbar.setId("snack2");
        snackbar.show(text, 4000);

    }
}
