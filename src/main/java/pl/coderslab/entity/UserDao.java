package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.*;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.setOut;
import static pl.coderslab.DbUtil.*;

public class UserDao {

    static Scanner scanner = new Scanner(System.in);

    public static User getUserByEmail(Connection conn, String email) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(getGetUserByEmail());
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            User user = new User(resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("password"));
            user.setId(resultSet.getInt("id"));
            return user;
        }
        return null;
    }

    public static void removeUser(User user) throws SQLException {
        remove(connect(), user.getId(), getRemoveUserById());
        System.out.println("User removed successfully");
    }

    //     adduser
    public static void newUserInput() throws SQLException {
        System.out.println("Please provide username:");
        String userInputUserName = scanner.nextLine();

        System.out.println("Please provide email address:");
        String userInputEmailAddress = scanner.nextLine();

        System.out.println("Please provide password:");
        String userInputPassword = scanner.nextLine();

        User user = new User(userInputUserName, userInputEmailAddress, userInputPassword);
        User createUser = create(user);

    }

    public static void update() throws SQLException {
        System.out.println("Please provide your email address ");
        String loginInput = scanner.nextLine();
        User user = getUserByEmail(connect(), loginInput);

        while (user == null) {
            System.out.println("Incorrect email, please try again");
            loginInput = scanner.nextLine();
            user = getUserByEmail(connect(), loginInput);
        }

        System.out.println("Please provide your password");
        String passwordInput = scanner.nextLine();

        while (!BCrypt.checkpw(passwordInput, user.getPassword())) {
            System.out.println("Incorrect password, please try again!");
            passwordInput = scanner.nextLine();
        }

        System.out.println("Please provide which section you wish to update: usermame, email, password, delete account ");

        while (true) {
            String userOption = scanner.nextLine();
            switch (userOption) {
                case "username" -> {
                    try {
                        System.out.println("Please input new username: ");
                        String changedUsername = scanner.nextLine();
                        user.setUserName(changedUsername);
                        DbUtil.update(connect(), user);
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    return;
                }
                case "email" -> {
                    try {
                        System.out.println("Please input new email address: ");
                        String changedEmail = scanner.nextLine();
                        user.setEmail(changedEmail);
                        DbUtil.update(connect(), user);
                    } catch (SQLIntegrityConstraintViolationException e) {
                        System.out.println("This address eamil already exists, please provide another");
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    return;
                }
                case "password" -> {
                    try {
                        System.out.println("Please input new password: ");
                        String changedPassword = scanner.nextLine();
                        user.setPassword(DbUtil.hashPassword(changedPassword));
                        DbUtil.update(connect(), user);
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    return;
                }
                case "delete" -> {
                    try {
                        removeUser(user);
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    return;
                }
                case "exit" -> {
                    return;
                }
                default -> System.out.println("Please select a correct option.");
            }
        }
    }
}


//    public static User getUserById(Connection conn, int id) throws SQLException {
//        PreparedStatement statement = conn.prepareStatement(getGetUserById());
//        statement.setInt(1, id);
//        ResultSet resultSet = statement.executeQuery();
//        resultSet.next();
//
//        User user = new User(resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("password"));
//        user.setId(resultSet.getInt("id"));
//        return user;
//    }