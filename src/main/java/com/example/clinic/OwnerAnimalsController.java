package com.example.clinic;

import java.net.URL;
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

public class OwnerAnimalsController {

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
    private TableColumn<Animals, String> breed;

    @FXML
    private Button delete;

    @FXML
    private ChoiceBox<String> fieldBreed;

    @FXML
    private TextField fieldName;

    @FXML
    private TableColumn<Animals, String> name;

    @FXML
    private TableColumn<Animals, String> owner;

    @FXML
    private TableView<Animals> tableAnimals;
    DataBaseControl dbHandler = null;
    Animals animal = new Animals();

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        add.setOnAction(event -> {
            if(!fieldName.getText().isEmpty() && !fieldBreed.getValue().isEmpty()) {
                try {
                    animal.addAnimal(new Animals(fieldName.getText(), fieldBreed.getValue(), dbHandler.searchOwner()));
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
            if(!fieldName.getText().isEmpty() && !fieldBreed.getValue().isEmpty()) {
                try {
                    animal.deleteAnimal(new Animals(fieldName.getText(), fieldBreed.getValue(), dbHandler.searchOwner()));
                    initialize();
                    fieldName.clear();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        appointments();
        account();
        fillChoiseBreed();
        getDataField();
        fillTable();

    }
    public void appointments(){
        appointments.setOnAction(event -> {
            Window.changeWindow(event, "ownerAppointments.fxml", "Ветеринарная клиника");

        });
    }
    public void account(){
        account.setOnAction(event -> {
            Window.changeWindow(event, "menuOwner.fxml", "Ветеринарная клиника");

        });
    }
    @FXML
    void getDataField (){
        tableAnimals.setOnMouseClicked(mouseEvent -> {
            if(!tableAnimals.getSelectionModel().isEmpty()) {
                Animals animal = tableAnimals.getSelectionModel().getSelectedItem();
                fieldName.setText(animal.getName());
                fieldBreed.setValue(animal.getBreed());
            }
        });
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

        // Очищаем данные в таблице перед заполнением
        tableAnimals.getItems().clear();

        // Получаем список всех животных из базы данных
        List<Animals> allAnimals = animal.getObservableList();

        // Задаем значение фильтрации по имени владельца
        String ownerNameFilter = dbHandler.searchOwner();

        // Проходимся по каждому животному и добавляем в таблицу только если имя владельца совпадает с заданным значением
        for (Animals animal : allAnimals) {
            if (animal.getOwner().equals(ownerNameFilter)) {
                tableAnimals.getItems().add(animal);
            }
        }
    }
    public void fillChoiseBreed() throws SQLException, ClassNotFoundException {
        List<Breeds> animals = DataBaseControl.getInstance().getTableBreeds();
        ObservableList<String> columnValues = FXCollections.observableArrayList();

        for (Breeds breed : animals) {
            String columnValue = breed.getName();
            columnValues.add(columnValue);
            fieldBreed.setItems(columnValues);
        }
    }

}

