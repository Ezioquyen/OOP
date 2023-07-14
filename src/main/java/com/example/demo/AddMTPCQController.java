package com.example.demo;
import com.jfoenix.controls.JFXSnackbar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.validation.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.abs;

public class AddMTPCQController {
    @FXML
    private VBox showChoices;
    @FXML
    private TreeView<String> root;
    @FXML
    private Label label;

    @FXML
    private TextArea quesTitle;
    @FXML
    private TextField quesName;
    @FXML
    private Button getVideo;
    @FXML
    private TextField mark;
    @FXML
    private StackPane dropZone;
    @FXML
    private VBox dropShow;
    @FXML
    private VBox dropFace;
    @FXML
    private VBox rootPane;
    private int currentChoice = 0;
    private DataModel dataModel;
    private BreadCrumbBarModel breadCrumbBarModel;
    private final List<String> VIDEO_EXTENSIONS = Arrays.asList(".mp4", ".avi", ".mkv", ".mov", ".wmv");
    private final List<ChoiceBoxController> controllers = new ArrayList<>();
    private final List<File> files = new ArrayList<>();

    private final List<ValidationSupport> validationSupportList = new ArrayList<>();
    private File video;
    @FXML
    private HBox containerVid;

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
        validationSupportForQuestionName.registerValidator(quesName, Validator.createRegexValidator("Wrong input", "(^\\S.*\\S$)|(^\\S+$)", Severity.ERROR));
        ValidationSupport validationSupportForqusâestionTitle = new ValidationSupport();
        validationSupportForqusâestionTitle.registerValidator(quesTitle, Validator.createRegexValidator("Wrong input", "(^\\S.*\\S$)|(^\\S+$)", Severity.ERROR));
        ValidationSupport validationSupportForMark = new ValidationSupport();
        validationSupportForMark.registerValidator(mark, Validator.createRegexValidator("Mark must be positive", "^(?=[+]?\\d+(\\.\\d+)?$)(?:0*(?:100(?:\\.0*)?|\\d{0,2}(?:\\.\\d*)?))$", Severity.ERROR));
        validationSupportList.add(validationSupportForQuestionName);
        validationSupportList.add(validationSupportForqusâestionTitle);
        validationSupportList.add(validationSupportForMark);

