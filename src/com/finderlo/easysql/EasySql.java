package com.finderlo.easysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.finderlo.easysql.Cache.*;

/**
 * Created by Finderlo on 2016/10/10.
 *
 * 使用方法：
 * com.finderlo.easysql.EasySql easySql = new com.finderlo.easysql.EasySql(Connection,TableName); easySql.insert(Object);
 * com.finderlo.easysql.EasySql.insert(Connection,TableName,Object)
 */
public class EasySql {

    public EasySql() {}
    public EasySql(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
        cache(connection,tableName);
    }

    public static int insert(Connection connection, String tablename, Object object) throws EasyException, SQLException {
        cache(connection,tablename);
        return new Insert(connection, tablename).insert(object);
    }

    public static int delete(Connection connection, String tablename, Object object) throws EasyException {
        cache(connection,tablename);
        return new Delete(connection, tablename).delete(object);
    }

    public static boolean delete(Connection connection, String tablename, String columName, String args) throws EasyException {
        cache(connection,tablename);
        return new Delete(connection, tablename).delete(columName, args);
    }

    public static List query(Connection connection, String tablename, Class classT, String columName, String args) throws EasyException {
        cache(connection,tablename,classT);
        return new Query(connection, tablename).query(columName, args, classT);
    }

    public static List query(Connection connection, String tablename, Class classT) throws EasyException {
        cache(connection,tablename,classT);
        return new Query(connection, tablename).query(classT);
    }

    public static boolean update(Connection connection, String tableName, String setKey, String setValue, String whereKey, String whereValue) {
        cache(connection,tableName);
        return new Update(connection, tableName).update(setKey, setValue, whereKey, whereValue);
    }

    public int insert(Object object) throws EasyException, SQLException {
        checkIsEnable();
        cache(object.getClass());
        return new Insert(connection, tableName).insert(object);
    }

    public int delete(Object object) throws EasyException {
        checkIsEnable();
        cache(object.getClass());
        return new Delete(connection,tableName).delete(object);
    }

    public boolean delete(String columName, String args) {
        return new Delete(connection, tableName).delete(columName, args);
    }

    public List query(Class classT) throws EasyException {
        cache(classT);
        return new Query(connection,tableName).query(classT);
    }

    public  List query(Class classT, String columName, String args) throws EasyException {
        cache(classT);
        return new Query(connection, tableName).query(columName, args, classT);
    }

    public  boolean update( String setKey, String setValue, String whereKey, String whereValue) {
        return new Update(connection, tableName).update(setKey, setValue, whereKey, whereValue);
    }

    void checkIsEnable() throws EasyException {
        try {
            if (connection == null || connection.isClosed() || tableName.equals("")) {
                throw new EasyException("Connection is not initialize Or is closd ; And maybe not set tableNmaee");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EasyException("Connection is not initialize Or is closd  Or is wrong connection");
        }
    }



    static EasySql singleton;
    Connection connection = null;
    String tableName = "";

    static EasySql open(Connection conn) {
        if (singleton == null) {
            singleton = new EasySql();
        }
        singleton.setConnection(conn);
        return singleton;
    }

    static EasySql on(String tablename) {
        if (singleton == null) {
            singleton = new EasySql();
        }
        singleton.setTableName(tablename);
        return singleton;
    }


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
