package com.example.clinic;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MenuDoctorController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button account;

    @FXML
    private TextField address;

    @FXML
    private Button animals;

    @FXML
    private Button appointments;

    @FXML
    private Button breeds;

    @FXML
    private Button exit;

    @FXML
    private TextField login;

    @FXML
    private TextField name;

    @FXML
    private PasswordField password;

    @FXML
    private Button save;

    @FXML
    private TextField telephone;
    Doctors doctor = new Doctors();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        fillData();
        appointments();
        animals();
        breeds();
        exit();

    }
    public void fillData() throws SQLException, ClassNotFoundException {
        name.setText(String.valueOf(doctor.getObservableList().get(0).getName()));
        address.setText(String.valueOf(doctor.getObservableList().get(0).getAddress()));
        telephone.setText(String.valueOf(doctor.getObservableList().get(0).getTelephone()));
        login.setText(String.valueOf(doctor.getObservableList().get(0).getLogin()));
    }

    public void appointments(){
        appointments.setOnAction(event -> {
            Window.changeWindow(event, "doctorAppointments.fxml", "Ветеринарная клиника");

        });
    }
    public void animals(){
        animals.setOnAction(event -> {
            Window.changeWindow(event, "doctorAnimals.fxml", "Ветеринарная клиника");

        });
    }
    public void breeds(){
        breeds.setOnAction(event -> {
            Window.changeWindow(event, "doctorBreeds.fxml", "Ветеринарная клиника");

        });
    }
    public void exit(){
        exit.setOnAction(event -> {
            Window.changeWindow(event, "hello-view.fxml", "Ветеринарная клиника");

        });
    }
}

