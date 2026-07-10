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

//    public static User[] getAllUsers(Connection conn, String query) throws SQLException {
//        int rows = countAll(conn);
//        User[] users = new User[rows];
//
//        PreparedStatement statement = conn.prepareStatement(query);
//        ResultSet resultSet = statement.executeQuery();
//
//        int i = 0;
//        while (resultSet.next()) {
//            users[i] = new User(resultSet.getInt("id"), resultSet.getString("userName"), resultSet.getString("email"));
//            i++;
//        }
//        return users;
//    }

    public static User getUserById(Connection conn, int id) throws SQLException {

        PreparedStatement statement = conn.prepareStatement(getGetUserById());
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        User user = new User(resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("password"));
        user.setId(resultSet.getInt("id"));
        return user;
    }

    public static User getUserByEmail(Connection conn, String email) throws SQLException {

        PreparedStatement statement = conn.prepareStatement(getGetUserByEmail());
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        User user = new User(resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("password"));
        user.setId(resultSet.getInt("id"));

        return user;
    }


    public static void removeUser() throws SQLException {
        System.out.println("Please provide user id you wish to remove:");
        String userInput = scanner.nextLine();
        int userInputInt = Integer.parseInt(userInput);

        remove(connect(), userInputInt, getRemoveUserById());


    }

    //     adduser
    public static void newUserInput() throws SQLException {
        System.out.println("Please provide username:");
        String userInputUserName = scanner.nextLine();

        System.out.println("Please provide email address:");
        String userInputEmailAddress = scanner.nextLine();

        // zrob tak zeby nie bylo widac tak fajnie jak masz w terminalu normalnie
        System.out.println("Please provide password:");
        String userInputPassword = scanner.nextLine();

        User user = new User(userInputUserName, userInputEmailAddress, userInputPassword);
        User createUser = create(user);

    }

    public static void update() throws SQLException {
        while (true) try {
            System.out.println("Please provide your email address ");
            String loginInput = scanner.nextLine();
            User user = getUserByEmail(connect(), loginInput);

            if (user != null) {
                System.out.println("Please provide your password: ");
                String passwordInput = scanner.nextLine();

                if (BCrypt.checkpw(passwordInput, user.getPassword()) == true) {
                    while (true) {
                        if (user != null && BCrypt.checkpw(passwordInput, user.getPassword())) {
                            int userInputInt = Integer.parseInt(getGetUserByEmail());
                            System.out.println("Please provide which section you wish to update: usermame, email, password ");
                            while (true) {
                                String userOption = scanner.nextLine();
                                switch (userOption) {
                                    case "username" -> {
                                        try {
//                                User user = getUserById(connect(), userInputInt);
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
//                                User user = getUserById(connect(), userInputInt);
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
                                    case "password" -> { // tutaj jeszcze trzeba sprawdzic hashe chyba? i zahashowac
                                        try {
//                                User user = getUserById(connect(), userInputInt);
                                            System.out.println("Please input new password: ");
                                            String changedPassword = scanner.nextLine();
                                            user.setPassword(changedPassword);
                                            DbUtil.update(connect(), user);
                                        } catch (SQLException e) {
                                            System.out.println("Error: " + e.getMessage());
                                        }
                                        return;
                                    }
                                    default -> System.out.println("Please select a correct option.");
                                }
                            }
                        } else {
                            System.out.println("Incorrect password, please try again");
                        }
                    } else {
                        System.out.println("Inncorect email, pleasy try again");
                    }
                    break;
                }
            }
        }
}
}

