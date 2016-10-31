package com.finderlo.easysql;

import com.finderlo.easysql.utility.EasyException;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.finderlo.easysql.Cache.*;
import static com.finderlo.easysql.Util.*;

/**
 * Created by Finderlo on 2016/10/10.
 * <p>
 * 使用方法：
 * com.finderlo.easysql.EasySql easySql = new com.finderlo.easysql.EasySql(Connection,TableName); easySql.insert(Object);
 * com.finderlo.easysql.EasySql.insert(Connection,TableName,Object)
 */
public class EasySql {

    static Controller controller;

    static {
        try {
            controller = new Controller();
        } catch (EasyException e) {
            e.printStackTrace();
        }
    }


    private EasySql() {
    }

    public static void executeQuery(String sql,Parser parser) throws EasyException {
        controller.executeQuery(sql,parser);
    }

    public static int insert(Object object) throws EasyException {
        return controller.executeInsert(object);
    }

    public static int delete(Object object) throws EasyException {
        return controller.executeDelete(object);
    }

//    public static boolean delete(String tablename, String columName, String args) throws EasyException {
//
//    }

    public static List query(Class classT, String columName, String args) throws EasyException {
        return controller.executeQuery(classT, columName, args);
    }

    public static List query(Class classT) throws EasyException {
        return controller.executeQuery(classT);
    }

    public static boolean update( String tableName, String setKey, String setValue, String whereKey, String whereValue) {
        return controller.executeUpdate(tableName,setKey,setValue,whereKey,whereValue);
    }

}
