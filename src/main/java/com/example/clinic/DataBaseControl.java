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
    public void newOwner(Owners owner) {
        String usernameCheck = "SELECT * FROM users WHERE login=?";
        String insertClient = "INSERT INTO owners (name, address , telephone, id_user) VALUES(?,?,?,?);";
        String insertUser = "INSERT INTO users (login, password, role) VALUES(?, ?, ?);";

        try {
            if (owner.getLogin().isEmpty() || owner.getPassword().isEmpty() || owner.getName().isEmpty() ||
                    owner.getAddress().isEmpty() || owner.getTelephone().isEmpty()) {
                System.out.println("Не все поля заполнены.");
                return;
            }

            PreparedStatement usernameCheckSt = getDbConnection().prepareStatement(usernameCheck);
            usernameCheckSt.setString(1, owner.getLogin());
            ResultSet usernameCheckRes = usernameCheckSt.executeQuery();
            if (usernameCheckRes.next()) {
                System.out.println("Логин уже существует.");
                return;
            }

            String hashedPassword = PasswordHasher.hashPassword(owner.getPassword());

            PreparedStatement prStUser = getDbConnection().prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
            prStUser.setString(1, owner.getLogin());
            prStUser.setString(2, hashedPassword);
            prStUser.setString(3, "Клиент");
            int resultUser = prStUser.executeUpdate();
            if (resultUser == 0) {
                System.out.println("Вставка пользователя не удалась.");
                return;
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
                    System.out.println("Вставка владельца не удалась.");
                } else {
                    System.out.println("Новый владелец успешно добавлен.");
                }
            } else {
                System.out.println("Идентификатор пользователя не был сгенерирован.");
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
    public List<Doctors> getTableDoctors() {
        List<Doctors> doctors = new ArrayList<>();
        ResultSet resultSet = null;
        String select = "SELECT * FROM users JOIN doctors ON users.id = doctors.id_user";

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
                Doctors doctor = new Doctors(name, address, telephone, login, password, role);
                doctors.add(doctor);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return doctors;
    }
    public List<Appointments> getTableAppointments() {
        List<Appointments> appointments = new ArrayList<>();
        ResultSet resultSet = null;
        String select = "SELECT * FROM appointments JOIN doctors ON doctors.id = appointments.id_doctor JOIN owners ON owners.id = " +
                "appointments.id_owner JOIN animals ON animals.id = appointments.id_animal JOIN breeds ON breeds.id = animals.id_breed";

        try {
            PreparedStatement prSt = getInstance().getDbConnection().prepareStatement(select);

            resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                String datetime = resultSet.getString("date")  + " " + resultSet.getString("time");
                String doctor = resultSet.getString("doctors.name") + ", " + resultSet.getString("telephone");
                String owner = resultSet.getString("owners.name") + ", " + resultSet.getString("telephone");
                String animal = resultSet.getString("animals.name") + " - " + resultSet.getString("breeds.name");
                Appointments appointment = new Appointments(date, time, doctor, owner, animal, datetime);
                appointments.add(appointment);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return appointments;
    }
    public List<Breeds> getTableBreeds() {
        List<Breeds> breeds = new ArrayList<>();
        ResultSet resultSet = null;
        String select = "SELECT * FROM breeds";

        try {
            PreparedStatement prSt = getInstance().getDbConnection().prepareStatement(select);

            resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                Breeds breed = new Breeds(id, name);
                breeds.add(breed);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return breeds;
    }
    public List<Animals> getTableAnimals() {
        List<Animals> animals = new ArrayList<>();
        ResultSet resultSet = null;
        String select = "SELECT * FROM animals JOIN breeds ON animals.id_breed = breeds.id JOIN owners ON owners.id = animals.id_owner";

        try {
            PreparedStatement prSt = getInstance().getDbConnection().prepareStatement(select);

            resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("animals.name");
                String breed = resultSet.getString("breeds.name");
                String owner = resultSet.getString("owners.name") + ", " + resultSet.getString("owners.telephone");
                Animals animal = new Animals(name, breed, owner);
                animals.add(animal);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return animals;
    }
    public void addAnimal(Animals animal) {
        try {
            // Получаем id породы по ее названию
            String selectBreedId = "SELECT id FROM breeds WHERE name = ?";
            PreparedStatement breedIdStatement = getInstance().getDbConnection().prepareStatement(selectBreedId);
            breedIdStatement.setString(1, animal.getBreed());
            ResultSet breedIdResultSet = breedIdStatement.executeQuery();
            int breedId = 0;
            if (breedIdResultSet.next()) {
                breedId = breedIdResultSet.getInt("id");
            } else {
                throw new IllegalArgumentException("Порода с названием " + animal.getBreed() + " не существует.");
            }

            // Получаем id хозяина по его имени и телефону
            String selectOwnerId = "SELECT id FROM owners WHERE name = ? AND telephone = ?";
            PreparedStatement ownerIdStatement = getInstance().getDbConnection().prepareStatement(selectOwnerId);
            ownerIdStatement.setString(1, animal.getOwner().split(", ")[0]); // Имя хозяина
            ownerIdStatement.setString(2, animal.getOwner().split(", ")[1]); // Телефон хозяина
            ResultSet ownerIdResultSet = ownerIdStatement.executeQuery();
            int ownerId = 0;
            if (ownerIdResultSet.next()) {
                ownerId = ownerIdResultSet.getInt("id");
            } else {
                throw new IllegalArgumentException("Хозяин с именем и телефоном " + animal.getOwner() + " не найден.");
            }

            // Проверяем, нет ли уже точно такой же записи в таблице animals
            String checkAnimalExists = "SELECT id FROM animals WHERE id_breed = ? AND id_owner = ? AND name = ?";
            PreparedStatement checkAnimalExistsStatement = getInstance().getDbConnection().prepareStatement(checkAnimalExists);
            checkAnimalExistsStatement.setInt(1, breedId);
            checkAnimalExistsStatement.setInt(2, ownerId);
            checkAnimalExistsStatement.setString(3, animal.getName());
            ResultSet animalExistsResultSet = checkAnimalExistsStatement.executeQuery();
            if (animalExistsResultSet.next()) {
                System.out.println("Животное уже существует.");
                return;
            }

            // Добавляем животное в таблицу animals
            String insertAnimal = "INSERT INTO animals (id_breed, id_owner, name) VALUES (?, ?, ?)";
            PreparedStatement insertAnimalStatement = getInstance().getDbConnection().prepareStatement(insertAnimal, Statement.RETURN_GENERATED_KEYS);
            insertAnimalStatement.setInt(1, breedId);
            insertAnimalStatement.setInt(2, ownerId);
            insertAnimalStatement.setString(3, animal.getName());
            int rowsAffected = insertAnimalStatement.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet generatedKeys = insertAnimalStatement.getGeneratedKeys();
                int animalId;
                if (generatedKeys.next()) {
                    animalId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Не удалось получить сгенерированный id для нового животного.");
                }

                System.out.println("Животное успешно добавлено с id: " + animalId);
            } else {
                throw new SQLException("Не удалось добавить животное.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void newDoctor(Doctors doctor) {
        String usernameCheck = "SELECT * FROM users WHERE login=?";
        String insertClient = "INSERT INTO doctors (name, address , telephone, id_user) VALUES(?,?,?,?);";
        String insertUser = "INSERT INTO users (login, password, role) VALUES(?, ?, ?);";

        try {
            if (doctor.getLogin().isEmpty() || doctor.getPassword().isEmpty() || doctor.getName().isEmpty() ||
                    doctor.getAddress().isEmpty() || doctor.getTelephone().isEmpty()) {
                System.out.println("Не все поля заполнены.");
                return;
            }

            PreparedStatement usernameCheckSt = getDbConnection().prepareStatement(usernameCheck);
            usernameCheckSt.setString(1, doctor.getLogin());
            ResultSet usernameCheckRes = usernameCheckSt.executeQuery();
            if (usernameCheckRes.next()) {
                System.out.println("Логин уже существует.");
                return;
            }

            String hashedPassword = PasswordHasher.hashPassword(doctor.getPassword());

            PreparedStatement prStUser = getDbConnection().prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
            prStUser.setString(1, doctor.getLogin());
            prStUser.setString(2, hashedPassword);
            prStUser.setString(3, "Врач");
            int resultUser = prStUser.executeUpdate();
            if (resultUser == 0) {
                System.out.println("Вставка пользователя не удалась.");
                return;
            }

            ResultSet generatedKeys = prStUser.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);

                PreparedStatement prSt = getDbConnection().prepareStatement(insertClient);
                prSt.setString(1, doctor.getName());
                prSt.setString(2, doctor.getAddress());
                prSt.setString(3, doctor.getTelephone());
                prSt.setInt(4, userId);
                int resultClient = prSt.executeUpdate();

                if (resultClient == 0) {
                    System.out.println("Вставка врача не удалась.");
                } else {
                    System.out.println("Новый врач успешно добавлен.");
                }
            } else {
                System.out.println("Идентификатор пользователя не был сгенерирован.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
