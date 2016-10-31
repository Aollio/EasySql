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

    ConnectionPoolimp() throws EasyException {
        databaseConfig = Util.parseProperties();
        initConnPool();
    }
    ConnectionPoolimp(Map<String,String> databaseConfig){
        this.databaseConfig =  databaseConfig;
    }

    private void initConnPool() throws EasyException {
        //初始化时，初始化连接池中连接的数量
        createSpecConn(initialConnCount);
    }
    @Override
    public int getPoolConnCount(){
        return specConnections.size();
    }

    @Override
    public SpecConnection getConnection() throws EasyException {

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

    @Override
    public boolean closePool() {
        specConnections.forEach(e -> e.closeConn());
        return true;
    }

    @Override
    public void returnConnection(SpecConnection specConnection) {
        specConnection.setBusy(false);
    }

    private void createSpecConn(int ConnCount) throws EasyException {
        for (int i = 0; i < ConnCount; i++) {
            createSpecConn();
        }
    }

    private void createSpecConn() throws EasyException {

        String driver = databaseConfig.get("driver");
        String url = databaseConfig.get("url");
        String user = databaseConfig.get("user");
        String password = databaseConfig.get("password");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new EasyException("配置文件中的driver（数据库驱动程序类）未找到");
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EasyException("通过配置文件获得连接失败，请确定url、user、password是否正确。" +
                    "['url':" + url + ";'user':" + user + ";'password':" + password + "]");
        }

        SpecConnection specConnection = new SpecConnection(connection);
        specConnections.add(specConnection);
    }

    public static void main(String[] args) throws EasyException, SQLException {
        ConnectionPool connectionPool= new ConnectionPoolimp();
        System.out.println(connectionPool.getPoolConnCount());
        SpecConnection connection = connectionPool.getConnection();
        connection.getConnection().createStatement().executeQuery("select * form ");
    }
}
