package com.example.clinic;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private Button Enter;

    @FXML
    private Button Registration;
    private static String userLogin;
    DataBaseControl dbHandler = null;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        authorisation();
        registration();

    }

    public void authorisation() throws SQLException, ClassNotFoundException {
        dbHandler = DataBaseControl.getInstance();
        Enter.setOnAction(event -> {
            String loginText = loginField.getText().trim();
            String loginPassword = passwordField.getText().trim();

            if (!loginText.isEmpty() && !loginPassword.isEmpty()) {
                User user = new User();
                user.setLogin(loginText);
                ResultSet result = dbHandler.getUser(user);
                try {
                    if (result.next()) {
                        String passwordFromDB = result.getString("password"); // Получаем хэш пароля из базы данных
                        String hashedPassword = PasswordHasher.hashPassword(loginPassword); // Хэшируем введенный пользователем пароль
                        if (passwordFromDB.equals(hashedPassword)) { // Сравниваем хэшированные пароли
                            String role = result.getString("role");
                            userLogin = loginText;
                            if (role.equals("Администратор")) {
                                Window.changeWindow(event, "menuAdmin.fxml", "Ветеринарная клиника");
                            } else if (role.equals("Врач")) {
                                Window.changeWindow(event, "menuDoctor.fxml", "Ветеринарная клиника");
                            } else if (role.equals("Клиент")) {
                                Window.changeWindow(event, "menuOwner.fxml", "Ветеринарная клиника");
                            }
                        } else {
                            System.out.println("Неверный пароль"); // Пароль не соответствует
                        }
                    } else {
                        System.out.println("Пользователь не найден"); // Пользователь не найден
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void registration(){
        Registration.setOnAction(event -> {
            Window.changeWindow(event, "registration.fxml", "Регистраиця");

        });
    }
    public static String getUserLogin() {
        return userLogin;
    }

}
