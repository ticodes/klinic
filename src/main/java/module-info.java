module com.example.clinic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.example.clinic to javafx.fxml;
    exports com.example.clinic;
}