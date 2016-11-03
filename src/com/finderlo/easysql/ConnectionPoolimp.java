package com.finderlo.easysql;

import com.finderlo.easysql.utility.EasyException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Finderlo on 2016/10/29.
 */
public class ConnectionPoolimp implements ConnectionPool {
    //连接池中维护的连接
    List<SpecConnection> specConnections = new ArrayList<>();

    //数据库连接到配置文件
    Map<String, String> databaseConfig = null;
    //连接词刚开始的连接数量
    private int initialConnCount = 2;
    //每次增加的连接数量
    private int incrementConnCount = 3;
    //连接池中最大的连接数量
    private int maxConnCount = 30;

    ConnectionPoolimp() {
        try {
            databaseConfig = Util.parseProperties();
        } catch (EasyException e) {
            e.printStackTrace();
            throw new RuntimeException("数据库连接配置信息错误");
        }
        initConnPool();
    }

    ConnectionPoolimp(Map<String, String> databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    private void initConnPool() {
        //初始化时，初始化连接池中连接的数量
        createSpecConn(initialConnCount);
    }

    /**
     * 获取连接池中所有的连接
     * */
    @Override
    public int getPoolConnCount() {
        return specConnections.size();
    }

    /**
     * 获得一个连接，如果连接池中所有连接全部忙碌状态，则尝试新建连接。
     * 如果连接达到最大数，则等待，一直到有空闲的连接为止
     * */
    @Override
    public SpecConnection getConnection() {

        for (SpecConnection specConnection : specConnections) {
            if (!specConnection.isBusy()) {
                specConnection.setBusy(true);
                return specConnection;
            }
        }
        //如果连接池中全部为忙，，，，
        //如果连接池中的数量没有到达最大值，则新增连接
        if (specConnections.size() < maxConnCount) {
            createSpecConn(incrementConnCount);
        } else {
            //如果达到最大值，则进行递归，递归到有空闲的连接
        }

        return getConnection();
    }

    /**
     * 关闭连接池，并将连接池中的所有连接全部关闭
     * */
    @Override
    public boolean closePool() {
        specConnections.forEach(e -> e.closeConn());
        return true;
    }

    /**
     * 连接使用完成后返回，将连接的busy状态设置为false
     */
    @Override
    public void returnConnection(SpecConnection specConnection) {
        specConnection.setBusy(false);
    }

    /**
     * 通过配置文件创建连接,并且将连接放入连接池中
     *
     * @param ConnCount 一次性创建连接的数量
     */
    private void createSpecConn(int ConnCount) {
        for (int i = 0; i < ConnCount; i++) {
            createSpecConn();
        }
    }

    /**
     * 通过配置文件创建一个连接,并且将连接放入连接池中
     */
    private void createSpecConn() {

        String driver = databaseConfig.get("driver");
        String url = databaseConfig.get("url");
        String user = databaseConfig.get("user");
        String password = databaseConfig.get("password");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("配置文件中的driver（数据库驱动程序类）未找到");
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("通过配置文件获得连接失败，请确定url、user、password是否正确。" +
                    "['url':" + url + ";'user':" + user + ";'password':" + password + "]");
        }

        SpecConnection specConnection = new SpecConnection(connection);
        specConnections.add(specConnection);
    }

    public static void main(String[] args) throws EasyException, SQLException {
        ConnectionPool connectionPool = new ConnectionPoolimp();
        System.out.println(connectionPool.getPoolConnCount());
        SpecConnection connection = connectionPool.getConnection();
        connection.getConnection().createStatement().executeQuery("select * form ");
    }
}
