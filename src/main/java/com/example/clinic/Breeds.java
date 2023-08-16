package com.example.clinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Breeds {
    private String id;
    private String name;
    private ObservableList<Breeds> breedsObesrvableList;

    public Breeds(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public Breeds(String name) {
        this.name = name;
    }
    public Breeds(){

    }
    public ObservableList<Breeds> getObservableList() throws SQLException, ClassNotFoundException {
        breedsObesrvableList = FXCollections.observableArrayList(DataBaseControl.getInstance().getTableBreeds());
        return breedsObesrvableList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void addBreed(Breeds breed) throws SQLException, ClassNotFoundException {
        DataBaseControl.getInstance().newBreed(breed);
    }
}
