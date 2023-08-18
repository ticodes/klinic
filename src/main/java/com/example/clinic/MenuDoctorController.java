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
        save.setOnAction(event -> {
            if(!name.getText().isEmpty() && !address.getText().isEmpty() && !telephone.getText().isEmpty() && !login.getText().isEmpty() && !password.getText().isEmpty()) {
                try {
                    doctor.update(new Doctors(name.getText(), address.getText(), telephone.getText(), login.getText(), password.getText()));
                    initialize();
                    password.clear();
                    String newLogin = login.getText();
                    MainController.setUserLogin(newLogin);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void fillData() throws SQLException, ClassNotFoundException {
        String searchResult = DataBaseControl.getInstance().searchDoctor(); // Получаем результат из dbHandler.searchOwner()

        for (Doctors doctor : doctor.getObservableList()) { // Цикл по всем элементам списка
            if (doctor.getName().equals(searchResult.split(", ")[0]) && doctor.getTelephone().equals(searchResult.split(", ")[1])) {
                name.setText(String.valueOf(doctor.getName()));
                address.setText(String.valueOf(doctor.getAddress()));
                telephone.setText(String.valueOf(doctor.getTelephone()));
                login.setText(String.valueOf(doctor.getLogin()));
                return; // Завершаем метод после заполнения данных
            }
        }
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

