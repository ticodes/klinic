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

public class AdminDiseasesController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button account;

    @FXML
    private Button add;

    @FXML
    private TableColumn<Diseases, String> address;

    @FXML
    private Button administrators;

    @FXML
    private Button animals;

    @FXML
    private Button appointments;

    @FXML
    private Button breeds;

    @FXML
    private TableView<Diseases> common_name;

    @FXML
    private Button delete;

    @FXML
    private Button diseases;

    @FXML
    private Button doctors;

    @FXML
    private TextField fieldName;

    @FXML
    private TableColumn<Diseases, String> name;

    @FXML
    private Button owners;

    @FXML
    private TextField scientific_name;
    DataBaseControl dbHandler = null;
    Diseases disease = new Diseases();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        fillTable();
        getDataField();
        administrators();
        add.setOnAction(event -> {
            if(!fieldName.getText().isEmpty()) {
                try {
                    disease.addDisease(new Diseases(fieldName.getText(), scientific_name.getText()));
                    initialize();
                    fieldName.clear();
                    scientific_name.clear();
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
                    disease.deleteDisease(new Diseases(fieldName.getText(), scientific_name.getText()));
                    initialize();
                    fieldName.clear();
                    scientific_name.clear();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        doctors();
        owners();
        appointments();
        animals();
        account();
        breeds();

    }
    private <T> void configureColumn(TableColumn<T, ?> column, String property) {
        column.setCellValueFactory(new PropertyValueFactory<>(property));
    }

    private void addInfAboutTables() {
        try {
            disease.getObservableList().clear();
            dbHandler = DataBaseControl.getInstance();
            disease.getObservableList().addAll(dbHandler.getTableDiseases());
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void fillTable() throws SQLException, ClassNotFoundException {
        addInfAboutTables();
        configureColumn(name, "common_name");
        configureColumn(address, "scientific_name");
        common_name.setItems(disease.getObservableList());
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
    public void breeds(){
        breeds.setOnAction(event -> {
            Window.changeWindow(event, "adminBreeds.fxml", "Ветеринарная клиника");

        });
    }
    @FXML
    void getDataField (){
        common_name.setOnMouseClicked(mouseEvent -> {
            if(!common_name.getSelectionModel().isEmpty()) {
                Diseases diseases = common_name.getSelectionModel().getSelectedItem();
                fieldName.setText(diseases.getCommon_name());
                scientific_name.setText(diseases.getScientific_name());
            }
        });
    }

}

