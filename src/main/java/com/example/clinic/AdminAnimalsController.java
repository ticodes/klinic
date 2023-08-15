package com.example.clinic;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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
    private TableColumn<?, ?> breed;

    @FXML
    private Button breeds;

    @FXML
    private Button change;

    @FXML
    private Button delete;

    @FXML
    private Button doctors;

    @FXML
    private ChoiceBox<?> fieldBreed;

    @FXML
    private TextField fieldName;

    @FXML
    private ChoiceBox<?> fieldOwner;

    @FXML
    private TableColumn<?, ?> name;

    @FXML
    private TableColumn<?, ?> owner;

    @FXML
    private Button owners;

    @FXML
    private TableView<?> tableAnimals;

    @FXML
    void initialize() {

        administrators();
        doctors();
        owners();
        appointments();
        breeds();
        account();
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

}
