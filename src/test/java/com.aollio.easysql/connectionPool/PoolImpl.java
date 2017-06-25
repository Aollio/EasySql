package com.aollio.easysql.connectionPool;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 连接池功能：
 * <p>
 * 1.通过对外接口，可以随时返回一个可用的连接
 * 2.如果连接池中暂时没有连接，则会自动创建一个，否则等待直到有空闲的连接
 * 3.连接池中最大的连接数不能超过设置的数目
 * 4.通过物理驱动来获得连接，需要设置url，user，password等值，从文件中读取
 * 5.已经使用了的connection需要一个标识符来设置当前connection的使用状态
 *
 * <p>
 * 对外接口：
 * 1.getConnection
 */
public class PoolImpl implements Pool {

    //定义数据库信息
    private String driver = "com.mysql.easysql.config.Driver";
    private String url = "easysql.config:mysql://localhost:3306/demo";
    private String user = "root";
    private String password = "123456";

    private int initialCount = 1;
    private int incrementalCount = 5;
    private int maxPoolCount = 20;

    private List<Connection> connections = new ArrayList<>();

    //  2016/10/20 构造器用于初始化连接信息
    public PoolImpl(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }


    public Connection getConnection() {
        return null;
    }

    //获得连接池的个数
    public int getPoolCount() {
        return connections.size();
    }

    //获得连接池中自增长的数目
    @Override
    public int getIncrementalCount() {
        return incrementalCount;
    }

    // todo 通过驱动来获得一个连接,返回连接,需要与物理连接驱动所需要的密码驱动类等参数
    private Connection createConnection() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //初始化连接池，将创建初始数量的连接
    private void initPool() {
        // TODO: 2016/10/19 往连接池中添加初始数量的连接
        for (int i = 0; i < initialCount; i++) {
            connections.add(createConnection());
        }
    }

    static class PoolConnection {
        Connection connection;
        boolean isBusy;


        public ResultSet executeQuery(String sql) throws SQLException {
            Statement statement = null;
            try {
                statement = connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return statement.executeQuery(sql);
        }

        public void setBusy(boolean busy) {
            isBusy = busy;
        }

        public boolean isBusy() {
            return isBusy;
        }

        public void setConnection(Connection connection) {
            this.connection = connection;
        }

        public Connection getConnection() {
            return connection;
        }
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
