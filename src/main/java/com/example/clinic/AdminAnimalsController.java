package com.example.clinic;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminAnimalsController {

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
    private TableColumn<Animals, String> breed;

    @FXML
    private Button breeds;
    @FXML
    private Button delete;
    @FXML
    private Button diseases;
    @FXML
    private Button doctors;

    @FXML
    private ChoiceBox<String> fieldBreed;

    @FXML
    private TextField fieldName;

    @FXML
    private ChoiceBox<String> fieldOwner;

    @FXML
    private TableColumn<Animals, String> name;

    @FXML
    private TableColumn<Animals, String> owner;

    @FXML
    private Button owners;

    @FXML
    private TableView<Animals> tableAnimals;
    DataBaseControl dbHandler = null;
    Animals animal = new Animals();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        fillTable();
        getDataField();
        fillChoiseBreed();
        fillChoiseOwner();

        add.setOnAction(event -> {
            if(!fieldName.getText().isEmpty() && !fieldBreed.getValue().isEmpty()) {
                try {
                    animal.addAnimal(new Animals(fieldName.getText(), fieldBreed.getValue(), fieldOwner.getValue()));
                    initialize();
                    fieldName.clear();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        delete.setOnAction(event -> {
            if(!fieldName.getText().isEmpty() && !fieldBreed.getValue().isEmpty() && !fieldOwner.getValue().isEmpty()) {
                try {
                    animal.deleteAnimal(new Animals(fieldName.getText(), fieldBreed.getValue(), fieldOwner.getValue()));
                    initialize();
                    fieldName.clear();
                    fieldOwner.setValue(null);
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
        breeds();
        account();
        diseases();
    }
    private <T> void configureColumn(TableColumn<T, ?> column, String property) {
        column.setCellValueFactory(new PropertyValueFactory<>(property));
    }

    private void addInfAboutTables() {
        try {
            animal.getObservableList().clear();
            dbHandler = DataBaseControl.getInstance();
            animal.getObservableList().addAll(dbHandler.getTableAnimals());
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void fillTable() throws SQLException, ClassNotFoundException {
        addInfAboutTables();
        configureColumn(name, "name");
        configureColumn(breed, "breed");
        configureColumn(owner, "owner");
        tableAnimals.setItems(animal.getObservableList());
    }

    public void administrators(){
        administrators.setOnAction(event -> {
            Window.changeWindow(event, "adminAdministrators.fxml", "Ветеринарная клиника");

        });
    }
    public void diseases(){
        diseases.setOnAction(event -> {
            Window.changeWindow(event, "adminDiseases.fxml", "Ветеринарная клиника");

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
    public void breeds(){
        breeds.setOnAction(event -> {
            Window.changeWindow(event, "adminBreeds.fxml", "Ветеринарная клиника");

        });
    }
    public void account(){
        account.setOnAction(event -> {
            Window.changeWindow(event, "menuAdmin.fxml", "Ветеринарная клиника");

        });
    }
    public void fillChoiseBreed(){
        List<Breeds> animals = dbHandler.getTableBreeds();
        ObservableList<String> columnValues = FXCollections.observableArrayList();

        for (Breeds breed : animals) {
            String columnValue = breed.getName();
            columnValues.add(columnValue);
            fieldBreed.setItems(columnValues);
        }
    }
    public void fillChoiseOwner(){
        List<Owners> owners = dbHandler.getTableOwners();
        ObservableList<String> columnValues = FXCollections.observableArrayList();

        for (Owners owner : owners) {
            String columnValue = owner.getName() + ", " + owner.getTelephone();
            columnValues.add(columnValue);
            fieldOwner.setItems(columnValues);
        }
    }
    @FXML
    void getDataField (){
        tableAnimals.setOnMouseClicked(mouseEvent -> {
            if(!tableAnimals.getSelectionModel().isEmpty()) {
                Animals animal = tableAnimals.getSelectionModel().getSelectedItem();
                fieldName.setText(animal.getName());
                fieldBreed.setValue(animal.getBreed());
                fieldOwner.setValue(animal.getOwner());
            }
        });
    }



}
