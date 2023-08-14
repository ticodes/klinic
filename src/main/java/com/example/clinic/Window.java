package com.example.clinic;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Window {
    public static  void changeWindow(ActionEvent event, String fxml, String title){
        Parent root = null;

        try{
            root = FXMLLoader.load(Window.class.getResource(fxml));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 930, 715));
        stage.show();
    }
}
