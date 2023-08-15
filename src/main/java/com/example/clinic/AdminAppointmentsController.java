package com.example.clinic;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminAppointmentsController {

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
    private TableColumn<Appointments, String> animal;

    @FXML
    private Button animals;

    @FXML
    private Button appointments;

    @FXML
    private Button breeds;

    @FXML
    private Button change;

    @FXML
    private TableColumn<Appointments, String> datetime;

    @FXML
    private Button delete;

    @FXML
    private TableColumn<Appointments, String> doctor;

    @FXML
    private Button doctors;

    @FXML
    private ChoiceBox<?> fieldAnimal;

    @FXML
    private ChoiceBox<?> fieldDoctor;

    @FXML
    private ChoiceBox<?> fieldOwner;

    @FXML
    private DatePicker fielddate;

    @FXML
    private TextField fieldtime;

    @FXML
    private TableColumn<Appointments, String> owner;

    @FXML
    private Button owners;

    @FXML
    private TableView<Appointments> tableappointment;
    DataBaseControl dbHandler = null;
    Appointments appoint = new Appointments();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        fillTable();
        account();
        administrators();
        doctors();
        owners();

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
            appoint.getObservableList().clear();
            dbHandler = DataBaseControl.getInstance();
            appoint.getObservableList().addAll(dbHandler.getTableAppointments());
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void fillTable() throws SQLException, ClassNotFoundException {
        addInfAboutTables();
        configureColumn(datetime, "datetime");
        configureColumn(doctor, "doctor");
        configureColumn(owner, "owner");
        configureColumn(animal, "animal");
        tableappointment.setItems(appoint.getObservableList());
    }

}

