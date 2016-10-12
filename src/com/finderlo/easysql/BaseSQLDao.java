package com.finderlo.easysql;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Finderlo on 2016/10/10.
 */
public abstract class BaseSQLDao {

    public static final String INT = "int";
    public static final String STRING = "String";
    public static final String BOOLEAN = "boolean";
    public static final String FLOAT = "float";


    //插入数据库的连接
    protected Connection mConnection ;

    protected ClassType mClassType;
    protected Table mTable;

    protected ObjectFieldValue mObjectFieldValue;

    BaseSQLDao(Connection connection, String tableName) {
        this.mConnection = connection;
        initConnection();
        // TODO: 2016/10/12 从缓存中获取表的信息
        Cache.getInstance().cacheTable(connection,tableName);
        mTable = Cache.getInstance().tableMap.get(tableName.toLowerCase());
    }


    protected  void initConnection(){
        try {
            mConnection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void initClass(Class classT){
        //// TODO: 2016/10/12 从缓存中获得类的信息
        mClassType = Cache.getInstance().classTypeMap.get(classT.getSimpleName().toLowerCase());
    }

    protected void initObject(Object object) throws EasyException {
        initClass(object.getClass());
        mObjectFieldValue = Util.getObjectFieldValue(object);

    }

     int executeUpdate(String sql){
        int count = 0;
        try {
            if (mConnection.getAutoCommit()){
                mConnection.setAutoCommit(false);
            }
            Statement statement = mConnection.createStatement();
            count = statement.executeUpdate(sql);
            mConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                mConnection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            count = 0;
        }
        return count;
    }

    ResultSet executeQuery(String sql){
        ResultSet resultSet = null;
        try {
            if (mConnection.getAutoCommit()){
                mConnection.setAutoCommit(false);
            }
            Statement statement = mConnection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }



}
