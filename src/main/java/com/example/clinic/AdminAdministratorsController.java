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
        getDataField();

        add.setOnAction(event -> {
            if(!fieldFirstName.getText().isEmpty() && !fieldLastName.getText().isEmpty() && !fieldSecondName.getText().isEmpty() && !fieldLogin.getText().isEmpty() && !fieldPassword.getText().isEmpty()) {
                try {
                    admins.addAdministrator(new Administrators(fieldFirstName.getText(), fieldLastName.getText(), fieldSecondName.getText(), fieldLogin.getText(), fieldPassword.getText()));
                    initialize();
                    fieldFirstName.clear();
                    fieldLastName.clear();
                    fieldSecondName.clear();
                    fieldLogin.clear();
                    fieldPassword.clear();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        account();
        doctors();
        owners();
        appointments();
        animals();
        breeds();
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
    public void appointments(){
        appointments.setOnAction(event -> {
            Window.changeWindow(event, "adminAppointments.fxml", "Ветеринарная клиника");

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
    @FXML
    void getDataField (){
        tableadmins.setOnMouseClicked(mouseEvent -> {
            if(!tableadmins.getSelectionModel().isEmpty()) {
                Administrators admin = tableadmins.getSelectionModel().getSelectedItem();
                fieldFirstName.setText(admin.getFirstName());
                fieldLastName.setText(admin.getLastName());
                fieldSecondName.setText(admin.getSecondName());
                fieldLogin.setText(admin.getLogin());
            }
        });
    }

}

