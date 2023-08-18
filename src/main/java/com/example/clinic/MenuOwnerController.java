package com.example.clinic;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MenuOwnerController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button account;

    @FXML
    private Button animals;

    @FXML
    private Button appointments;

    @FXML
    private Button exit;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField login;

    @FXML
    private PasswordField password;

    @FXML
    private Button save;

    @FXML
    private TextField secondName;
    Owners owner = new Owners();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        exit();
        appointments();
        animals();
        fillData();

    }
    public void fillData() throws SQLException, ClassNotFoundException {
        String searchResult = DataBaseControl.getInstance().searchOwner(); // Получаем результат из dbHandler.searchOwner()

        for (Owners owner : owner.getObservableList()) { // Цикл по всем элементам списка
            if (owner.getName().equals(searchResult.split(", ")[0]) && owner.getTelephone().equals(searchResult.split(", ")[1])) {
                lastName.setText(String.valueOf(owner.getName()));
                firstName.setText(String.valueOf(owner.getAddress()));
                secondName.setText(String.valueOf(owner.getTelephone()));
                login.setText(String.valueOf(owner.getLogin()));
                return; // Завершаем метод после заполнения данных
            }
        }
    }
    public void exit(){
        exit.setOnAction(event -> {
            Window.changeWindow(event, "hello-view.fxml", "Ветеринарная клиника");

        });
    }
    public void appointments(){
        appointments.setOnAction(event -> {
            Window.changeWindow(event, "ownerAppointments.fxml", "Ветеринарная клиника");

        });
    }
    public void animals(){
        animals.setOnAction(event -> {
            Window.changeWindow(event, "ownerAnimals.fxml", "Ветеринарная клиника");

        });
    }

}

