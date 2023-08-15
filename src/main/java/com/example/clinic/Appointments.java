package com.example.clinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Appointments {
    private ObservableList<Appointments> appointmentsObesrvableList;
    private String datetime;
    private String doctor;
    private String owner;

    public Appointments(String datetime, String doctor, String owner, String animal) {
        this.datetime = datetime;
        this.doctor = doctor;
        this.owner = owner;
        this.animal = animal;
    }
    public Appointments(){

    }

    private String animal;
    public ObservableList<Appointments> getObservableList() throws SQLException, ClassNotFoundException {
        appointmentsObesrvableList = FXCollections.observableArrayList(DataBaseControl.getInstance().getTableAppointments());
        return appointmentsObesrvableList;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String date) {
        this.datetime = date;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