        for (int i = 1; i <= 2; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("choice-box.fxml"));
            Parent parent = fxmlLoader.load();
            ChoiceBoxController view = fxmlLoader.getController();
            view.setChoice("Choice " + (i + currentChoice));
            controllers.add(view);
            ValidationSupport validationSupport = new ValidationSupport();
            validationSupport.registerValidator(view.getTextArea(), Validator.createRegexValidator("Invalid Value", "(^\\S.*\\S$)|(^$)|(^\\S+$)", Severity.ERROR));
            showChoices.getChildren().add(parent);
        }
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
        currentChoice = 2;
        root.setRoot(dataModel.getRoot());
        root.getSelectionModel().selectedItemProperty().addListener(e -> label.setText(root.getSelectionModel().getSelectedItem().getValue()));

    }

    @FXML
    private void btnAddThreeMoreChoices() throws IOException {
        for (int i = 1; i <= 3; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("choice-box.fxml"));
            Parent parent = fxmlLoader.load();
            ChoiceBoxController view = fxmlLoader.getController();
            ValidationSupport validationSupport = new ValidationSupport();
            validationSupport.registerValidator(view.getTextArea(), Validator.createRegexValidator("Error", "(^\\S.*\\S$)|(^$)|(^\\S+$)", Severity.ERROR));
            view.setChoice("Choice " + (i + currentChoice));
            validationSupportList.add(validationSupport);
            controllers.add(view);
            showChoices.getChildren().add(parent);
        }
        currentChoice += 3;


    }

    private void isImageFile(List<File> files) {
        for (File file : files) {
            try {
                new Image(file.getPath());
            } catch (Exception e) {
                e.printStackTrace();
                files.remove(file);
            }
        }
    }

    private boolean isImageFile(File file) {
        try {
            Image image = new Image(file.getPath());
            return !image.isError();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean validChecker() {
        if (root.getSelectionModel().getSelectedItem() == null) {
            snackBarNoti("No category selected", false);
            return false;
        }
        for (ChoiceBoxController view : controllers) {
            if (view.getTextArea().getParent().getChildrenUnmodifiable().size() == 2) {
                view.getTextArea().getParent().getChildrenUnmodifiable().remove(1);
            }
        }
        for (ValidationSupport validSupport : validationSupportList) {
            if (!validSupport.getValidationResult().getErrors().isEmpty()) {
                snackBarNoti("Invalid value please check again", false);
                return false;
            }
        }

        Question question = new Question();
        question.addTitle(quesName.getText() + ": " + quesTitle.getText());
        question.setMark(Double.valueOf(mark.getText()));
        String vidPath = null;
        if (video != null) vidPath = video.getAbsolutePath();
        for (ChoiceBoxController controller : controllers) {
            if (!(controller.getTextArea().getText().isEmpty() && controller.getDropShow().getChildren().isEmpty())) {
                question.getOptions().add(controller.getTextArea().getText());
                if (!controller.getDropShow().getChildren().isEmpty()) {
                    question.getImageOptionPath().add(controller.getFilePath());
                }
                if (Objects.equals(controller.getComboBox().getSelectionModel().getSelectedItem(), "None") || controller.getComboBox().getSelectionModel().getSelectedItem() == null) {
                    question.getPercent().add((double) 0);
                } else
                    question.getPercent().add(Double.valueOf(controller.getComboBox().getSelectionModel().getSelectedItem().substring(0, controller.getComboBox().getSelectionModel().getSelectedItem().length() - 1)));
            }
        }
        question.typeDetect();
        double sum = 0;
        for (Double value : question.getPercent()) {
            if (value > 0) sum = sum + value;
        }
        if (abs(100 - sum) < 0.00001) {
            dataModel.insertQuestion(root.getSelectionModel().getSelectedItem(), question.getTitle(), question.isType(), Double.valueOf(mark.getText()), vidPath);
            dataModel.insertAnswers(question.getOptions(), question.getPercent(), 0, question.getImageOptionPath());
            if (!files.isEmpty()) {
                for (File file : files) {
                    question.getImageFilePath().add(file.getPath());
                    dataModel.insertImage(file.getAbsolutePath(), 0);
                }
            }
            dataModel.setCount(1);
            dataModel.updateCategory(root.getSelectionModel().getSelectedItem());
            dataModel.setCount(0);

        } else {
            snackBarNoti("Invalid total grade (max of total grade must be 100%)", false);
            return false;
        }
        return true;
    }

    @FXML
    private void btnSaveAndContinueEditing() throws NullPointerException {
        if (validChecker()) {
            snackBarNoti("Add Question Successful", true);
        }
    }

    @FXML
    private void btnSaveChanges() throws NullPointerException {
        if (validChecker()) {
            breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("questionbank.fxml"));
        }

    }

    @FXML
    private void btnCancel() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("questionbank.fxml"));
    }

    @FXML
    private void btnAction() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Chọn tệp");


        // Lấy stage của scene hiện tại từ nút

        // Hiển thị hộp thoại chọn tệp và lấy tệp được chọn
        File selectedFile = fileChooser.showOpenDialog(Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null));


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

    private void snackBarNoti(String text, boolean check) {
        JFXSnackbar snackbar = new JFXSnackbar(rootPane);
        if (check) {
            snackbar.setId("snack1");
        } else snackbar.setId("snack2");
        snackbar.show(text, 4000);

    }

    @FXML
    private void btnInsertVideo() throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Chọn tệp");


        // Lấy stage của scene hiện tại từ nút

        // Hiển thị hộp thoại chọn tệp và lấy tệp được chọn
        File selectedFile = fileChooser.showOpenDialog(Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null));


        if (selectedFile != null && isVideoFile(selectedFile.getAbsolutePath())) {

            video = selectedFile;
            FileShow fileShow = new FileShow(video);
            containerVid.getChildren().add(fileShow);
            fileShow.getRemove().setOnAction(e -> {
                containerVid.getChildren().remove(fileShow);
                getVideo.setDisable(false);
                video = null;
            });
            getVideo.setDisable(true);

        }
    }

    private boolean isVideoFile(String filePath) {
        File file = new File(filePath);
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));

        return VIDEO_EXTENSIONS.contains(fileExtension.toLowerCase());
    }

}
