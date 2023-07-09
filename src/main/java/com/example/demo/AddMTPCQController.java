package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddMTPCQController {
    @FXML
    private VBox showChoices;
    @FXML
    private TreeView<String> root;
    @FXML
    private Label label;
    @FXML
    private TextField quesName;
    @FXML
    private TextField mark;
    @FXML
    private StackPane dropZone;
    @FXML
    private VBox dropShow;
    @FXML
    private VBox dropFace;

    private int currentChoice = 0;
    private DataModel dataModel;
    private BreadCrumbBarModel breadCrumbBarModel;
    private final List<ChoiceBoxController> controllers = new ArrayList<>();
    private final List<File> files = new ArrayList<>();

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
        for (int i = 1; i <= 2; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("choice-box.fxml"));
            Parent parent = fxmlLoader.load();
            ChoiceBoxController view = fxmlLoader.getController();
            view.setChoice("Choice " + (i + currentChoice));
            controllers.add(view);
            showChoices.getChildren().add(parent);
        }
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
            view.setChoice("Choice " + (i + currentChoice));
            controllers.add(view);
            showChoices.getChildren().add(parent);
        }
        currentChoice += 3;
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

                List<File> dropfile = new ArrayList<>(db.getFiles());
                isImageFile(dropfile);
                if (!dropfile.isEmpty()) {
                    dropShow.setVisible(true);
                    for (File file : dropfile) {
                        dropShow.getChildren().add(new Label(file.getName()));
                        files.add(file);
                    }
                    dropFace.setVisible(false);
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
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

    @FXML
    private void btnSaveAndContinueEditing() throws NullPointerException {
        Question question = new Question();
        question.addTitle(quesName.getText());
        question.setMark(Double.valueOf(mark.getText()));
        for (ChoiceBoxController controller : controllers) {
            if (!(controller.getTextArea().getText().isEmpty() && controller.getDropShow().getChildren().isEmpty())) {
                question.getOptions().add(controller.getTextArea().getText());
                if (!controller.getDropShow().getChildren().isEmpty()) {
                    question.setImageOptionPath(controller.getFilePath());
                }
                if (Objects.equals(controller.getComboBox().getSelectionModel().getSelectedItem(), "None") || controller.getComboBox().getSelectionModel().getSelectedItem() == null) {
                    question.getPercent().add((double) 0);
                } else
                    question.getPercent().add(Double.valueOf(controller.getComboBox().getSelectionModel().getSelectedItem().substring(0, controller.getComboBox().getSelectionModel().getSelectedItem().length() - 1)));
            }
        }
        question.typeDetect();
        dataModel.insertQuestion(root.getSelectionModel().getSelectedItem(), question.getTitle(), question.isType(), Double.valueOf(mark.getText()));

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
    }

    @FXML
    private void btnSaveChanges() throws NullPointerException {
        btnSaveAndContinueEditing();
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("questionbank.fxml"));
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
        Stage stage = new Stage();

        // Hiển thị hộp thoại chọn tệp và lấy tệp được chọn
        File selectedFile = fileChooser.showOpenDialog(stage);


        if (selectedFile != null && isImageFile(selectedFile)) {
            if (dropShow.getChildren().isEmpty()) {
                dropShow.setVisible(true);
                dropFace.setVisible(false);
            }
            dropShow.getChildren().add(new Label(selectedFile.getName()));
            files.add(selectedFile);
        }
    }

}
