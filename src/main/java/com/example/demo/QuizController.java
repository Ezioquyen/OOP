package com.example.demo;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXSnackbar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.min;

public class QuizController {
    @FXML
    private Label quizName;
    @FXML
    private Label time;
    @FXML
    private VBox rootPane;
    private DataModel dataModel;
    private BreadCrumbBarModel breadCrumbBarModel;

    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
    }

    public void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    @FXML
    private void initialize() {
        initModel(BreadCrumbBarModel.getInstance());
        initDataModel(DataModel.getInstance());
        quizName.setText(dataModel.getCurrentQuiz().getName());
        if (dataModel.getCurrentQuiz().getTime() != 0) {
            int min = (int) (dataModel.getCurrentQuiz().getTime());
            int sec = (int) ((dataModel.getCurrentQuiz().getTime() % 1) * 60);
            String timeTaken = "Time limit: ";
            timeTaken += min + " minute";
            if (min > 1) timeTaken += "s";
            timeTaken += " ";
            timeTaken += sec + " second";
            if (sec > 1) timeTaken += "s";
            time.setText(timeTaken);
        } else time.setText("Time limit: not applied");
    }

    @FXML
    private void btnQuizEditing() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("EditQuiz.fxml"));
    }

    @FXML
    private void openPopup() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Noti.fxml")));
        Parent root = fxmlLoader.load();
        NotiController controller = fxmlLoader.getController();
        if (dataModel.getCurrentQuiz().getTime() != 0)
            controller.getText().setText("Your attempt wil have time limit of " + String.format("%,.0f", dataModel.getCurrentQuiz().getTime()) + " minutes. When you start, the timer will begin to count down and cannot paused. You must finish your attempt before it expires. Are you sure you wish start now?");
        else controller.getText().setText("Your attempt is not applied time limit");
        controller.getCloseButton().setOnAction(e -> {
            stage.close();
        });
        controller.getStartAttempt().setOnAction(e -> {
            breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("AttemptQuiz.fxml"));
            stage.close();
        });
        controller.getCancel().setOnAction(e -> {
            stage.close();
        });
        controller.getExport().setOnAction(e -> {
            try {
                createPass();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            stage.close();
        });
        Scene scene = new Scene(root, 600, 275);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        stage.initOwner(owner);
        stage.show();
    }

    private void export(boolean havePass, String oP, String p) throws FileNotFoundException {
        List<Question> questions = dataModel.getQuestionToQuiz(dataModel.getCurrentQuiz().getQuizID());
        createSampleData(questions, havePass, oP, p);
    }

    private void createSampleData(List<Question> questions, boolean havePass, String oP, String p) throws FileNotFoundException {
        Document document = new Document();
        LocalTime time = LocalTime.now();
        LocalDate currentDate = LocalDate.now();

        // Định dạng ngày tháng năm
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss");
        String outPath = "D:\\TestFolder\\" + currentDate.format(formatter2) + "-" + time.format(formatter) + dataModel.getCurrentQuiz().getQuizID() + ".pdf";
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));
            if (havePass)
                writer.setEncryption(p.getBytes(), oP.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_256);
            document.open();
            String fontName = "D:\\OOP\\Demo\\src\\main\\resources\\font\\arial.ttf";
            BaseFont baseFont = BaseFont.createFont(fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont);
            float maxWidth = 500f;
            float maxHeight = 500f;
            for (Question question : questions) {
                document.add(new Paragraph(question.getTitle(), font));
                if (!question.getImageFilePath().isEmpty()) {
                    for (String path : question.getImageFilePath()) {
                        Image image = Image.getInstance(path);
                        image.scaleToFit(maxWidth, maxHeight);
                        document.add(image);
                    }
                }
                int j = 0;
                for (String option : question.getOptions()) {
                    String ans = ((char) (65 + j) + ". " + option);
                    if (question.getImageOptionPath().get(j) != null) {
                        PdfPTable table = new PdfPTable(2);
                        table.setWidthPercentage(100);
                        table.setWidths(new int[]{min(question.getOptions().get(j).length() + 2, 50), 50});
                        // Đường dẫn đến file ảnh
                        // Tạo ô với văn bản và ảnh
                        PdfPCell cell1 = new PdfPCell(new Paragraph(ans, font));
                        cell1.setBorder(Rectangle.NO_BORDER);
                        PdfPCell cell2 = new PdfPCell();
                        cell2.setBorder(Rectangle.NO_BORDER);
                        float maxWidthOp = 50f;
                        float maxHeightOp = 50f;
                        Image image = Image.getInstance(question.getImageOptionPath().get(j));
                        image.scaleToFit(maxWidthOp, maxHeightOp);
                        cell2.addElement(image);
                        // Thêm ô vào bảng
                        table.addCell(cell1);
                        table.addCell(cell2);

                        // Thêm bảng vào tài liệu PDF
                        document.add(table);
                    } else document.add(new Paragraph(ans, font));
                    j++;
                }
                StringBuilder correctAns = new StringBuilder();
                j = 0;
                for (String ignored : question.getOptions()) {
                    if (question.getPercent().get(j) > 0) {
                        correctAns.append(" ").append((char) (65 + j)).append(",");
                    }
                    j++;
                }
                String out = "Correct answer: " + correctAns.substring(0, correctAns.length() - 1) + ".";
                document.add(new Paragraph(out, font));
                document.add(new Paragraph(" "));
            }
            document.close();
            writer.close();

        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void snackBarNoti(String text, boolean check) {
        JFXSnackbar snackbar = new JFXSnackbar(rootPane);
        if (check) {
            snackbar.setId("snack1");
        } else snackbar.setId("snack2");
        snackbar.show(text, 4000);

    }

    private void createPass() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("createPassword.fxml")));
        Parent root = fxmlLoader.load();
        CreatePassword controller = fxmlLoader.getController();
        controller.getCancel().setOnAction(e -> {
            stage.close();
        });
        controller.getNoPass().setOnAction(e -> {
            try {
                export(false, "", "");
                snackBarNoti("Export successful", true);
            } catch (FileNotFoundException ex) {
                snackBarNoti("Export failed", false);
                throw new RuntimeException(ex);
            }
            stage.close();
        });
        controller.getCreatePass().setOnAction(e -> {
            try {
                if (controller.getValidationSupportPass().getValidationResult().getErrors().isEmpty() && controller.getValidationSupportOwnPass().getValidationResult().getErrors().isEmpty()) {
                    export(true, controller.getOwnPass().getText(), controller.getPass().getText());
                    snackBarNoti("Export successful", true);
                    stage.close();
                } else snackBarNoti("Password is required", false);
            } catch (FileNotFoundException ex) {
                snackBarNoti("Export failed", false);
                stage.close();
                throw new RuntimeException(ex);
            }

        });

        Scene scene = new Scene(root, 600, 275);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        stage.initOwner(owner);
        stage.show();
    }
}
