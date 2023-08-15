package com.example.clinic;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminBreedsController {

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
    private TextField fieldName;

    @FXML
    private TableColumn<Breeds, String> id;

    @FXML
    private TableColumn<Breeds, String> name;

    @FXML
    private Button owners;

    @FXML
    private TableView<Breeds> tableadmins;
    DataBaseControl dbHandler = null;
    Breeds breed = new Breeds();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        fillTable();
        administrators();
        doctors();
        owners();
        appointments();
        animals();
        account();
    }
    private <T> void configureColumn(TableColumn<T, ?> column, String property) {
        column.setCellValueFactory(new PropertyValueFactory<>(property));
    }

    private void addInfAboutTables() {
        try {
            breed.getObservableList().clear();
            dbHandler = DataBaseControl.getInstance();
            breed.getObservableList().addAll(dbHandler.getTableBreeds());
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void fillTable() throws SQLException, ClassNotFoundException {
        addInfAboutTables();
        configureColumn(id, "id");
        configureColumn(name, "name");
        tableadmins.setItems(breed.getObservableList());
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
    public void account(){
        account.setOnAction(event -> {
            Window.changeWindow(event, "menuAdmin.fxml", "Ветеринарная клиника");

        });
    }

}
