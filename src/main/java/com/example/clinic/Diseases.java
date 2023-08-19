package com.example.clinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Diseases {
    public Diseases() {
    }

    public Diseases(String id, String common_name, String scientific_name) {
        this.id = id;
        this.common_name = common_name;
        this.scientific_name = scientific_name;
    }
    public ObservableList<Diseases> getObservableList() throws SQLException, ClassNotFoundException {
        diseasesObesrvableList = FXCollections.observableArrayList(DataBaseControl.getInstance().getTableDiseases());
        return diseasesObesrvableList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    private String id;
    private String common_name;
    private String scientific_name;
    private ObservableList<Diseases> diseasesObesrvableList;
}
