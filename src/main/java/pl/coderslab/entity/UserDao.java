package pl.coderslab.entity;

import pl.coderslab.DbUtil;

import java.sql.*;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.setOut;
import static pl.coderslab.DbUtil.*;


public class UserDao {

    static Scanner scanner = new Scanner(System.in);

    public static User[] getAllUsers(Connection conn, String query) throws SQLException {
        int rows = countAll(conn);
        User[] users = new User[rows];

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        int i = 0;
        while (resultSet.next()) {
            users[i] = new User(resultSet.getInt("id"), resultSet.getString("userName"), resultSet.getString("email"));
            i++;
        }
        return users;
    }

    public static User getUserById(Connection conn, String query, int id) throws SQLException {

        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        User user = new User(resultSet.getInt("id"), resultSet.getString("email"), resultSet.getString("username"));
        return user;
    }


    public static void removeUser() throws SQLException {
        System.out.println("Please provide user id you wish to remove:");
        String userInput = scanner.nextLine();
        int userInputInt = Integer.parseInt(userInput);

        remove(connect(), userInputInt, getRemoveUserById());


    }
//     adduser
    public static void addUser() throws SQLException {
        System.out.println("Please provide username:");
        String userInputUserName = scanner.nextLine();

        System.out.println("Please provide email address:");
        String userInputEmailAddress = scanner.nextLine();

        // zrob tak zeby nie bylo widac tak fajnie jak masz w terminalu normalnie
        System.out.println("Please provide password:");
        String userInputPassword = scanner.nextLine();

       DbUtil.create(user);

    }

    public static void updateUser() {
        System.out.println("Please provide User id you wish to update: ");
        String userInput = scanner.nextLine();
        int userInputInt = Integer.parseInt(userInput);
        System.out.println("Please provide which section you wish to update: usermame, email, password ");

        while (true) {
            String userOption = scanner.nextLine();
            switch (userOption) {
                case "username" -> {
                    try {
                        System.out.println("Please input new username: ");
                        String changedUsername = scanner.nextLine();
                        update(connect(), getUpdateUsername(), userInputInt, changedUsername, "username");
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    return;
                }
                case "email" -> {
                    try {
                        System.out.println("Please input new email address: ");
                        String newEmailAddress = scanner.nextLine();
                        update(connect(), getUpdateEmail(), userInputInt, newEmailAddress, "email");
                    } catch (SQLIntegrityConstraintViolationException e) {
                        System.out.println("This address eamil already exists, please provide another");
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    return;
                }
//                case "password" -> {
//                    try {
//
//                    } catch (SQLException e) {
//                        System.out.println("Error: " + e.getMessage());
//                    }
//                return;
//                }

                default -> System.out.println("Please select a correct option.");

            }
        }
    }




    public static void updateUsername() {
    }

//    // nie najlepsze rozwiazanie zmien!
//    public static void selectOption(){
//        while (true) {
//            options();
//            String userOption = scanner.nextLine();
//
//            switch (userOption) {
//                case "username" -> updateUser();
//                case "email" -> updateUser();
//                case "password" -> updateUser();
//                case "exit" -> {
//                    System.exit(0);
//                }
//
//                default -> System.out.println("Please select a correct option.");
//
//            }
//        }
//    }

}
