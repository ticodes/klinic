package com.example.clinic;
import javafx.collections.ObservableList;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

        String select = "SELECT appointments.*, doctors.name AS doctor_name, doctors.telephone AS doctor_telephone, owners.name AS owner_name, owners.telephone AS owner_telephone, animals.name AS animal_name, breeds.name AS breed_name, GROUP_CONCAT(diseases.scientific_name SEPARATOR ', ') AS concatenated_diseases " +
                "FROM appointments " +
                "JOIN doctors ON doctors.id = appointments.id_doctor " +
                "JOIN animals ON animals.id = appointments.id_animal " +
                "JOIN breeds ON breeds.id = animals.id_breed " +
                "LEFT JOIN owners ON owners.id = appointments.id_owner " +
                "LEFT JOIN appointment_disease ON appointment_disease.id_appointment = appointments.id " +
                "LEFT JOIN diseases ON diseases.id = appointment_disease.id_disease " +
                "GROUP BY appointments.id";

        try {
            PreparedStatement prSt = getInstance().getDbConnection().prepareStatement(select);

            resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                String datetime = resultSet.getString("date") + " " + resultSet.getString("time");
                String doctor = resultSet.getString("doctor_name") + ", " + resultSet.getString("doctor_telephone");
                String owner = resultSet.getString("owner_name") + ", " + resultSet.getString("owner_telephone");
                owner = (owner == null) ? "Без хозяина" : owner.replaceAll("null, null", "Без хозяина");
                String animal = resultSet.getString("animal_name") + " - " + resultSet.getString("breed_name");
                String diseases = resultSet.getString("concatenated_diseases");
                Appointments appointment = new Appointments(date, time, doctor, animal, owner, datetime, diseases);
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
        String select = "SELECT animals.name, breeds.name AS breed, IFNULL(CONCAT(owners.name, ', ', owners.telephone), 'Без хозяина') AS owner FROM animals " +
                "JOIN breeds ON animals.id_breed = breeds.id LEFT JOIN owners ON owners.id = animals.id_owner";

        try {
            PreparedStatement prSt = getInstance().getDbConnection().prepareStatement(select);

            resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("animals.name");
                String breed = resultSet.getString("breed");
                String owner = resultSet.getString("owner");
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

            // Получаем id хозяина по его имени и телефону (если owner не равно null)
            int ownerId = 0;
            if (animal.getOwner() != null) {
                String selectOwnerId = "SELECT id FROM owners WHERE name = ? AND telephone = ?";
                PreparedStatement ownerIdStatement = getInstance().getDbConnection().prepareStatement(selectOwnerId);
                ownerIdStatement.setString(1, animal.getOwner().split(", ")[0]); // Имя хозяина
                ownerIdStatement.setString(2, animal.getOwner().split(", ")[1]); // Телефон хозяина
                ResultSet ownerIdResultSet = ownerIdStatement.executeQuery();
                if (ownerIdResultSet.next()) {
                    ownerId = ownerIdResultSet.getInt("id");
                } else {
                    throw new IllegalArgumentException("Хозяин с именем и телефоном " + animal.getOwner() + " не найден.");
                }
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
            if (ownerId != 0) {
                insertAnimalStatement.setInt(2, ownerId);
            } else {
                insertAnimalStatement.setNull(2, java.sql.Types.INTEGER);
            }
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
    public void newAdministrator(Administrators admin) {
        String usernameCheck = "SELECT * FROM users WHERE login=?";
        String insertClient = "INSERT INTO administrators (last_name, first_name, second_name, id_user) VALUES(?,?,?,?);";
        String insertUser = "INSERT INTO users (login, password, role) VALUES(?, ?, ?);";

        try {
            if (admin.getLogin().isEmpty() || admin.getPassword().isEmpty() || admin.getFirstName().isEmpty() ||
                    admin.getSecondName().isEmpty() || admin.getLastName().isEmpty()) {
                System.out.println("Не все поля заполнены.");
                return;
            }

            PreparedStatement usernameCheckSt = getDbConnection().prepareStatement(usernameCheck);
            usernameCheckSt.setString(1, admin.getLogin());
            ResultSet usernameCheckRes = usernameCheckSt.executeQuery();
            if (usernameCheckRes.next()) {
                System.out.println("Логин уже существует.");
                return;
            }

            String hashedPassword = PasswordHasher.hashPassword(admin.getPassword());

            PreparedStatement prStUser = getDbConnection().prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
            prStUser.setString(1, admin.getLogin());
            prStUser.setString(2, hashedPassword);
            prStUser.setString(3, "Администратор");
            int resultUser = prStUser.executeUpdate();
            if (resultUser == 0) {
                System.out.println("Вставка пользователя не удалась.");
                return;
            }

            ResultSet generatedKeys = prStUser.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);

                PreparedStatement prSt = getDbConnection().prepareStatement(insertClient);
                prSt.setString(1, admin.getLastName());
                prSt.setString(2, admin.getFirstName());
                prSt.setString(3, admin.getSecondName());
                prSt.setInt(4, userId);
                int resultClient = prSt.executeUpdate();

                if (resultClient == 0) {
                    System.out.println("Вставка администратора не удалась.");
                } else {
                    System.out.println("Новый администратор успешно добавлен.");
                }
            } else {
                System.out.println("Идентификатор пользователя не был сгенерирован.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void newBreed(Breeds breed) {
        String usernameCheck = "SELECT * FROM breeds WHERE name =?";
        String insertBreed = "INSERT INTO breeds (name) VALUES(?);";

        try {
            if (breed.getName().isEmpty()) {
                System.out.println("Не все поля заполнены.");
                return;
            }

            PreparedStatement usernameCheckSt = getDbConnection().prepareStatement(usernameCheck);
            usernameCheckSt.setString(1, breed.getName());
            ResultSet usernameCheckRes = usernameCheckSt.executeQuery();
            if (usernameCheckRes.next()) {
                System.out.println("Порода уже существует.");
                return;
            }

            PreparedStatement prSt = getDbConnection().prepareStatement(insertBreed);
            prSt.setString(1, breed.getName());
            int result = prSt.executeUpdate();

            if (result == 0) {
                System.out.println("Не удалось добавить породу.");
            } else {
                System.out.println("Новая порода успешно добавлена.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void newAppointment(Appointments appointment) {
        try {
            // Проверяем, что атрибут owner не равен null
            if (appointment.getOwner() == null) {
                throw new IllegalArgumentException("Не удалось получить данные owner.");
            }

            // Получаем id породы по ее названию
            String selectBreedId = "SELECT animals.id FROM animals JOIN breeds ON animals.id_breed = breeds.id WHERE animals.name = ? AND breeds.name = ?";
            PreparedStatement breedIdStatement = getDbConnection().prepareStatement(selectBreedId);
            breedIdStatement.setString(1, appointment.getAnimal().split(" - ")[0].split("\\.")[0]);
            breedIdStatement.setString(2, appointment.getAnimal().split(" - ")[1].split("\\.")[0]);
            ResultSet breedIdResultSet = breedIdStatement.executeQuery();
            int breedId;
            if (breedIdResultSet.next()) {
                breedId = breedIdResultSet.getInt("id");
            } else {
                throw new IllegalArgumentException("Порода с названием " + appointment.getAnimal().split(" - ")[1].split("\\.")[0] + " не найдена.");
            }

            // Получаем id владельца по его имени и телефону
            String ownerData = appointment.getAnimal().split(" - ")[1].split("\\. ")[1];
            String ownerName;
            String ownerTelephone;
            Integer ownerId;

            if (ownerData.equals("Без хозяина")) {
                ownerId = null;
            } else {
                ownerName = ownerData.split(", ")[0];
                ownerTelephone = ownerData.split(", ")[1];

                String selectOwnerId = "SELECT id FROM owners WHERE name = ? AND telephone = ?";
                PreparedStatement ownerIdStatement = getDbConnection().prepareStatement(selectOwnerId);
                ownerIdStatement.setString(1, ownerName);
                ownerIdStatement.setString(2, ownerTelephone);
                ResultSet ownerIdResultSet = ownerIdStatement.executeQuery();

                if (ownerIdResultSet.next()) {
                    ownerId = ownerIdResultSet.getInt("id");
                } else {
                    throw new IllegalArgumentException("Владелец с именем " + ownerName + " и номером телефона " + ownerTelephone + " не найден.");
                }
            }

            // Получаем id доктора по его имени и телефону
            String selectDoctorId = "SELECT id FROM doctors WHERE name = ? AND telephone = ?";
            PreparedStatement doctorIdStatement = getDbConnection().prepareStatement(selectDoctorId);
            doctorIdStatement.setString(1, appointment.getDoctor().split(", ")[0]);
            doctorIdStatement.setString(2, appointment.getDoctor().split(", ")[1]);
            ResultSet doctorIdResultSet = doctorIdStatement.executeQuery();
            int doctorId;
            if (doctorIdResultSet.next()) {
                doctorId = doctorIdResultSet.getInt("id");
            } else {
                throw new IllegalArgumentException("Доктор с именем " + appointment.getDoctor().split(", ")[0] + " и номером телефона " + appointment.getDoctor().split(", ")[1] + " не найден.");
            }

            // Получаем id заболевания по его научному названию
            String selectDiseaseId = "SELECT id FROM diseases WHERE scientific_name = ?";
            PreparedStatement diseaseIdStatement = getDbConnection().prepareStatement(selectDiseaseId);
            diseaseIdStatement.setString(1, appointment.getDisease());
            ResultSet diseaseIdResultSet = diseaseIdStatement.executeQuery();
            int diseaseId;
            if (diseaseIdResultSet.next()) {
                diseaseId = diseaseIdResultSet.getInt("id");
            } else {
                throw new IllegalArgumentException("Заболевание с научным названием " + appointment.getDisease() + " не найдено.");
            }

            String date = appointment.getDate();
            String time = appointment.getTime();

            // Проверяем, существует ли запись с такой датой, временем и доктором
            String selectExistingAppointment = "SELECT id FROM appointments WHERE date = ? AND time = ? AND id_doctor = ?";
            PreparedStatement existingAppointmentStatement = getDbConnection().prepareStatement(selectExistingAppointment);
            existingAppointmentStatement.setString(1, date);
            existingAppointmentStatement.setString(2, time);
            existingAppointmentStatement.setInt(3, doctorId);
            ResultSet existingAppointmentResultSet = existingAppointmentStatement.executeQuery();

            if (existingAppointmentResultSet.next()) {
                //System.out.println("Запись с такой датой, временем и доктором уже существует.");
                // Получаем id приема
                String selectAppointmentId = "SELECT id FROM appointments WHERE date = ? AND time = ? AND id_doctor = ?";
                PreparedStatement appointmentIdStatement = getDbConnection().prepareStatement(selectAppointmentId);
                appointmentIdStatement.setString(1, date);
                appointmentIdStatement.setString(2, time);
                appointmentIdStatement.setInt(3, doctorId);
                ResultSet appointmentIdResultSet = appointmentIdStatement.executeQuery();
                int appointmentId;
                if (appointmentIdResultSet.next()) {
                    appointmentId = appointmentIdResultSet.getInt("id");
                } else {
                    throw new IllegalArgumentException("Прием не найден.");
                }
                // Добавляем новый прием в таблицу appointment_disease
                String insertAppointmentDisease = "INSERT INTO appointment_disease (id_appointment, id_disease) VALUES (?, ?)";
                PreparedStatement insertAppointmentDiseaseStatement = getDbConnection().prepareStatement(insertAppointmentDisease);
                insertAppointmentDiseaseStatement.setInt(1, appointmentId);
                insertAppointmentDiseaseStatement.setInt(2, diseaseId);
                int result2 = insertAppointmentDiseaseStatement.executeUpdate();
            } else {
                // Добавляем новый прием в таблицу appointments
                String insertAppointment = "INSERT INTO appointments (id_animal, id_owner, id_doctor, date, time) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertAppointmentStatement = getDbConnection().prepareStatement(insertAppointment);
                insertAppointmentStatement.setInt(1, breedId);
                insertAppointmentStatement.setObject(2, ownerId);
                insertAppointmentStatement.setInt(3, doctorId);
                insertAppointmentStatement.setString(4, date);
                insertAppointmentStatement.setString(5, time);
                int result = insertAppointmentStatement.executeUpdate();

                // Получаем id приема
                String selectAppointmentId = "SELECT id FROM appointments WHERE date = ? AND time = ? AND id_doctor = ?";
                PreparedStatement appointmentIdStatement = getDbConnection().prepareStatement(selectAppointmentId);
                appointmentIdStatement.setString(1, date);
                appointmentIdStatement.setString(2, time);
                appointmentIdStatement.setInt(3, doctorId);
                ResultSet appointmentIdResultSet = appointmentIdStatement.executeQuery();
                int appointmentId;
                if (appointmentIdResultSet.next()) {
                    appointmentId = appointmentIdResultSet.getInt("id");
                } else {
                    throw new IllegalArgumentException("Прием не найден.");
                }
                // Добавляем новый прием в таблицу appointment_disease
                String insertAppointmentDisease = "INSERT INTO appointment_disease (id_appointment, id_disease) VALUES (?, ?)";
                PreparedStatement insertAppointmentDiseaseStatement = getDbConnection().prepareStatement(insertAppointmentDisease);
                insertAppointmentDiseaseStatement.setInt(1, appointmentId);
                insertAppointmentDiseaseStatement.setInt(2, diseaseId);
                int result2 = insertAppointmentDiseaseStatement.executeUpdate();

                if (result == 1) {
                    System.out.println("Новый прием успешно добавлен.");
                } else {
                    System.out.println("Не удалось добавить новый прием.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateAdministrator(Administrators administrator) {
        String select = "SELECT * FROM users JOIN administrators ON users.id = administrators.id_user WHERE login = ?";
        String selectByUsername = "SELECT * FROM users WHERE login = ?";
        String updateAdmins = "UPDATE administrators SET first_name = ?, last_name = ?, second_name = ? WHERE id_user = ?";
        String updateUser = "UPDATE users SET login = ?, password = ? WHERE id = ?"; // Добавлено обновление логина

        try {
            // Проверяем, не совпадает ли текущий логин с новым логином
            PreparedStatement selectStatement = getInstance().getDbConnection().prepareStatement(select);
            selectStatement.setString(1, MainController.getUserLogin());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id_user");
                String currentLogin = resultSet.getString("login");

                // Проверяем, не совпадает ли текущий логин с новым логином
                if (!currentLogin.equals(administrator.getLogin())) {
                    // Проверяем, не существует ли уже такой логин
                    PreparedStatement usernameCheckSt = getInstance().getDbConnection().prepareStatement(selectByUsername);
                    usernameCheckSt.setString(1, administrator.getLogin());
                    ResultSet usernameCheckRes = usernameCheckSt.executeQuery();
                    if (usernameCheckRes.next()) {
                        int userIdToUpdate = getUserIdByLogin(administrator.getLogin()); // метод для получения id пользователя по логину

                        if (userIdToUpdate != 0 && userIdToUpdate != userId) {
                            System.out.println("Логин уже существует.");
                            return;
                        }
                    }
                }

                // Захешируем пароль, если он указан
                String hashedPassword = null;
                if (administrator.getPassword() != null && !administrator.getPassword().isEmpty()) {
                    hashedPassword = PasswordHasher.hashPassword(administrator.getPassword());
                }

                PreparedStatement updateAdminsStatement = getInstance().getDbConnection().prepareStatement(updateAdmins);
                updateAdminsStatement.setString(1, administrator.getFirstName());
                updateAdminsStatement.setString(2, administrator.getLastName());
                updateAdminsStatement.setString(3, administrator.getSecondName());
                updateAdminsStatement.setInt(4, userId);
                int rowsAffectedAdmins = updateAdminsStatement.executeUpdate();

                PreparedStatement updateUserStatement = getInstance().getDbConnection().prepareStatement(updateUser);
                if (!currentLogin.equals(administrator.getLogin())) {
                    updateUserStatement.setString(1, administrator.getLogin()); // Обновляем логин
                } else {
                    updateUserStatement.setString(1, currentLogin); // Оставляем текущий логин
                }
                if (hashedPassword != null) {
                    updateUserStatement.setString(2, hashedPassword);
                } else {
                    updateUserStatement.setNull(2, java.sql.Types.VARCHAR);
                }
                updateUserStatement.setInt(3, userId);
                int rowsAffectedUser = updateUserStatement.executeUpdate();

                if (rowsAffectedAdmins == 1 || rowsAffectedUser == 1) {
                    System.out.println("Данные администратора успешно обновлены.");
                } else {
                    System.out.println("Не удалось обновить данные администратора.");
                }
            } else {
                System.out.println("Администратор не найден.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private int getUserIdByLogin(String login) throws SQLException, ClassNotFoundException {
        String selectUserId = "SELECT id FROM users WHERE login = ?";
        PreparedStatement selectUserIdStatement = getInstance().getDbConnection().prepareStatement(selectUserId);
        selectUserIdStatement.setString(1, login);
        ResultSet userIdResultSet = selectUserIdStatement.executeQuery();

        if (userIdResultSet.next()) {
            return userIdResultSet.getInt("id");
        }

        return 0;
    }
    public void deleteBreed(Breeds breed) {
        String idCheck = "SELECT id FROM breeds WHERE name = ?";
        String deleteBreed = "DELETE FROM breeds WHERE id = ?";

        try {
            if (breed.getName().isEmpty()) {
                System.out.println("Не все поля заполнены.");
                return;
            }

            PreparedStatement idCheckStatement = getDbConnection().prepareStatement(idCheck);
            idCheckStatement.setString(1, breed.getName());
            ResultSet idCheckResultSet = idCheckStatement.executeQuery();
            if (!idCheckResultSet.next()) {
                System.out.println("Порода не найдена.");
                return;
            }
            int breedId = idCheckResultSet.getInt("id");

            PreparedStatement deleteBreedStatement = getDbConnection().prepareStatement(deleteBreed);
            deleteBreedStatement.setInt(1, breedId);
            int result = deleteBreedStatement.executeUpdate();

            if (result == 0) {
                System.out.println("Не удалось удалить породу.");
            } else {
                System.out.println("Порода успешно удалена.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteAnimal(Animals animal) {
        String idBreed = "SELECT id FROM breeds WHERE name = ?";
        String idCheck = "SELECT id FROM animals WHERE name = ? AND id_breed = ?";
        String deleteAnimal = "DELETE FROM animals WHERE id = ?";

        try {
            if (animal.getName().isEmpty() || animal.getBreed().isEmpty()) {
                System.out.println("Не все поля заполнены.");
                return;
            }

            int breedId;
            if (!animal.getOwner().equals("Без хозяина")) {
                String idOwner = "SELECT id FROM owners WHERE name = ? AND telephone = ?";
                PreparedStatement idCheckOwner = getDbConnection().prepareStatement(idOwner);
                idCheckOwner.setString(1, animal.getOwner().split(", ")[0]);
                idCheckOwner.setString(2, animal.getOwner().split(", ")[1]);
                ResultSet idOwnerResultSet = idCheckOwner.executeQuery();
                if (!idOwnerResultSet.next()) {
                    System.out.println("Владелец не найден.");
                    return;
                }
                int ownerId = idOwnerResultSet.getInt("id");

                PreparedStatement idCheckBreed = getDbConnection().prepareStatement(idBreed);
                idCheckBreed.setString(1, animal.getBreed());
                ResultSet idBreedResultSet = idCheckBreed.executeQuery();
                if (!idBreedResultSet.next()) {
                    System.out.println("Порода не найдена.");
                    return;
                }
                breedId = idBreedResultSet.getInt("id");

                PreparedStatement idCheckStatement = getDbConnection().prepareStatement(idCheck);
                idCheckStatement.setString(1, animal.getName());
                idCheckStatement.setInt(2, breedId);
                ResultSet idCheckResultSet = idCheckStatement.executeQuery();
                if (!idCheckResultSet.next()) {
                    System.out.println("Животное не найдено.");
                    return;
                }
                int animalId = idCheckResultSet.getInt("id");

                PreparedStatement deleteAnimalStatement = getDbConnection().prepareStatement(deleteAnimal);
                deleteAnimalStatement.setInt(1, animalId);
                int result = deleteAnimalStatement.executeUpdate();

                if (result == 0) {
                    System.out.println("Не удалось удалить животное.");
                } else {
                    System.out.println("Животное успешно удалено.");
                }
            } else {
                PreparedStatement idCheckBreed = getDbConnection().prepareStatement(idBreed);
                idCheckBreed.setString(1, animal.getBreed());
                ResultSet idBreedResultSet = idCheckBreed.executeQuery();
                if (!idBreedResultSet.next()) {
                    System.out.println("Порода не найдена.");
                    return;
                }
                breedId = idBreedResultSet.getInt("id");

                PreparedStatement idCheckStatement = getDbConnection().prepareStatement(idCheck);
                idCheckStatement.setString(1, animal.getName());
                idCheckStatement.setInt(2, breedId);
                ResultSet idCheckResultSet = idCheckStatement.executeQuery();
                if (!idCheckResultSet.next()) {
                    System.out.println("Животное не найдено.");
                    return;
                }
                int animalId = idCheckResultSet.getInt("id");
                PreparedStatement deleteAnimalStatement = getDbConnection().prepareStatement(deleteAnimal);
                deleteAnimalStatement.setInt(1, animalId);
                int result = deleteAnimalStatement.executeUpdate();

                if (result == 0) {
                    System.out.println("Не удалось удалить животное.");
                } else {
                    System.out.println("Животное успешно удалено.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteAppointment(Appointments appointment) {
        String idDoctor = "SELECT id FROM doctors WHERE name = ? AND telephone = ?";
        String idCheck = "SELECT id FROM appointments WHERE date = ? AND time = ? AND id_doctor = ?";
        String deleteAppointment = "DELETE FROM appointments WHERE id = ?";

        try {
            if (appointment.getDate().isEmpty() || appointment.getTime().isEmpty() || appointment.getDoctor().isEmpty()) {
                System.out.println("Не все поля заполнены.");
                return;
            }

            PreparedStatement idCheckDoctor = getDbConnection().prepareStatement(idDoctor);
            idCheckDoctor.setString(1, appointment.getDoctor().split(", ")[0]);
            idCheckDoctor.setString(2, appointment.getDoctor().split(", ")[1]);
            ResultSet idDoctorResultSet = idCheckDoctor.executeQuery();
            if (!idDoctorResultSet.next()) {
                System.out.println("Врач не найден.");
                return;
            }
            int doctorId = idDoctorResultSet.getInt("id");

            PreparedStatement idCheckStatement = getDbConnection().prepareStatement(idCheck);
            idCheckStatement.setString(1, appointment.getDate());
            idCheckStatement.setString(2, appointment.getTime());
            idCheckStatement.setInt(3, doctorId);
            ResultSet idCheckResultSet = idCheckStatement.executeQuery();
            if (!idCheckResultSet.next()) {
                System.out.println("Запись о приеме не найдена.");
                return;
            }
            int appointmentId = idCheckResultSet.getInt("id");

            PreparedStatement deleteAppointmentStatement = getDbConnection().prepareStatement(deleteAppointment);
            deleteAppointmentStatement.setInt(1, appointmentId);
            int result = deleteAppointmentStatement.executeUpdate();

            if (result == 0) {
                System.out.println("Не удалось удалить запись о приеме.");
            } else {
                System.out.println("Запись о приеме успешно удалена.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteDoctor(Doctors doctor) {
        String idDoctor = "SELECT id, id_user FROM doctors WHERE name = ? AND telephone = ?";
        String deleteAppointment = "DELETE FROM doctors WHERE id = ?";
        String deleteUser = "DELETE FROM users WHERE id = ?";

        try {
            if (doctor.getName().isEmpty() || doctor.getTelephone().isEmpty()) {
                System.out.println("Не все поля заполнены.");
                return;
            }

            PreparedStatement idCheckDoctor = getDbConnection().prepareStatement(idDoctor);
            idCheckDoctor.setString(1, doctor.getName());
            idCheckDoctor.setString(2, doctor.getTelephone());
            ResultSet idDoctorResultSet = idCheckDoctor.executeQuery();
            if (!idDoctorResultSet.next()) {
                System.out.println("Врач не найден.");
                return;
            }
            int doctorId = idDoctorResultSet.getInt("id");
            int userId = idDoctorResultSet.getInt("id_user");

            PreparedStatement deleteAppointmentStatement = getDbConnection().prepareStatement(deleteAppointment);
            deleteAppointmentStatement.setInt(1, doctorId);
            int result1 = deleteAppointmentStatement.executeUpdate();

            PreparedStatement deleteUserStatement = getDbConnection().prepareStatement(deleteUser);
            deleteUserStatement.setInt(1, userId);
            int result2 = deleteUserStatement.executeUpdate();

            if (result1 > 0 || result2 > 0) {
                System.out.println("Врач успешно удален.");
            } else {
                System.out.println("Не удалось удалить врача.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteOwner(Owners owner) {
        String idDoctor = "SELECT id, id_user FROM owners WHERE name = ? AND telephone = ?";
        String deleteAppointment = "DELETE FROM owners WHERE id = ?";
        String deleteUser = "DELETE FROM users WHERE id = ?";

        try {
            if (owner.getName().isEmpty() || owner.getTelephone().isEmpty()) {
                System.out.println("Не все поля заполнены.");
                return;
            }

            PreparedStatement idCheckDoctor = getDbConnection().prepareStatement(idDoctor);
            idCheckDoctor.setString(1, owner.getName());
            idCheckDoctor.setString(2, owner.getTelephone());
            ResultSet idDoctorResultSet = idCheckDoctor.executeQuery();
            if (!idDoctorResultSet.next()) {
                System.out.println("Владелец не найден.");
                return;
            }
            int doctorId = idDoctorResultSet.getInt("id");
            int userId = idDoctorResultSet.getInt("id_user");

            PreparedStatement deleteAppointmentStatement = getDbConnection().prepareStatement(deleteAppointment);
            deleteAppointmentStatement.setInt(1, doctorId);
            int result1 = deleteAppointmentStatement.executeUpdate();

            PreparedStatement deleteUserStatement = getDbConnection().prepareStatement(deleteUser);
            deleteUserStatement.setInt(1, userId);
            int result2 = deleteUserStatement.executeUpdate();

            if (result1 > 0 || result2 > 0) {
                System.out.println("Владелец успешно удален.");
            } else {
                System.out.println("Не удалось удалить владельца.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteAdministrator(Administrators admin) {
        String idAdministrator = "SELECT administrators.id, administrators.id_user FROM administrators JOIN users ON administrators.id_user = users.id WHERE login = ?";
        String deleteAdministrator = "DELETE FROM administrators WHERE id = ?";
        String deleteUser = "DELETE FROM users WHERE id = ?";

        try {
            if (admin.getFirstName().isEmpty() || admin.getLastName().isEmpty() || admin.getSecondName().isEmpty()) {
                System.out.println("Не все поля заполнены.");
                return;
            }

            PreparedStatement idCheckAdministrator = getDbConnection().prepareStatement(idAdministrator);
            idCheckAdministrator.setString(1, admin.getLogin());
            ResultSet idAdministratorResultSet = idCheckAdministrator.executeQuery();
            if (!idAdministratorResultSet.next()) {
                System.out.println("Администратор не найден.");
                return;
            }
            int administratorId = idAdministratorResultSet.getInt("id");
            int userId = idAdministratorResultSet.getInt("id_user");

            PreparedStatement deleteAdministratorStatement = getDbConnection().prepareStatement(deleteAdministrator);
            deleteAdministratorStatement.setInt(1, administratorId);
            int result1 = deleteAdministratorStatement.executeUpdate();

            PreparedStatement deleteUserStatement = getDbConnection().prepareStatement(deleteUser);
            deleteUserStatement.setInt(1, userId);
            int result2 = deleteUserStatement.executeUpdate();

            if (result1 > 0 || result2 > 0) {
                System.out.println("Администратор успешно удален.");
            } else {
                System.out.println("Не удалось удалить администратора.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String searchOwner() {
        String name = "";
        String nameOwner = "SELECT owners.name AS ownerName, owners.telephone AS ownerTelephone FROM owners JOIN users ON owners.id_user = users.id WHERE login = ?";
        try (PreparedStatement stmt = getDbConnection().prepareStatement(nameOwner)) {
            stmt.setString(1, MainController.getUserLogin());
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return "Владелец не найден.";
                }
                name = rs.getString("ownerName") + ", " + rs.getString("ownerTelephone");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return name;
    }
    public String searchDoctor() {
        String name = "";
        String nameOwner = "SELECT doctors.name AS ownerName, doctors.telephone AS ownerTelephone FROM doctors JOIN users ON doctors.id_user = users.id WHERE login = ?";
        try (PreparedStatement stmt = getDbConnection().prepareStatement(nameOwner)) {
            stmt.setString(1, MainController.getUserLogin());
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return "Врач не найден.";
                }
                name = rs.getString("ownerName") + ", " + rs.getString("ownerTelephone");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return name;
    }
    public String searchAdmin() {
        String name = "";
        String nameOwner = "SELECT first_name, last_name, second_name FROM administrators JOIN users ON administrators.id_user = users.id WHERE login = ?";
        try (PreparedStatement stmt = getDbConnection().prepareStatement(nameOwner)) {
            stmt.setString(1, MainController.getUserLogin());
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return "Врач не найден.";
                }
                name = rs.getString("last_name") + " " + rs.getString("first_name") + " " + rs.getString("second_name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return name;
    }
    public void updateDoctor(Doctors doctor) {
        String select = "SELECT * FROM users JOIN doctors ON users.id = doctors.id_user WHERE login = ?";
        String selectByUsername = "SELECT * FROM users WHERE login = ?";
        String updateDoctors = "UPDATE doctors SET name = ?, address = ?, telephone = ? WHERE id_user = ?";
        String updateUser = "UPDATE users SET login = ?, password = ? WHERE id = ?";

        try {
            PreparedStatement selectStatement = getInstance().getDbConnection().prepareStatement(select);
            selectStatement.setString(1, MainController.getUserLogin());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id_user");
                String currentLogin = resultSet.getString("login");

                if (!currentLogin.equals(doctor.getLogin())) {
                    PreparedStatement usernameCheckSt = getInstance().getDbConnection().prepareStatement(selectByUsername);
                    usernameCheckSt.setString(1, doctor.getLogin());
                    ResultSet usernameCheckRes = usernameCheckSt.executeQuery();
                    if (usernameCheckRes.next()) {
                        int userIdToUpdate = getUserIdByLogin(doctor.getLogin());

                        if (userIdToUpdate != 0 && userIdToUpdate != userId) {
                            System.out.println("Логин уже существует.");
                            return;
                        }
                    }
                }

                String hashedPassword = null;
                if (doctor.getPassword() != null && !doctor.getPassword().isEmpty()) {
                    hashedPassword = PasswordHasher.hashPassword(doctor.getPassword());
                }

                PreparedStatement updateDoctorsStatement = getInstance().getDbConnection().prepareStatement(updateDoctors);
                updateDoctorsStatement.setString(1, doctor.getName());
                updateDoctorsStatement.setString(2, doctor.getAddress());
                updateDoctorsStatement.setString(3, doctor.getTelephone());
                updateDoctorsStatement.setInt(4, userId);
                int rowsAffectedDoctors = updateDoctorsStatement.executeUpdate();

                PreparedStatement updateUserStatement = getInstance().getDbConnection().prepareStatement(updateUser);
                if (!currentLogin.equals(doctor.getLogin())) {
                    updateUserStatement.setString(1, doctor.getLogin());
                } else {
                    updateUserStatement.setString(1, currentLogin);
                }
                if (hashedPassword != null) {
                    updateUserStatement.setString(2, hashedPassword);
                } else {
                    updateUserStatement.setNull(2, java.sql.Types.VARCHAR);
                }
                updateUserStatement.setInt(3, userId);
                int rowsAffectedUser = updateUserStatement.executeUpdate();

                if (rowsAffectedDoctors == 1 || rowsAffectedUser == 1) {
                    System.out.println("Данные врача успешно обновлены.");
                } else {
                    System.out.println("Не удалось обновить данные врача.");
                }
            } else {
                System.out.println("Врач не найден.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateOwner(Owners owner) {
        String select = "SELECT * FROM users JOIN owners ON users.id = owners.id_user WHERE login = ?";
        String selectByUsername = "SELECT * FROM users WHERE login = ?";
        String updateDoctors = "UPDATE owners SET name = ?, address = ?, telephone = ? WHERE id_user = ?";
        String updateUser = "UPDATE users SET login = ?, password = ? WHERE id = ?";

        try {
            PreparedStatement selectStatement = getInstance().getDbConnection().prepareStatement(select);
            selectStatement.setString(1, MainController.getUserLogin());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id_user");
                String currentLogin = resultSet.getString("login");

                if (!currentLogin.equals(owner.getLogin())) {
                    PreparedStatement usernameCheckSt = getInstance().getDbConnection().prepareStatement(selectByUsername);
                    usernameCheckSt.setString(1, owner.getLogin());
                    ResultSet usernameCheckRes = usernameCheckSt.executeQuery();
                    if (usernameCheckRes.next()) {
                        int userIdToUpdate = getUserIdByLogin(owner.getLogin());

                        if (userIdToUpdate != 0 && userIdToUpdate != userId) {
                            System.out.println("Логин уже существует.");
                            return;
                        }
                    }
                }

                String hashedPassword = null;
                if (owner.getPassword() != null && !owner.getPassword().isEmpty()) {
                    hashedPassword = PasswordHasher.hashPassword(owner.getPassword());
                }

                PreparedStatement updateDoctorsStatement = getInstance().getDbConnection().prepareStatement(updateDoctors);
                updateDoctorsStatement.setString(1, owner.getName());
                updateDoctorsStatement.setString(2, owner.getAddress());
                updateDoctorsStatement.setString(3, owner.getTelephone());
                updateDoctorsStatement.setInt(4, userId);
                int rowsAffectedDoctors = updateDoctorsStatement.executeUpdate();

                PreparedStatement updateUserStatement = getInstance().getDbConnection().prepareStatement(updateUser);
                if (!currentLogin.equals(owner.getLogin())) {
                    updateUserStatement.setString(1, owner.getLogin());
                } else {
                    updateUserStatement.setString(1, currentLogin);
                }
                if (hashedPassword != null) {
                    updateUserStatement.setString(2, hashedPassword);
                } else {
                    updateUserStatement.setNull(2, java.sql.Types.VARCHAR);
                }
                updateUserStatement.setInt(3, userId);
                int rowsAffectedUser = updateUserStatement.executeUpdate();

                if (rowsAffectedDoctors == 1 || rowsAffectedUser == 1) {
                    System.out.println("Данные владельца успешно обновлены.");
                } else {
                    System.out.println("Не удалось обновить данные владельца.");
                }
            } else {
                System.out.println("Владелец не найден.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Diseases> getTableDiseases() {
        List<Diseases> diseases = new ArrayList<>();
        ResultSet resultSet = null;
        String select = "SELECT * FROM diseases";

        try {
            PreparedStatement prSt = getInstance().getDbConnection().prepareStatement(select);

            resultSet = prSt.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String common_name = resultSet.getString("common_name");
                String scientific_name = resultSet.getString("scientific_name");
                Diseases diseas = new Diseases(id, common_name, scientific_name);
                diseases.add(diseas);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return diseases;
    }
    public void newDisease(Diseases disease) {
        String usernameCheck = "SELECT * FROM diseases WHERE common_name =? AND scientific_name = ?";
        String insertBreed = "INSERT INTO diseases (common_name, scientific_name) VALUES(?, ?);";

        try {
            if (disease.getCommon_name().isEmpty() && disease.getScientific_name().isEmpty()) {
                System.out.println("Не все поля заполнены.");
                return;
            }

            PreparedStatement usernameCheckSt = getDbConnection().prepareStatement(usernameCheck);
            usernameCheckSt.setString(1, disease.getCommon_name());
            usernameCheckSt.setString(2, disease.getScientific_name());
            ResultSet usernameCheckRes = usernameCheckSt.executeQuery();
            if (usernameCheckRes.next()) {
                System.out.println("Заболевание уже существует.");
                return;
            }

            PreparedStatement prSt = getDbConnection().prepareStatement(insertBreed);
            prSt.setString(1, disease.getCommon_name());
            prSt.setString(2, disease.getScientific_name());
            int result = prSt.executeUpdate();

            if (result == 0) {
                System.out.println("Не удалось добавить заболевание.");
            } else {
                System.out.println("Новое заболевание успешно добавлено.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteDisease(Diseases disease) {
        String idCheck = "SELECT id FROM diseases WHERE common_name = ? AND scientific_name = ?";
        String deleteBreed = "DELETE FROM diseases WHERE id = ?";

        try {
            if (disease.getCommon_name().isEmpty() && disease.getScientific_name().isEmpty()) {
                System.out.println("Не все поля заполнены.");
                return;
            }

            PreparedStatement idCheckStatement = getDbConnection().prepareStatement(idCheck);
            idCheckStatement.setString(1, disease.getCommon_name());
            idCheckStatement.setString(2, disease.getScientific_name());
            ResultSet idCheckResultSet = idCheckStatement.executeQuery();
            if (!idCheckResultSet.next()) {
                System.out.println("Заболевание не найдено.");
                return;
            }
            int breedId = idCheckResultSet.getInt("id");

            PreparedStatement deleteBreedStatement = getDbConnection().prepareStatement(deleteBreed);
            deleteBreedStatement.setInt(1, breedId);
            int result = deleteBreedStatement.executeUpdate();

            if (result == 0) {
                System.out.println("Не удалось удалить заболевание.");
            } else {
                System.out.println("Заболевание успешно удалено.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
