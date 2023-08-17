package com.example.clinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.SQLException;


public class Administrators extends User{
    private String lastName;
    private String firstName;
    private String secondName;
    private ObservableList<Administrators> adminObesrvableList;

    public Administrators(String lastName, String firstName, String secondName, String login, String password, String role) {
        super(login, password, role);
        this.lastName = lastName;
        this.firstName = firstName;
        this.secondName = secondName;
    }
    public Administrators(String firstName, String lastName, String secondName, String login, String password) {
        super(login, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.secondName = secondName;
    }
    public ObservableList<Administrators> getObservableList() throws SQLException, ClassNotFoundException {
        adminObesrvableList = FXCollections.observableArrayList(DataBaseControl.getInstance().getAdmins());
        return adminObesrvableList;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Administrators(){

    }
    public ObservableList<Administrators> getAdmins() throws SQLException, ClassNotFoundException {
        adminObesrvableList = FXCollections.observableArrayList(DataBaseControl.getInstance().getTableAdmins());
        return adminObesrvableList;
    }
    public void addAdministrator(Administrators administrator) throws SQLException, ClassNotFoundException {
        DataBaseControl.getInstance().newAdministrator(administrator);
    }
    public void update(Administrators administrator) throws SQLException, ClassNotFoundException {
        DataBaseControl.getInstance().updateAdministrator(administrator);
    }
}
