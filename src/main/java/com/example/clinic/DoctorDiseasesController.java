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

public class DoctorDiseasesController {

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
    private TextField fieldName;

    @FXML
    private TableColumn<Diseases, String> name;

    @FXML
    private TextField scientific_name;
    DataBaseControl dbHandler = null;
    Diseases disease = new Diseases();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        fillTable();
        getDataField();
        appointments();
        breeds();
        account();
        animals();

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
    public void breeds(){
        breeds.setOnAction(event -> {
            Window.changeWindow(event, "doctorBreeds.fxml", "Ветеринарная клиника");

        });
    }

}

