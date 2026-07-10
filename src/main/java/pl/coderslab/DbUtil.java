package pl.coderslab;

import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.entity.User;

import java.sql.*;

public class DbUtil {
    private static final String ADD_USER = "INSERT INTO users (email, username, password) VALUES (?, ?, ?);";
    private static final String UPDATE_USER = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String REMOVE_USER_BY_ID = "DELETE FROM users WHERE id = ?;";
    private static final String GET_ALL_USERS = "SELECT * FROM users";

    private static Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = dotenv.get("DB_URL");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASS = dotenv.get("DB_PASS");

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static String getRemoveUserById() {
        return REMOVE_USER_BY_ID;
    }
    public static String getGetUserById() {
        return GET_USER_BY_ID;
    }

    public static String getGetUserByEmail() {
        return GET_USER_BY_EMAIL;
    }

    public static String getGetAllUsers() {
        return GET_ALL_USERS;
    }

    public static void update(Connection conn, User user) throws SQLException{
        PreparedStatement statement = conn.prepareStatement(UPDATE_USER);
        statement.setString(1, user.getUserName());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPassword());
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

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static User create(User user) {
        try (Connection conn = DbUtil.connect()) {
            PreparedStatement statement =
                    conn.prepareStatement(ADD_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
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

    public static User[] getAllUsers(Connection conn) throws SQLException {
        int rows = DbUtil.countAll(conn);
        User[] users = new User[rows];

        PreparedStatement statement = conn.prepareStatement(getGetAllUsers());
        ResultSet resultSet = statement.executeQuery();

        int i = 0;
        while (resultSet.next()) {
           User user = new User(resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("password"));
           user.setId(resultSet.getInt("id"));
           users[i] = user;
           i++;
        }
        return users;
    }

    public static void printAllUsers() throws SQLException{
        User[] users = getAllUsers(connect());
        for (User user : users) {
            System.out.println(user.getId() + ", " + user.getUserName() + ", " + user.getEmail());
        }
    }
}

