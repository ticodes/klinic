package com.example.clinic;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class OwnerAppointmentsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button account;

    @FXML
    private TableColumn<Appointments, String> animal;

    @FXML
    private Button animals;

    @FXML
    private Button appointments;

    @FXML
    private TableColumn<Appointments, String> datetime;

    @FXML
    private TableColumn<Appointments, String> doctor;

    @FXML
    private TableColumn<Appointments, String> owner;

    @FXML
    private TableView<Appointments> tableappointment;
    DataBaseControl dbHandler = null;
    Appointments appoint = new Appointments();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        animals();
        account();
        fillTable();

    }
    public void animals(){
        animals.setOnAction(event -> {
            Window.changeWindow(event, "ownerAnimals.fxml", "Ветеринарная клиника");

        });
    }
    public void account(){
        account.setOnAction(event -> {
            Window.changeWindow(event, "menuOwner.fxml", "Ветеринарная клиника");

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
        configureColumn(owner, "disease");
        configureColumn(animal, "animal");

        // Очищаем данные в таблице перед заполнением
        tableappointment.getItems().clear();

        // Получаем список всех животных из базы данных
        List<Appointments> allAppointments = appoint.getObservableList();

        // Задаем значение фильтрации по имени владельца
        String ownerNameFilter = dbHandler.searchOwner();

        // Проходимся по каждому животному и добавляем в таблицу только если имя владельца совпадает с заданным значением
        for (Appointments appointments1: allAppointments) {
            if (appointments1.getOwner().equals(ownerNameFilter)) {
                tableappointment.getItems().add(appointments1);
            }
        }
    }


}
