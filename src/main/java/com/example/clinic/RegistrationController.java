package com.example.clinic;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Button Exit;

    @FXML
    private Button Registration;

    @FXML
    private TextField addressField;

    @FXML
    private TextField loginField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField telephoneField;
    DataBaseControl dbHandler = null;

    @FXML
    void initialize() {
        exit();
        Registration.setOnAction(event -> {
            try {
                newOwners();
                nameField.clear();
                addressField.clear();
                telephoneField.clear();
                loginField.clear();
                passwordField.clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });

    }
    public void exit(){
        Exit.setOnAction(event -> {
            Window.changeWindow(event, "hello-view.fxml", "Ветеринарная клиника");

        });
    }
    public void newOwners() throws SQLException, ClassNotFoundException {
        String name = nameField.getText();
        String address = addressField.getText();
        String telephone = telephoneField.getText();
        String login = loginField.getText();
        String password = passwordField.getText();

        dbHandler = DataBaseControl.getInstance();
        Owners owner = new Owners(name, address, telephone, login, password, "Клиент");
        dbHandler.newOwner(owner);

    }

}

