package pl.coderslab.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// metody maja byc obiektowe (mogloby byx statyczne w ostatecznosci

public class UserDao {
    private static final String ADD_USER = "INSERT INTO users (email, username, password) VALUES (?, ?, ?);";
    private static final String UPDATE_USER = ""
    private static final String GET_USER_BY_ID = "INSERT INTO users (email, username, password) VALUES (?, ?, ?);";
    private static final String REMOVE_USER_BY_ID = "DELETE FROM users WHERE id = ?;";
    private static final String GET_ALL_USERS = "SELECT * FROM users WHERE id = ?";


    public static void insert(Connection conn, String query, String... params) {
        try ( PreparedStatement statement = conn.prepareStatement(ADD_USER)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(Connection conn, String queryUpdate, String... columnNames) {
        try ( PreparedStatement statement = conn.prepareStatement(UPDATE_USER)) {
            for (int i = 0; i < columnNames.length; i++) {
                statement.setString(i + 1, columnNames[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void remove(Connection conn, int id) {
        try {
            PreparedStatement statement = conn.prepareStatement(REMOVE_USER_BY_ID);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static User[] getAllUsers(Connection conn, String tableName) throws SQLException {
        int rows = countAll(conn, "users");
        User[] cinemas = new User[rows];

        PreparedStatement statement = conn.prepareStatement(GET_ALL_USERS);
        ResultSet resultSet = statement.executeQuery();

        int i = 0;
        while (resultSet.next()) {
            cinemas[i] = new User(resultSet.getInt("id"), resultSet.getString("email"), resultSet.getString("username"));
            i++;
        }
        return cinemas;
    }


public static int countAll(Connection conn) throws SQLException {
    String query = "SELECT COUNT(*) FROM users;";
    PreparedStatement statement = conn.prepareStatement(query);
    ResultSet resultSet = statement.executeQuery();

    resultSet.next();
    int count = resultSet.getInt(1);

    return count;
}
}
