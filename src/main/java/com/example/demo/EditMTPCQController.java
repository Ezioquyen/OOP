package com.example.demo;

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
import org.apache.poi.ss.formula.SheetRangeIdentifier;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private int preNumberOfChoice = 0;
    private int currentChoice = 0;
    private DataModel dataModel;
    private BreadCrumbBarModel breadCrumbBarModel;
    private final List<ChoiceBoxController> controllers = new ArrayList<>();
    private List<File> files = new ArrayList<>();

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
        quesName.setText(dataModel.getCurrentQuestion().getTitle());
        mark.setText("" + dataModel.getCurrentQuestion().getMark());

        for (String string : dataModel.getCurrentQuestion().getImageFilePath()) {
            if (string != null) {
                File file = new File(string);
                files.add(file);
                dropShow.getChildren().add(new Label(file.getName()));
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
            showChoices.getChildren().add(parent);
        }
        root.setRoot(dataModel.getRoot());
        root.getSelectionModel().select(dataModel.getCurrentCategory());
        label.setText(root.getSelectionModel().getSelectedItem().getValue());
        root.getSelectionModel().selectedItemProperty().addListener(e -> {
            label.setText(root.getSelectionModel().getSelectedItem().getValue());
        });
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
                Image image = new Image(file.getPath());
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
    }

    @FXML
    private void btnSaveAndContinueEditing() throws NullPointerException {
        List<String> options = new ArrayList<>();
        List<Double> percent = new ArrayList<>();
        List<String> opPath = new ArrayList<>();
        dataModel.getCurrentQuestion().setTitle(quesName.getText());
        dataModel.getCurrentQuestion().setMark(Double.valueOf(mark.getText()));
        int index = 0;
        for (ChoiceBoxController controller : controllers) {
            if (!(controller.getTextArea().getText().isEmpty() && controller.getDropShow().getChildren().isEmpty())) {
                options.add(controller.getTextArea().getText());
                dataModel.getCurrentQuestion().getImageOptionPath().set(index, controller.getFilePath());
                if (Objects.equals(controller.getComboBox().getSelectionModel().getSelectedItem(), "None") || controller.getComboBox().getSelectionModel().getSelectedItem() == null) {
                    percent.add((double) 0);
                } else
                    percent.add(Double.valueOf(controller.getComboBox().getSelectionModel().getSelectedItem().substring(0, controller.getComboBox().getSelectionModel().getSelectedItem().length() - 1)));
            }
            index++;
        }
        dataModel.getCurrentQuestion().setOptions(options);
        dataModel.getCurrentQuestion().setPercent(percent);
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
        /*dataModel.updateImagePath(dataModel.getCurrentQuestion());*/
        if (files.size() > dataModel.getCurrentQuestion().getImageFilePath().size()) {
            for (int i = dataModel.getCurrentQuestion().getImageFilePath().size() + 1; i <= files.size(); i++) {
                dataModel.getCurrentQuestion().getImageFilePath().add(files.get(i).getPath());
                dataModel.insertImage(files.get(i).getPath(), dataModel.getCurrentQuestion().getId());
            }
        }/*else if(files.size()<dataModel.getCurrentQuestion().getImageFilePath().size()){

        }*/
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
}
