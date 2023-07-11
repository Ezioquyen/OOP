package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ChoiceBoxController {
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private StackPane dropZone;
    @FXML
    private VBox dropShow;
    @FXML
    private VBox dropFace;
    @FXML
    private Button importBtn;
    @FXML
    private Button rmvBtn;
    @FXML
    private Label choice;
    @FXML
    private TextArea textArea;
    private String filePath = null;

    @FXML
    private void initialize() {
        List<String> options_String = Arrays.asList("None", "100%", "90%", "83.33333%", "80%", "75%", "70%", "66.66667%", "60%",
                "50%", "40%", "33.33333%", "30%", "25%", "20%", "16.66667%", "14.28571%",
                "12.5%", "11.11111%", "10%", "5%", "-5%", "-10%", "-11.11111%", "-12.5%",
                "-14.28571%", "-16.66667%", "-20%", "-25%", "-30%", "-33.33333%", "-40%",
                "-50%", "-60%", "-66.66667%", "-70%", "-75%", "-80%", "-83.33333%");
        comboBox.getItems().addAll(options_String);
        dropZone.setOnDragOver(event -> {
            if (event.getGestureSource() != dropZone && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
                event.consume();
            }

        });

        dropZone.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles() && db.getFiles().size() == 1 && dropShow.getChildren().isEmpty() && isImageFile(db.getFiles().get(0))) {
                success = true;
                File file = db.getFiles().get(0);
                dropShow.getChildren().add(new Label(file.getName()));
                dropShow.setVisible(true);
                filePath = file.getAbsolutePath();
                dropFace.setVisible(false);
                importBtn.setDisable(true);
                rmvBtn.setDisable(false);
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public void setChoice(String text) {
        this.choice.setText(text);
    }

    public TextArea getTextArea() {
        return textArea;
    }

    @FXML
    private void btnRemove() {
        dropShow.getChildren().clear();
        dropShow.setVisible(false);
        filePath = null;
        dropFace.setVisible(true);
        importBtn.setDisable(false);
        rmvBtn.setDisable(true);
    }

    @FXML
    private void btnAction() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Chọn tệp");




        // Hiển thị hộp thoại chọn tệp và lấy tệp được chọn
        File selectedFile = fileChooser.showOpenDialog(Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null));


        if (selectedFile != null && isImageFile(selectedFile)) {
            dropShow.getChildren().add(new Label(selectedFile.getName()));
            dropShow.setVisible(true);
            filePath = selectedFile.getAbsolutePath();
            dropFace.setVisible(false);
            importBtn.setDisable(true);
            rmvBtn.setDisable(false);
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

    public ComboBox<String> getComboBox() {
        return comboBox;
    }

    public String getFilePath() {
        return filePath;
    }

    public VBox getDropShow() {
        return dropShow;
    }

    public void showImage(File file) {
        dropShow.getChildren().add(new Label(file.getName()));
        dropShow.setVisible(true);
        filePath = file.getPath();
        dropFace.setVisible(false);
        importBtn.setDisable(true);
        rmvBtn.setDisable(false);
    }

}
