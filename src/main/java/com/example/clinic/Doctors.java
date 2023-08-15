package com.example.clinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Doctors extends User{
    private String name;
    private String address;
    private String telephone;
    private ObservableList<Doctors> doctorsObesrvableList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Doctors(String name, String address, String telephone, String login, String password, String role) {
        super(login, password, role);
        this.name = name;
        this.address = address;
        this.telephone = telephone;
    }
    public Doctors(){

    }

    public ObservableList<Doctors> getObservableList() throws SQLException, ClassNotFoundException {
        doctorsObesrvableList = FXCollections.observableArrayList(DataBaseControl.getInstance().getTableDoctors());
        return doctorsObesrvableList;
    }
}
