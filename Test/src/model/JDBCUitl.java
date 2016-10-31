package model;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Finderlo on 2016/10/8.
 */
public class JDBCUitl {
    public static final String driverClass = "com.mysql.easysql.config.Driver";
    public static final String jdbcUrl = "easysql.config:mysql://localhost:3306/test";
    public static final String user = "root";
    public static final String password = "houyudong2012";

    static {
        try {
            Driver driver = new Driver();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
