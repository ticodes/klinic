package com.example.clinic;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminAdministratorsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button account;

    @FXML
    private Button add;

    @FXML
    private Button administrators;

    @FXML
    private Button animals;

    @FXML
    private Button appointments;

    @FXML
    private Button breeds;

    @FXML
    private Button change;

    @FXML
    private Button delete;

    @FXML
    private Button doctors;

    @FXML
    private TextField fieldFirstName;

    @FXML
    private TextField fieldLastName;

    @FXML
    private TextField fieldLogin;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private TextField fieldSecondName;

    @FXML
    private TableColumn<Administrators, String> firstName;

    @FXML
    private TableColumn<Administrators, String> lastName;

    @FXML
    private TableColumn<Administrators, String> login;

    @FXML
    private Button owners;

    @FXML
    private TableColumn<Administrators, String> secondName;

    @FXML
    private TableView<Administrators> tableadmins;
    DataBaseControl dbHandler = null;
    Administrators admins = new Administrators();


    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        fillTable();
        account();
        doctors();
        owners();
    }
    public void account(){
        account.setOnAction(event -> {
            Window.changeWindow(event, "menuAdmin.fxml", "Ветеринарная клиника");

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
    private <T> void configureColumn(TableColumn<T, ?> column, String property) {
        column.setCellValueFactory(new PropertyValueFactory<>(property));
    }

    private void addInfAboutTables() {
        try {
            admins.getAdmins().clear();
            dbHandler = DataBaseControl.getInstance();
            admins.getAdmins().addAll(dbHandler.getTableAdmins());
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void fillTable() throws SQLException, ClassNotFoundException {
        addInfAboutTables();
        configureColumn(firstName, "firstName");
        configureColumn(lastName, "lastName");
        configureColumn(secondName, "secondName");
        configureColumn(login, "login");
        tableadmins.setItems(admins.getAdmins());
    }

}

