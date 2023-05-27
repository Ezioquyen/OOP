package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.BreadCrumbBar;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloController {

    @FXML
    private VBox pane;
    private TreeItem<String> home, myCourse, thiCuoiKy, questionBank, question, addMTPCQ;
    @FXML
    private BreadCrumbBar<String> bread;

    @FXML
    private void initialize() throws IOException {
        home = new TreeItem<>("Home");
        myCourse = new TreeItem<>("My Course");
        thiCuoiKy = new TreeItem<>("THI CUỐI KỲ");
        questionBank = new TreeItem<>("Question Bank");
        question = new TreeItem<>("Questions ");
        addMTPCQ = new TreeItem<>("Editing a Multiple choice question");
        home.getChildren().addAll(myCourse);
        myCourse.getChildren().addAll(thiCuoiKy);
        thiCuoiKy.getChildren().add(questionBank);
        questionBank.getChildren().add(question);
        question.getChildren().add(addMTPCQ);
        bread.setSelectedCrumb(thiCuoiKy);
        Pane view = FXMLLoader.load(getClass().getResource("thi-cuoi-ky.fxml"));
        pane.getChildren().setAll(view);
    }

    @FXML
    private void btnQuestions(ActionEvent event) throws IOException {
        VBox view = FXMLLoader.load(getClass().getResource("questionbank.fxml"));
        view.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        pane.getChildren().setAll(view);
    }

    @FXML
    private void btnThiCuoiKy() throws IOException {
        if (thiCuoiKy == bread.getSelectedCrumb()) {
            VBox view = FXMLLoader.load(getClass().getResource("thi-cuoi-ky.fxml"));
            pane.getChildren().setAll(view);
        }
    }
    /*@FXML
    private void btnConnectDB(ActionEvent event) {
        ConnectJDBC connectJDBC = new ConnectJDBC();
        Connection conn = connectJDBC.getConnection();

        String query = "SELECT * FROM CAUTHU";

//        Statement stm = null;
        try {
            //Tạo đối tượng Statement
//            stm = conn.createStatement();

            //Thực thi truy vấn và trả về đối tượng ResultSet
            ResultSet rs = conn.createStatement().executeQuery(query);

            //Duyệt kết quả trả về
            while (rs.next()){  //Di chuyển con trỏ xuống bản ghi kế tiếp
                String id = rs.getString("MACLB");
                String username = rs.getString("HOTEN");
                String password = rs.getString("NGAYSINH");
                String email = rs.getString("VITRI");

                System.out.println(id + " - " + username + " - " + password + " - " + email);
            }
            //Đóng kết nối
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}