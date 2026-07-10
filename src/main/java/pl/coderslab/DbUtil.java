package pl.coderslab;

import com.mysql.cj.exceptions.ConnectionIsClosedException;
import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.entity.User;

import java.sql.*;



public class DbUtil {
    private static final String ADD_USER = "INSERT INTO users (email, username, password) VALUES (?, ?, ?);";
    private static final String UPDATE_USER = "UPDATE users SET username = ?, emmail = ?, password = ?, WHERE id = ?";
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String REMOVE_USER_BY_ID = "DELETE FROM users WHERE id = ?;";
    private static final String GET_ALL_USERS = "SELECT * FROM users";
    private static final String UPDATE_USERNAME = "UPDATE users SET username = ? WHERE id = ?";
    private static final String UPDATE_EMAIL = "UPDATE users SET email = ? WHERE id = ?";
    private static final String UPDATE_PASSWORD = "UPDATE users SET password = ? WHERE id = ?";
    private static final String GET_PASSWORD = "UPDATE users SET password = ? WHERE id = ?";

    private static Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = dotenv.get("DB_URL");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASS = dotenv.get("DB_PASS");

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static String getGetPassword() {
        return GET_PASSWORD;
    }

    public static String getRemoveUserById() {
        return REMOVE_USER_BY_ID;
    }

    public static String getGetUserById() {
        return GET_USER_BY_ID;
    }

    public static String getUpdateUsername() {
        return UPDATE_USERNAME;
    }

    public static String getUpdateEmail() {
        return UPDATE_EMAIL;
    }

    public static String getUpdatePassword() {
        return UPDATE_PASSWORD;
    }

    public static String getGetUserByEmail() {
        return GET_USER_BY_EMAIL;
    }

    public static void insert(Connection conn, String query, String... params) {
        try ( PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(Connection conn, User user) throws SQLException{
        PreparedStatement statement = conn.prepareStatement(UPDATE_USER);
        statement.setString(1, user.getUserName());
        statement.setString(2, user.getEmail());
        statement.setString(3, hashPassword(user.getPassword()));
        statement.setInt(4, user.getId());
        statement.executeUpdate();

    }

    public static void remove(Connection conn, int id, String query) {
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int countAll(Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) FROM users;";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        resultSet.next();
        int count = resultSet.getInt(1);

        return count;
    }

    public static int count(Connection conn, String sqlQuery) throws SQLException {
        String query = "SELECT COUNT(*) as numberOfRows from (" + sqlQuery + ") as result"; // niezbyt dobre roziwazanie sql injection!!!!!

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        resultSet.next();
        int count = resultSet.getInt(1);

        return count;
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static User create(User user) {
        try (Connection conn = DbUtil.connect()) {
            PreparedStatement statement =
                    conn.prepareStatement(ADD_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();

            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void printData(Connection conn, String query, String... columnNames) {

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery();) {
            while (resultSet.next()) {
                for (String columnName : columnNames) {
                    System.out.println(resultSet.getString(columnName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean exists(Connection conn, String tableName, int id) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE id = " + id; // tez niezbyt dobry pomysl sql injection

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        boolean result = resultSet.next();

        return result;
    }

    public static void options() {
        String[] str = {"username", "email", "password"};

        System.out.println("Please select an option:");
        for (String option : str) {
            System.out.println(option);
        }
    }

}

