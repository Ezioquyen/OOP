package com.example.demo;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class QuestionBankController {
    @FXML
    private VBox vBox;
    @FXML
    private TextField textField;
    private BreadCrumbBarModel breadCrumbBarModel;

    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
    }

    @FXML
    private void initialize() {
        initModel(BreadCrumbBarModel.getInstance());
    }

    @FXML
    private void txtf(ActionEvent event) {
        ConnectJDBC connectJDBC = new ConnectJDBC();
        Connection conn = connectJDBC.getConnection();
        String txt = textField.getText();
        String query = String.format("SELECT * FROM CAUTHU WHERE HOTEN LIKE '%%%s%%'", txt);
        System.out.println(query);

//        Statement stm = null;
        try {
            //Tạo đối tượng Statement
//            stm = conn.createStatement();

            //Thực thi truy vấn và trả về đối tượng ResultSet
            ResultSet rs = conn.createStatement().executeQuery(query);

            //Duyệt kết quả trả về
            while (rs.next()) {  //Di chuyển con trỏ xuống bản ghi kế tiếp
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
    }

    @FXML
    private void btnAddQuestion(ActionEvent event) throws IOException {
        breadCrumbBarModel.setCurrentView("add-MTPCQ.fxml");

        breadCrumbBarModel.setCurrentTree(breadCrumbBarModel.getBreadConnection().get("add-MTPCQ.fxml"));
        VBox view = FXMLLoader.load(getClass().getResource("add-MTPCQ.fxml"));
        view.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        vBox.getChildren().setAll(view);
    }
}
