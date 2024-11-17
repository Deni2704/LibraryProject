package repository.user;
import model.Book;
import model.Role;
import model.User;
import model.builder.BookBuilder;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.USER;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM user;";

        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                System.out.println(getUserFromResultSet(resultSet));
                users.add(getUserFromResultSet(resultSet));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }


    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {

        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            //Statement statement = connection.createStatement();
            //SQL INJECTION !!!"Select * from `" + USER + "` where `username`=\'" + username + "\' and `password`=\'" + password + "\'";
            String fetchUserSql =
            "SELECT * from USER where username = ? and password = ?";
            PreparedStatement statement = connection.prepareStatement(fetchUserSql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet userResultSet = statement.executeQuery();
            if (userResultSet.next())
            {
                User user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();

                findByUsernameAndPasswordNotification.setResult(user);
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }

        return findByUsernameAndPasswordNotification;
    }

    @Override
    public Notification<Boolean> save(User user) {
        Notification <Boolean> saveUserNotification = new Notification<>();
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            int affectedRows = insertUserStatement.executeUpdate();
            if (affectedRows == 0){
                saveUserNotification.addError("Failed to insert user into database");
                saveUserNotification.setResult(false);
                return saveUserNotification;
            }
            ResultSet rs = insertUserStatement.getGeneratedKeys();

            if (rs.next()) {
                long userId = rs.getLong(1);
                user.setId(userId);
                rightsRolesRepository.addRolesToUser(user, user.getRoles());
                saveUserNotification.setResult(true);
            }
            else {
                saveUserNotification.addError("Failed to retrieve the id for user");
                saveUserNotification.setResult(false);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            saveUserNotification.addError("error in database");
        }
        return saveUserNotification;

    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
            //Statement statement = connection.createStatement();
           //SQL INJECTION!!!  "Select * from `" + USER + "` where `username`=\'" + email + "\'";
            String fetchUserSql =
            "SELECT * from user where username = ?  ";
            PreparedStatement statement = connection.prepareStatement(fetchUserSql);
            statement.setString(1, "username");
            ResultSet userResultSet = statement.executeQuery();
            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private User getUserFromResultSet(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        List<Role> roles = rightsRolesRepository.findRolesForUser(id);
        return new UserBuilder()
                .setId(id)
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .setRoles(roles)
                .build();
    }
}