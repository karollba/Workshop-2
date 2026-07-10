package pl.coderslab;

import java.sql.SQLException;
import java.util.Scanner;

import static pl.coderslab.DbUtil.*;
import static pl.coderslab.entity.UserDao.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        System.out.println("Hello! What are we going to do today?");
        System.out.println("1. Add new user");
        System.out.println("2. Update info about existing user");
        System.out.println("3. Print all users");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> newUserInput();
            case "2" -> update();
            case "3" -> printAllUsers();
            default -> System.out.println("Please select a correct option");
        }
    }
}