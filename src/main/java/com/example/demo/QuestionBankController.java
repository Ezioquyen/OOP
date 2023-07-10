package com.example.demo;

import com.jfoenix.controls.JFXSnackbar;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.poi.xwpf.usermodel.*;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class QuestionBankController {
    private BreadCrumbBarModel breadCrumbBarModel;
    @FXML
    private TabPane tabPane;
    @FXML
    private TreeView<String> root;
    @FXML
    private Label label;
    @FXML
    private TreeView<String> root1;
    @FXML
    private Label label1;
    @FXML
    private VBox dropZone;
    @FXML
    private TextField catogeryName;
    @FXML
    private CheckBox showQuesFromCate;
    @FXML
    private VBox showInfor;
    @FXML
    private VBox dropZoneInterface;
    @FXML
    private VBox rootPane;
    @FXML
    private VBox fileNameShow;
    @FXML
    private ListView<CustomCheckBox> list;
    @FXML
    private Button btnChooseFile;
    private DataModel dataModel;
    private File file;
    private int countLine = 0;
    private final List<CustomCheckBox> quesFromSubcategories = new ArrayList<>();

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

    private ValidationSupport validationSupport = new ValidationSupport();

    @FXML
    private void initialize() {
        initDataModel(DataModel.getInstance());// design partten single ton
        initModel(BreadCrumbBarModel.getInstance());
        showInfor.setVisible(false);
        validationSupport.registerValidator(catogeryName, Validator.createRegexValidator("Invalid value", "(^\\S.*\\S$)|(^\\S+$)", Severity.ERROR));
        showQuesFromCate.selectedProperty().addListener(e -> {
            if (root.getSelectionModel().getSelectedItem() != null) {
                if (showQuesFromCate.isSelected()) {
                    traverseTreeView(root.getSelectionModel().getSelectedItem());
                    list.getItems().addAll(quesFromSubcategories);
                } else {
                    list.getItems().removeAll(quesFromSubcategories);
                    quesFromSubcategories.clear();
                    showInfor.setVisible(!list.getItems().isEmpty());
                }
            }
        });
        root.setRoot(dataModel.getRoot());
        root.getSelectionModel().selectedItemProperty().addListener(e -> {
            list.getItems().clear();
            quesFromSubcategories.clear();
            showQuestion();
            showInfor.setVisible(!list.getItems().isEmpty());
            updateCategorySelection();
        });
        root1.setRoot(dataModel.getRoot());
        root1.getSelectionModel().selectedItemProperty().addListener(e -> label1.setText(root1.getSelectionModel().getSelectedItem().getValue()));

        if (Objects.equals(breadCrumbBarModel.getCurrentView(), "questionbank.fxml")) {
            tabPane.getSelectionModel().select(0);
        } else if (!Objects.equals(breadCrumbBarModel.getCurrentView(), "questionbank.fxml"))
            tabPane.getSelectionModel().select(Integer.parseInt(breadCrumbBarModel.getCurrentView()));
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, b, a) -> {
            if (a != b) {
                breadCrumbBarModel.setToggle(true);
                switch (tabPane.getSelectionModel().getSelectedIndex()) {
                    case 1 ->
                            breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("1"));
                    case 2 ->
                            breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("2"));
                    case 3 ->
                            breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("3"));
                    default ->
                            breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("questionbank.fxml"));
                }

            }
        });
        dropZone.setOnDragOver(event -> {
            if (event.getGestureSource() != dropZone && event.getDragboard().hasFiles() && event.getDragboard().getFiles().size() == 1 && fileNameShow.getChildren().isEmpty()) {
                event.acceptTransferModes(TransferMode.COPY);
                event.consume();
            }

        });

        dropZone.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasFiles()) {
                file = dragboard.getFiles().get(0);
                dropZoneInterface.setVisible(false);
                FileShow fileShow = new FileShow(file);
                fileShow.getRemove().setOnAction(e -> {
                    file = null;
                    btnChooseFile.setDisable(false);
                    dropZoneInterface.setVisible(true);
                    fileNameShow.setVisible(false);
                    fileNameShow.getChildren().remove(fileShow);
                });
                fileNameShow.getChildren().add(fileShow);
                fileNameShow.setVisible(true);
                btnChooseFile.setDisable(true);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private boolean isTextFile(File file) {
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return fileExtension.equalsIgnoreCase("txt") || fileExtension.equalsIgnoreCase("doc") || fileExtension.equalsIgnoreCase("docx");
    }

    private void updateCategorySelection() {
        dataModel.setCurrentCategory(root.getSelectionModel().getSelectedItem());
        label.setText(root.getSelectionModel().getSelectedItem().getValue());
    }

    @FXML
    private void btnAddQuestion() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("add-MTPCQ.fxml"));
    }

    @FXML
    private void textAction() {
        if (root1.getSelectionModel().isEmpty()) {
            snackBarNoti("No category selected", false);
        } else {
            if (!validationSupport.getValidationResult().getErrors().isEmpty()) {
                for (ValidationMessage msg : validationSupport.getValidationResult().getErrors()) {
                    snackBarNoti(msg.getText(), false);
                }
            } else {
                if (dataModel.insertCategory(root1.getSelectionModel().getSelectedItem(), catogeryName.getText()))
                    snackBarNoti("Import category successful", true);
                else snackBarNoti("Category existed", false);
            }
        }
    }

    private boolean haveTitle(Question question) {
        return question.getTitle().isEmpty();
    }

    public List<Question> readAikenQuestions(File file) {
        List<Question> questions = new ArrayList<>();
        countLine = 0;
        boolean haveAns = false;
        if (file.getName().substring(file.getName().lastIndexOf(".") + 1).equalsIgnoreCase("docx")) {
            try {
                FileInputStream fis = new FileInputStream(file);
                XWPFDocument document = new XWPFDocument(fis);
                Question question = new Question();
                String imageFileName;
                for (XWPFParagraph paragraph : document.getParagraphs()) {
                    countLine++;
                    String text = paragraph.getText().trim();
                    if (haveTitle(question)) {
                        question.addTitle(text);
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            List<XWPFPicture> pictures = run.getEmbeddedPictures();
                            int imageIndex = 0;
                            for (XWPFPicture picture : pictures) {
                                XWPFPictureData pictureData = picture.getPictureData();
                                LocalTime time = LocalTime.now();
                                LocalDate currentDate = LocalDate.now();

                                // Định dạng ngày tháng năm
                                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss");
                                imageFileName = "D:\\TestFolder\\" + currentDate.format(formatter2) + "-" + time.format(formatter) + countLine + imageIndex + ".png";
                                question.setImageFilePath(imageFileName);
                                try (OutputStream os = new FileOutputStream(imageFileName)) {
                                    os.write(pictureData.getData());
                                }
                                imageIndex++;
                            }
                        }
                    } else {
                        if (text.matches("^[A-Z]\\.\\s.*")) {
                            if (!text.startsWith(String.valueOf((char) ((question.getOptions().size()) + 65)))) {
                                return null;
                            }
                            question.addOption(text.substring("A. ".length()).trim());
                        } else if (text.matches("^ANSWER:\\s[A-Z]") && question.getOptions().size() >= 2) {
                            haveAns = true;
                            for (String a : question.getOptions()) {
                                if (question.getOptions().indexOf(a) == text.substring("ANSWER:".length()).trim().charAt(0) - 65) {
                                    question.getPercent().add(100.0);
                                } else question.getPercent().add(0.0);
                            }
                        } else if (text.isEmpty() && haveAns) {
                            question.setMark(1.0);
                            question.typeDetect();

                            questions.add(question);
                            question = new Question();
                            haveAns = false;
                        } else {
                            return null;
                        }
                    }
                }
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                Question question = new Question();
                while ((line = reader.readLine()) != null) {
                    String text = line.trim();
                    countLine++;

                    if (haveTitle(question)) {
                        question.addTitle(text);
                    } else {
                        if (text.matches("^[A-Z]\\.\\s.*")) {
                            if (!text.startsWith(String.valueOf((char) ((question.getOptions().size()) + 65)))) {
                                return null;
                            }
                            question.addOption(text.substring("A. ".length()).trim());
                        } else if (text.matches("^ANSWER:\\s[A-Z]") && question.getOptions().size() >= 2) {
                            for (String a : question.getOptions()) {
                                haveAns = true;
                                if (question.getOptions().indexOf(a) == text.substring("ANSWER:".length()).trim().charAt(0) - 65) {
                                    question.getPercent().add(100.0);
                                } else question.getPercent().add(0.0);
                            }
                        } else if (text.isEmpty() && haveAns) {
                            question.setMark(1.0);
                            question.typeDetect();
                            questions.add(question);
                            question = new Question();
                            haveAns = false;
                        } else {
                            return null;
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return questions;
    }

    public void btnImport() {
        if (file == null) {
            snackBarNoti("No file selected", false);
        } else {
            if (!isTextFile(file)) {
                snackBarNoti(file.getName() + " is not text file", false);
            } else {
                int i = 0;
                int count = 0;
                if (dataModel.getLastNode_id() != 0) {
                    Random random = new Random();
                    i = random.nextInt(dataModel.getLastNode_id()) + 1;
                } else {
                    LocalDate currentDate = LocalDate.now();

                    // Định dạng ngày tháng năm
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    // Chuyển đổi thành chuỗi và in ra
                    String formattedDate = currentDate.format(formatter);
                    dataModel.insertCategory(dataModel.getCategoryMap().inverse().get(i), formattedDate);
                    i = 1;

                }
                try {
                    for (Question question : readAikenQuestions(file)) {
                        dataModel.insertQuestion(dataModel.getCategoryMap().inverse().get(i), question.getTitle(), question.isType(), 1.0);
                        if (!question.getImageFilePath().isEmpty()) {
                            for (String string : question.getImageFilePath()) dataModel.insertImage(string, 0);
                        }
                        dataModel.insertAnswers(question.getOptions(), question.getPercent(), 0, null);
                        count++;
                    }
                    dataModel.setCount(count);
                    dataModel.updateCategory(dataModel.getCategoryMap().inverse().get(i));
                    label1.setText(dataModel.getCategoryMap().inverse().get(i).getValue());
                    dataModel.setCount(0);
                    fileNameShow.getChildren().clear();
                    fileNameShow.setVisible(false);
                    dropZoneInterface.setVisible(true);
                    btnChooseFile.setDisable(false);
                    file = null;
                    snackBarNoti("Imported successful", true);
                } catch (NullPointerException e) {
                    snackBarNoti("Wrong question format: Error at line " + countLine, false);
                }
            }
        }
        /*} catch (NullPointerException e) {
            System.out.println("No file selected");
        }*/
    }

    public void chooseFile() {
        // Tạo một đối tượng FileChooser
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Chọn tệp");


        // Lấy stage của scene hiện tại từ nút
        Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);

        // Hiển thị hộp thoại chọn tệp và lấy tệp được chọn
        File selectedFile = fileChooser.showOpenDialog(owner);
        if (selectedFile != null) {
            dropZoneInterface.setVisible(false);
            fileNameShow.setVisible(true);
            file = selectedFile;
            FileShow fileShow = new FileShow(file);
            fileShow.getRemove().setOnAction(e -> {
                file = null;
                btnChooseFile.setDisable(false);
                dropZoneInterface.setVisible(true);
                fileNameShow.setVisible(false);
                fileNameShow.getChildren().remove(fileShow);
            });
            fileNameShow.getChildren().add(fileShow);
            btnChooseFile.setDisable(true);
        }

    }

    private void showQuestion() {

        if (root.getSelectionModel().getSelectedItem() != null) {
            for (Question question : dataModel.getQuestion(dataModel.getCategoryMap().get(root.getSelectionModel().getSelectedItem()))) {

                CustomCheckBox customCheckBox = new CustomCheckBox(question);
                customCheckBox.getButton().setOnAction(event -> {
                    dataModel.setCurrentQuestion(customCheckBox.getQuestion());
                    breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("edit-MTPCQ.fxml"));
                });

                list.getItems().add(customCheckBox);
            }
            if (showQuesFromCate.isSelected()) {
                traverseTreeView(root.getSelectionModel().getSelectedItem());
                list.getItems().addAll(quesFromSubcategories);
            }
            list.getSelectionModel().selectedItemProperty().addListener(observable -> {
                if (list.getSelectionModel().getSelectedItem() != null) {
                    list.getSelectionModel().getSelectedItem().getCheckBox().setSelected(!list.getSelectionModel().getSelectedItem().getCheckBox().isSelected());

                }
            });
        }
    }

    private void snackBarNoti(String text, boolean check) {
        JFXSnackbar snackbar = new JFXSnackbar(rootPane);
        if (check) {
            snackbar.setId("snack1");
        } else snackbar.setId("snack2");
        snackbar.show(text, 4000);

    }

    private void traverseTreeView(TreeItem<String> root) {
        if (root != null) {
            for (TreeItem<String> child : root.getChildren()) {
                traverseTreeView(child);
                for (Question question : dataModel.getQuestion(dataModel.getCategoryMap().get(child))) {
                    CustomCheckBox customCheckBox = new CustomCheckBox(question);
                    customCheckBox.getButton().setOnAction(event -> {
                        dataModel.setCurrentQuestion(customCheckBox.getQuestion());
                        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("edit-MTPCQ.fxml"));
                    });
                    quesFromSubcategories.add(customCheckBox);
                }
            }
        }
    }
}
