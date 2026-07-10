package pl.coderslab;

import java.sql.SQLException;

import static pl.coderslab.entity.UserDao.removeUser;
import static pl.coderslab.entity.UserDao.updateUser;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
//        removeUser();
        updateUser();
    }
}
