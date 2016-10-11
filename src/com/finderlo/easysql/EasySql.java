package com.finderlo.easysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Finderlo on 2016/10/10.
 *
 * 使用方法：
 * com.finderlo.easysql.EasySql.open(Connection).on(TableName).insert(Object);
 * com.finderlo.easysql.EasySql easySql = new com.finderlo.easysql.EasySql(Connection,TableName); easySql.insert(Object);
 * com.finderlo.easysql.EasySql.insert(Connection,TableName,Object)
 */
public class EasySql {

    public EasySql() {}
    public EasySql(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    public static boolean insert(Connection connection, String tablename, Object object) throws EasyException {
        return new Insert(connection, tablename).insert(object);
    }

    public static boolean delete(Connection connection, String tablename, Object object) throws EasyException {
        return new Delete(connection, tablename).delete(object, object.getClass());
    }

    public static boolean delete(Connection connection, String tablename, String columName, String args) throws EasyException {
        return new Delete(connection, tablename).delete(columName, args);
    }

    public static List query(Connection connection, String tablename, Class classT, String columName, String args) throws EasyException {
        return new Query(connection, tablename).query(columName, args, classT);
    }

    public static List query(Connection connection, String tablename, Class classT) throws EasyException {
        return new Query(connection, tablename).query(classT);
    }

    public static boolean update(Connection connection, String tableName, String setKey, String setValue, String whereKey, String whereValue) {
        return new Update(connection, tableName).update(setKey, setValue, whereKey, whereValue);
    }

    public boolean insert(Object object) throws EasyException {
        checkIsEnable();
        return new Insert(connection, tableName).insert(object);
    }

    public boolean delete(Object object) throws EasyException {
        checkIsEnable();
        return new Delete(connection,tableName).delete(object,object.getClass());
    }

    public boolean delete(String columName, String args) {
        return new Delete(connection, tableName).delete(columName, args);
    }

    public List query(Class classT) throws EasyException {
        return new Query(connection,tableName).query(classT);
    }

    public  List query(Class classT, String columName, String args) throws EasyException {
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
