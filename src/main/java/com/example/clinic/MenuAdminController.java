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

        save.setOnAction(event -> {
            if(!firstName.getText().isEmpty() && !lastName.getText().isEmpty() && !secondName.getText().isEmpty() && !login.getText().isEmpty() && !password.getText().isEmpty()) {
                try {
                    admin.update(new Administrators(firstName.getText(), lastName.getText(), secondName.getText(), login.getText(), password.getText()));
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

        administrators();
        doctors();
        owners();
        appointments();
        animals();
        breeds();

    }
    public void exit(){
        exit.setOnAction(event -> {
            Window.changeWindow(event, "hello-view.fxml", "Ветеринарная клиника");

        });
    }
    public void fillData() throws SQLException, ClassNotFoundException {
        String searchResult = DataBaseControl.getInstance().searchAdmin(); // Получаем результат из dbHandler.searchOwner()

        for (Administrators admin : admin.getObservableList()) { // Цикл по всем элементам списка
            if (admin.getLastName().equals(searchResult.split(" ")[0]) && admin.getFirstName().equals(searchResult.split(" ")[1]) && admin.getSecondName().equals(searchResult.split(" ")[2])){
                lastName.setText(String.valueOf(admin.getLastName()));
                firstName.setText(String.valueOf(admin.getFirstName()));
                secondName.setText(String.valueOf(admin.getSecondName()));
                login.setText(String.valueOf(admin.getLogin()));
                return; // Завершаем метод после заполнения данных
            }
        }
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
    public void animals(){
        animals.setOnAction(event -> {
            Window.changeWindow(event, "adminAnimals.fxml", "Ветеринарная клиника");

        });
    }
    public void breeds(){
        breeds.setOnAction(event -> {
            Window.changeWindow(event, "adminBreeds.fxml", "Ветеринарная клиника");

        });
    }

}

