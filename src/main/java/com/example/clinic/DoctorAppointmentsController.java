package com.example.clinic;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class DoctorAppointmentsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button account;

    @FXML
    private Button add;

    @FXML
    private TableColumn<Appointments, String> animal;

    @FXML
    private Button animals;

    @FXML
    private Button appointments;

    @FXML
    private Button breeds;

    @FXML
    private TableColumn<Appointments, String> datetime;
    @FXML
    private TableColumn<Appointments, String> diseaseColumn;
    @FXML
    private ChoiceBox<String> diseaseField;
    @FXML
    private Button delete;
    @FXML
    private Button diseases;
    @FXML
    private TableColumn<Appointments, String> doctor;

    @FXML
    private ChoiceBox<String> fieldAnimal;

    @FXML
    private ChoiceBox<String> fieldDoctor;

    @FXML
    private DatePicker fielddate;

    @FXML
    private TextField fieldtime;

    @FXML
    private TableColumn<Appointments, String> owner;

    @FXML
    private TableView<Appointments> tableappointment;
    DataBaseControl dbHandler = null;
    Appointments appoint = new Appointments();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        getDataField();
        fillTable();

        add.setOnAction(event -> {
            if(!fielddate.getValue().equals("") && !fieldtime.getText().isEmpty() && !fieldAnimal.getValue().isEmpty() && !fieldDoctor.getValue().isEmpty() && !diseaseField.getValue().isEmpty()) {
                try {
                    appoint.addAppointment(new Appointments( String.valueOf(fielddate.getValue()), fieldtime.getText(), fieldDoctor.getValue(), fieldAnimal.getValue(), fieldAnimal.getValue(), diseaseField.getValue()));
                    initialize();
                    fieldtime.clear();
                    fielddate.setValue(null);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        delete.setOnAction(event -> {
            if(!fieldtime.getText().isEmpty() && !fielddate.getValue().equals(null) && !fieldDoctor.getValue().isEmpty()) {
                try {
                    appoint.deleteAppointment(new Appointments( String.valueOf(fielddate.getValue()), fieldtime.getText(), fieldDoctor.getValue()));
                    initialize();
                    fielddate.setValue(null);
                    fieldtime.clear();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        fillChoiseDoctor();
        fillChoiseAnimal();
        fillChoiseDisease();
        account();
        animals();
        breeds();
        diseases();
    }

    public void account(){
        account.setOnAction(event -> {
            Window.changeWindow(event, "menuDoctor.fxml", "Ветеринарная клиника");

        });
    }
    public void animals(){
        animals.setOnAction(event -> {
            Window.changeWindow(event, "doctorAnimals.fxml", "Ветеринарная клиника");

        });
    }
    public void diseases(){
        diseases.setOnAction(event -> {
            Window.changeWindow(event, "doctorDiseases.fxml", "Ветеринарная клиника");

        });
    }
    public void breeds(){
        breeds.setOnAction(event -> {
            Window.changeWindow(event, "doctorBreeds.fxml", "Ветеринарная клиника");

        });
    }
    @FXML
    void getDataField (){
        tableappointment.setOnMouseClicked(mouseEvent -> {
            if(!tableappointment.getSelectionModel().isEmpty()) {
                Appointments appointment = tableappointment.getSelectionModel().getSelectedItem();
                fielddate.setValue(LocalDate.parse(appointment.getDate()));
                fieldtime.setText(appointment.getTime());
                fieldDoctor.setValue(appointment.getDoctor());
                fieldAnimal.setValue(appointment.getAnimal() + ". " + appointment.getOwner());
                diseaseField.setValue(appointment.getDisease());
            }
        });
    }

    public void fillChoiseDoctor(){
        List<Doctors> doctors = dbHandler.getTableDoctors();
        ObservableList<String> columnValues = FXCollections.observableArrayList();

        for (Doctors doctor : doctors) {
            String columnValue = doctor.getName() + ", " + doctor.getTelephone();
            columnValues.add(columnValue);
            fieldDoctor.setItems(columnValues);
        }
    }
    public void fillChoiseAnimal(){
        List<Animals> animals = dbHandler.getTableAnimals();
        ObservableList<String> columnValues = FXCollections.observableArrayList();

        for (Animals animal : animals) {
            String columnValue = animal.getName() + " - " + animal.getBreed() + ". " + animal.getOwner();
            columnValues.add(columnValue);
            fieldAnimal.setItems(columnValues);
        }
    }
    public void fillChoiseDisease(){
        List<Diseases> diseases = dbHandler.getTableDiseases();
        ObservableList<String> columnValues = FXCollections.observableArrayList();

        for (Diseases disease : diseases) {
            String columnValue = disease.getScientific_name();
            columnValues.add(columnValue);
            diseaseField.setItems(columnValues);
        }
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
        configureColumn(diseaseColumn, "disease");
        tableappointment.setItems(appoint.getObservableList());
    }

}
