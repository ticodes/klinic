package com.example.clinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Owners extends User{
    private String name;
    private String address;
    private String telephone;

    private ObservableList<Owners> ownersObesrvableList;

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

    public Owners(String name, String address, String telephone, String login, String password, String role) {
        super(login, password, role);
        this.name = name;
        this.address = address;
        this.telephone = telephone;
    }
    public Owners(String name, String address, String telephone, String login, String password) {
        super(login, password);
        this.name = name;
        this.address = address;
        this.telephone = telephone;
    }
    public Owners(){

    }

    public ObservableList<Owners> getObservableList() throws SQLException, ClassNotFoundException {
        ownersObesrvableList = FXCollections.observableArrayList(DataBaseControl.getInstance().getTableOwners());
        return ownersObesrvableList;
    }
    public void addOwner(Owners owner) throws SQLException, ClassNotFoundException {
        DataBaseControl.getInstance().newOwner(owner);
    }
    public void deleteOwner(Owners owner) throws SQLException, ClassNotFoundException {
        DataBaseControl.getInstance().deleteOwner(owner);
    }
}
