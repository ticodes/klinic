package com.example.clinic;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MenuAdminController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button account;

    @FXML
    private Button administrators;

    @FXML
    private Button animals;

    @FXML
    private Button appointments;

    @FXML
    private Button breeds;

    @FXML
    private Button doctors;

    @FXML
    private Button exit;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField login;

    @FXML
    private Button owners;

    @FXML
    private PasswordField password;

    @FXML
    private Button save;

    @FXML
    private TextField secondName;
    DataBaseControl dbHandler = null;
    Administrators admin = new Administrators();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        exit();
        fillData();
        administrators();
        doctors();
        owners();
        appointments();

    }
    public void exit(){
        exit.setOnAction(event -> {
            Window.changeWindow(event, "hello-view.fxml", "Ветеринарная клиника");

        });
    }
    public void fillData() throws SQLException, ClassNotFoundException {
        lastName.setText(String.valueOf(admin.getObservableList().get(0).getLastName()));
        firstName.setText(String.valueOf(admin.getObservableList().get(0).getFirstName()));
        secondName.setText(String.valueOf(admin.getObservableList().get(0).getSecondName()));
        login.setText(String.valueOf(admin.getObservableList().get(0).getLogin()));
    }
    public void administrators(){
        administrators.setOnAction(event -> {
            Window.changeWindow(event, "adminAdministrators.fxml", "Ветеринарная клиника");

        });
    }
    public void doctors(){
        doctors.setOnAction(event -> {
            Window.changeWindow(event, "adminDoctors.fxml", "Ветеринарная клиника");

        });
    }
    public void owners(){
        owners.setOnAction(event -> {
            Window.changeWindow(event, "adminOwners.fxml", "Ветеринарная клиника");

        });
    }
    public void appointments(){
        appointments.setOnAction(event -> {
            Window.changeWindow(event, "adminAppointments.fxml", "Ветеринарная клиника");

        });
    }

}

