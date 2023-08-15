package com.example.clinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Appointments {
    private ObservableList<Appointments> appointmentsObesrvableList;
    private String date;
    private String time;
    private String datetime;
    private String doctor;
    private String owner;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Appointments(String date, String time , String doctor, String owner, String animal, String datetime) {
        this.date = date;
        this.time = time;
        this.doctor = doctor;
        this.owner = owner;
        this.animal = animal;
        this.datetime = datetime;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
