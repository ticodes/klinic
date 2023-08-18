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

public class DoctorBreedsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button account;

    @FXML
    private Button add;

    @FXML
    private Button animals;

    @FXML
    private Button appointments;

    @FXML
    private Button breeds;

    @FXML
    private Button delete;

    @FXML
    private TextField fieldName;

    @FXML
    private TableColumn<Breeds, String> id;

    @FXML
    private TableColumn<Breeds, String> name;

    @FXML
    private TableView<Breeds> tableadmins;
    DataBaseControl dbHandler = null;
    Breeds breed = new Breeds();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        fillTable();

        add.setOnAction(event -> {
            if(!fieldName.getText().isEmpty()) {
                try {
                    breed.addBreed(new Breeds(fieldName.getText()));
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
            if(!fieldName.getText().isEmpty()) {
                try {
                    breed.deleteBreed(new Breeds(fieldName.getText()));
                    initialize();
                    fieldName.clear();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        getDataField();

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

    public void appointments(){
        appointments.setOnAction(event -> {
            Window.changeWindow(event, "doctorAppointments.fxml", "Ветеринарная клиника");

        });
    }
    public void animals(){
        animals.setOnAction(event -> {
            Window.changeWindow(event, "doctorAnimals.fxml", "Ветеринарная клиника");

        });
    }
    public void account(){
        account.setOnAction(event -> {
            Window.changeWindow(event, "menuDoctor.fxml", "Ветеринарная клиника");

        });
    }
    @FXML
    void getDataField (){
        tableadmins.setOnMouseClicked(mouseEvent -> {
            if(!tableadmins.getSelectionModel().isEmpty()) {
                Breeds breed = tableadmins.getSelectionModel().getSelectedItem();
                fieldName.setText(breed.getName());
            }
        });
    }

}
