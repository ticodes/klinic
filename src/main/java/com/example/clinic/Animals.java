package com.example.clinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Animals {
    private String name;
    private String breed;
    private String owner;
    private ObservableList<Animals> animalsObesrvableList;

    public Animals(String name, String breed, String owner) {
        this.name = name;
        this.breed = breed;
        this.owner = owner;
    }
    public Animals(){

    }
    public ObservableList<Animals> getObservableList() throws SQLException, ClassNotFoundException {
        animalsObesrvableList = FXCollections.observableArrayList(DataBaseControl.getInstance().getTableAnimals());
        return animalsObesrvableList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
