package com.finderlo.easysql;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import static com.finderlo.easysql.Util.*;

/**
 * Created by Finderlo on 2016/10/12.
 */
public class Cache {

    private static Cache instance;

    //表中和表中列名和主键的对应map,列名和主键从数据库中获得。
    //表名列名均为小写
    Map<String, Table> tableMap = new HashMap<>();
    //获取类中的所有属性的对应map，通过反射获得
    //类中均为小写
    Map<String, ClassType> classTypeMap = new HashMap<>();

    private Cache() {
    }

    void cacheTable(Connection connection, String tableName) {
        if (tableMap.get(tableName.toLowerCase()) != null) {
            return;
        }
        tableMap.put(tableName.toLowerCase(), parseTable(connection, tableName));
    }

    void cacheClass(Class classT) {
        if (classTypeMap.get(classT.getSimpleName().toLowerCase()) != null) {
            return;
        }
        classTypeMap.put(classT.getSimpleName().toLowerCase(), parseClassType(classT));
    }

    static Cache getInstance() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    static void cache(Connection connection,String tableName,Class classT){
        getInstance().cacheTable(connection,tableName);
        getInstance().cacheClass(classT);
    }

    static void cache(Class classT){
        getInstance().cacheClass(classT);
    }
    static void cache(Connection connection,String tableName){
        getInstance().cacheTable(connection,tableName);
    }
}
