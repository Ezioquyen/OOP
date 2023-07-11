module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires ojdbc10;
    requires com.google.common;
    requires org.apache.poi.poi;
    requires org.apache.logging.log4j;
    requires org.apache.poi.ooxml;
    requires net.synedra.validatorfx;
    requires com.jfoenix;
    requires org.apache.pdfbox;
    requires itextpdf;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}