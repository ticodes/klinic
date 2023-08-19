package com.example.clinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;

public class Appointments {
    private ObservableList<Appointments> appointmentsObesrvableList;
    private String date;
    private String time;
    private String datetime;
    private String doctor;
    private String owner;
    private String disease;

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getOwnerAnimal() {
        return ownerAnimal;
    }

    public void setOwnerAnimal(String ownerAnimal) {
        this.ownerAnimal = ownerAnimal;
    }

    private String ownerAnimal;

    public Appointments(String date, String time, String doctor, String animal, String owner) {
        this.date = date;
        this.time = time;
        this.doctor = doctor;
        this.animal = animal;
        this.owner = owner;
    }
    public Appointments(String date, String time, String doctor, String animal, String owner, String datetime, String disease) {
        this.date = date;
        this.time = time;
        this.doctor = doctor;
        this.animal = animal;
        this.owner = owner;
        this.disease = disease;
        this.datetime = datetime;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Appointments(String date, String time , String doctor, String owner, String animal, String disease) {
        this.date = date;
        this.time = time;
        this.doctor = doctor;
        this.owner = owner;
        this.animal = animal;
        this.disease = disease;
    }
    public Appointments(){

    }
    public Appointments(String date, String time , String doctor) {
        this.date = date;
        this.time = time;
        this.doctor = doctor;
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
    public void addAppointment(Appointments appointment) throws SQLException, ClassNotFoundException {
        DataBaseControl.getInstance().newAppointment(appointment);
    }
    public void deleteAppointment(Appointments appointment) throws SQLException, ClassNotFoundException {
        DataBaseControl.getInstance().deleteAppointment(appointment);
    }
}
