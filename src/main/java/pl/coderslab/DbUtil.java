package pl.coderslab;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;

// stworzyc klase user

// stworzyc klase userdao

public class DbUtil {

    private static Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = dotenv.get("DB_URL");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASS = dotenv.get("DB_PASS");

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}