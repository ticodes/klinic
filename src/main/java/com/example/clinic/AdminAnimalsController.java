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
       

    }

}
