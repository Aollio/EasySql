package com.aollio.easysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Finderlo on 2016/10/29.
 */
public class SpecConnection {

    private Connection connection;

    boolean isBusy = false;

    boolean isEnable = true;

    public SpecConnection(Connection connection) {
        this.connection = connection;
    }

    int executeUpdate(String sql) {
        int count = 0;
        try {
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            Statement statement = connection.createStatement();
            count = statement.executeUpdate(sql);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            count = 0;
        }
        return count;
    }

    ResultSet executeQuery(String sql) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void returnConn() {
        isBusy = false;
    }

    public boolean closeConn() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        isEnable = false;
        return true;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public boolean isBusy() {
        return isBusy;
    }
}
