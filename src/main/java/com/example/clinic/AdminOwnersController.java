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

public class AdminOwnersController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button account;

    @FXML
    private Button add;

    @FXML
    private TableColumn<Owners, String> address;

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
    private Button diseases;
    @FXML
    private Button doctors;

    @FXML
    private TextField fieldAddress;

    @FXML
    private TextField fieldLogin;

    @FXML
    private TextField fieldName;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private TextField fieldTelephone;

    @FXML
    private TableColumn<Owners, String> login;

    @FXML
    private TableColumn<Owners, String> name;

    @FXML
    private Button owners;

    @FXML
    private TableView<Owners> tableadmins;

    @FXML
    private TableColumn<Owners, String> telephone;
    DataBaseControl dbHandler = null;
    Owners owner = new Owners();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        fillTable();
        getDataField();

        add.setOnAction(event -> {
            if(!fieldName.getText().isEmpty() && !fieldAddress.getText().isEmpty() && !fieldTelephone.getText().isEmpty() && !fieldLogin.getText().isEmpty() && !fieldPassword.getText().isEmpty()) {
                try {
                    owner.addOwner(new Owners(fieldName.getText(), fieldAddress.getText(), fieldTelephone.getText(), fieldLogin.getText(), fieldPassword.getText()));
                    initialize();
                    fieldName.clear();
                    fieldAddress.clear();
                    fieldTelephone.clear();
                    fieldLogin.clear();
                    fieldPassword.clear();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        delete.setOnAction(event -> {
            if(!fieldName.getText().isEmpty() && !fieldTelephone.getText().isEmpty()) {
                try {
                    owner.deleteOwner(new Owners(fieldName.getText(), fieldAddress.getText(), fieldTelephone.getText(), fieldLogin.getText(), fieldPassword.getText()));
                    initialize();
                    fieldName.clear();
                    fieldAddress.clear();
                    fieldTelephone.clear();
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
        administrators();
        doctors();
        appointments();
        animals();
        breeds();
        diseases();

    }
    public void account(){
        account.setOnAction(event -> {
            Window.changeWindow(event, "menuAdmin.fxml", "Ветеринарная клиника");

        });
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
    public void appointments(){
        appointments.setOnAction(event -> {
            Window.changeWindow(event, "adminAppointments.fxml", "Ветеринарная клиника");

        });
    }
    public void diseases(){
        diseases.setOnAction(event -> {
            Window.changeWindow(event, "adminDiseases.fxml", "Ветеринарная клиника");

        });
    }
    private <T> void configureColumn(TableColumn<T, ?> column, String property) {
        column.setCellValueFactory(new PropertyValueFactory<>(property));
    }

    private void addInfAboutTables() {
        try {
            owner.getObservableList().clear();
            dbHandler = DataBaseControl.getInstance();
            owner.getObservableList().addAll(dbHandler.getTableOwners());
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void fillTable() throws SQLException, ClassNotFoundException {
        addInfAboutTables();
        configureColumn(name, "name");
        configureColumn(address, "address");
        configureColumn(telephone, "telephone");
        configureColumn(login, "login");
        tableadmins.setItems(owner.getObservableList());
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
                Owners owner = tableadmins.getSelectionModel().getSelectedItem();
                fieldName.setText(owner.getName());
                fieldAddress.setText(owner.getAddress());
                fieldTelephone.setText(owner.getTelephone());
                fieldLogin.setText(owner.getLogin());
            }
        });
    }

}

