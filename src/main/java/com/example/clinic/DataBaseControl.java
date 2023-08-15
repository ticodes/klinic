package com.example.clinic;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseControl {
    private static DataBaseControl instance; // Статическое поле для хранения единственного экземпляра класса
    private Connection dbConnection;

    private DataBaseControl() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://std-mysql.ist.mospolytech.ru:3306/std_2285_clinic";
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, "std_2285_clinic", "adgjl150+A");
    }

    public static DataBaseControl getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            instance = new DataBaseControl(); // Создание экземпляра класса, если он еще не был создан
        }
        else if (instance.getDbConnection().isClosed()){
            instance = new DataBaseControl();
        }
        return instance;
    }

    public Connection getDbConnection() throws SQLException {
        return dbConnection; // Возвращение уже установленного соединения
    }

    public ResultSet getUser(User user) {
        ResultSet resSet = null;
        String select = "SELECT * FROM users WHERE login = ?";
        try {
            PreparedStatement prSt = getInstance().getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getLogin());

            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return resSet;
    }

    public List<Administrators> getAdmins() {
        List<Administrators> admins = new ArrayList<>();
        ResultSet resultSet = null;
        String select = "SELECT * FROM users JOIN administrators ON users.id = administrators.id_user WHERE login = ?";

        try {
            PreparedStatement prSt = getInstance().getDbConnection().prepareStatement(select);
            prSt.setString(1, MainController.getUserLogin());

            resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                String lastName = resultSet.getString("last_name");
                String firstName = resultSet.getString("first_name");
                String secondName = resultSet.getString("second_name");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                Administrators admin = new Administrators(lastName, firstName, secondName, login, password, role);
                admins.add(admin);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return admins;
    }
    public List<Owners> getOwners() {
        List<Owners> owners = new ArrayList<>();
        ResultSet resultSet = null;
        String select = "SELECT * FROM users JOIN owners ON users.id = owners.id_user";

        try {
            PreparedStatement prSt = getInstance().getDbConnection().prepareStatement(select);
            resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String telephone = resultSet.getString("telephone");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");

                Owners owner = new Owners(name, address, telephone, login, password, "Клиент");
                owners.add(owner);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return owners;
    }
    public void newOwner(Owners owner) {
        String usernameCheck = "SELECT * FROM users WHERE login=?";
        String insertClient = "INSERT INTO owners (name, address , telephone, id_user) VALUES(?,?,?,?);";
        String insertUser = "INSERT INTO users (login, password, role) VALUES(?, ?, ?);";

        try {
            PreparedStatement usernameCheckSt = getDbConnection().prepareStatement(usernameCheck);
            usernameCheckSt.setString(1, owner.getLogin());

            ResultSet usernameCheckRes = usernameCheckSt.executeQuery();
            if (usernameCheckRes.next()) {
                throw new IllegalArgumentException("Логин уже существует"); // бросаем исключение если логин уже существует
            }

            String hashedPassword = PasswordHasher.hashPassword(owner.getPassword());

            PreparedStatement prStUser = getDbConnection().prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
            prStUser.setString(1, owner.getLogin());
            prStUser.setString(2, hashedPassword);
            prStUser.setString(3, "Клиент");

            int resultUser = prStUser.executeUpdate();

            if (resultUser == 0) {
                throw new SQLException("Вставка пользователя не удалась"); // бросаем исключение если вставка пользователя не удалась
            }

            ResultSet generatedKeys = prStUser.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);

                PreparedStatement prSt = getDbConnection().prepareStatement(insertClient);
                prSt.setString(1, owner.getName());
                prSt.setString(2, owner.getAddress());
                prSt.setString(3, owner.getTelephone());
                prSt.setInt(4, userId);

                int resultClient = prSt.executeUpdate();

                if (resultClient == 0) {
                    throw new SQLException("Вставка владельца не удалась"); // бросаем исключение если вставка владельца не удалась
                }
            } else {
                throw new SQLException("Идентификатор пользователя не был сгенерирован"); // бросаем исключение если идентификатор пользователя не был сгенерирован
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Administrators> getTableAdmins() {
        List<Administrators> admins = new ArrayList<>();
        ResultSet resultSet = null;
        String select = "SELECT * FROM users JOIN administrators ON users.id = administrators.id_user";

        try {
            PreparedStatement prSt = getInstance().getDbConnection().prepareStatement(select);

            resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                String lastName = resultSet.getString("last_name");
                String firstName = resultSet.getString("first_name");
                String secondName = resultSet.getString("second_name");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                Administrators admin = new Administrators(lastName, firstName, secondName, login, password, role);
                admins.add(admin);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return admins;
    }
    public List<Owners> getTableOwners() {
        List<Owners> owners = new ArrayList<>();
        ResultSet resultSet = null;
        String select = "SELECT * FROM users JOIN owners ON users.id = owners.id_user";

        try {
            PreparedStatement prSt = getInstance().getDbConnection().prepareStatement(select);

            resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String telephone = resultSet.getString("telephone");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                Owners owner = new Owners(name, address, telephone, login, password, role);
                owners.add(owner);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return owners;
    }
}
