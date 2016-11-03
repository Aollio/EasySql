package com.finderlo.easysql;

import com.finderlo.easysql.model.ClassModel;
import com.finderlo.easysql.model.ObjectFieldValue;
import com.finderlo.easysql.model.TableModel;
import com.finderlo.easysql.utility.EasyException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 控制器将eastsql中的请求转移来执行。
 * 内部维护缓存和连接词
 */
public class EasySql {

    static Cache cache = Cacheimp.getInstance();
    static ConnectionPool connectionPool = new ConnectionPoolimp();
    static EasySql singleton = new EasySql();

    private EasySql() {
    }

    public static EasySql getInstance() {
        return singleton;
    }

    //作为一个模板类，典型的执行一个操作。参入为对象时，先解析class，然后在拼接语句，最后执行
    static int delete(Object object) throws EasyException {
        SpecConnection specConnection = connectionPool.getConnection();
        //执行顺序应该是先获取对象中具体值，同时获取这个对象的类所属的表名，然后将这两个值传递给jointsql用来获取sql值，最后执行
        ObjectFieldValue objectFieldValue = Util.getObjectFieldValue(object);

        ClassModel classModel = getClassModel(specConnection.getConnection(), object.getClass());
        TableModel tableModel = classModel.tableModel;

        String sql = Sentence.jointDelete(tableModel, objectFieldValue);
        int result = SqlDao.executeUpdate(Util.getConnection(), sql);
        specConnection.returnConn();
        return result;
    }

    /**
     * 用作删除表中某一行的数据。
     *
     * @param columnName
     * @param args       在表中的列名字段和参数。建议输入主键和主键值
     * @return 是否删除成功
     **/
    static int delete(String tableName, String columnName, String args) throws EasyException {
        int result = 0;
        SpecConnection specConnection = connectionPool.getConnection();
        result = SqlDao.executeUpdate(specConnection.getConnection(),
                Sentence.jointDelete(tableName, columnName, args));
        specConnection.returnConn();
        return result;
    }

    static int insert(Object object) throws EasyException {
        SpecConnection specConnection = connectionPool.getConnection();
        ObjectFieldValue objectFieldValue = Util.getObjectFieldValue(object);
        ClassModel classModel = cache.getClassModel(object.getClass());
        String sql = Sentence.jointInsert(classModel.tableModel, objectFieldValue);
        int result = SqlDao.executeUpdate(specConnection.getConnection(), sql);
        specConnection.returnConn();
        return result;
    }

    static List query(Class classT, String column, String columnValue, boolean isJoinQuery) throws EasyException {
        SpecConnection specConnection = connectionPool.getConnection();
        ClassModel classModel = cache.getClassModel(classT);
        String sql = Sentence.jointQueryWithArg(classModel.tableModel, column, columnValue);
        ResultSet resultSet = SqlDao.executeQuery(specConnection.getConnection(), sql);
        List result = SqlDao.parseResult(classModel, resultSet, isJoinQuery);
        specConnection.returnConn();
        return result;
    }

    static List query(Class classT) throws EasyException {
        SpecConnection specConnection = connectionPool.getConnection();
        ClassModel classModel = cache.getClassModel(classT);
        String sql = Sentence.jointQueryWithoutArg(cache.getTableModel(classModel.tableName));
        ResultSet resultSet = SqlDao.executeQuery(specConnection.getConnection(), sql);
        List result = SqlDao.parseResult(classModel, resultSet, false);
        specConnection.returnConn();
        return result;
    }

    static public <T> List<T> query(String sql, Parser<T> parser) {
        SpecConnection specConnection = connectionPool.getConnection();
        ResultSet resultSet = SqlDao.executeQuery(specConnection.getConnection(), sql);
        List<T> result = new ArrayList();
        int index = 0;
        try {
            while (resultSet.next()) {
                result.add(parser.parse(resultSet, index));
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            specConnection.returnConn();
        }
        return result;
    }

    static public int update(String sql) {
        SpecConnection specConnection = connectionPool.getConnection();
        int count = SqlDao.executeUpdate(specConnection.getConnection(), sql);
        specConnection.returnConn();
        return count;
    }

//    public boolean executeUpdate(String TableName, String Setkey, String Setvalue, String whereKey, String whereValue) {
//        String sql = Sentence.jointUpdate(TableName, Setkey, Setvalue, whereKey, whereValue);
//        return false;
//    }

    static class UpdateBuilder {
        Class classT;
        String tableName;

        Map<String, String> setMap = new HashMap<>();
        Map<String, String> whereMap = new HashMap<>();

        String currentSet;
        String currentWhere;

        UpdateBuilder builder = this;

        UpdateBuilder on(String tableName) {
            this.tableName = tableName;
            return builder;
        }

        UpdateBuilder on(Class classT) {
            this.classT = classT;
            return builder;
        }

        UpdateBuilder setKey(String key) {
            setMap.put(key, null);
            currentSet = key;
            return builder;
        }

        UpdateBuilder setValue(String value) {
            setMap.put(currentSet, value);
            return builder;
        }

        UpdateBuilder whereKey(String key) {
            whereMap.put(key, null);
            currentWhere = key;
            return builder;
        }

        UpdateBuilder whereValue(String value) {
            whereMap.put(currentWhere, value);
            return builder;
        }

        int build() {
            if (classT == null && tableName == null) {
                throw new RuntimeException("UpdateBuiler没有设置表名或者实体类");
            }
            setMap.entrySet().stream().forEach(entry -> {
                if (entry.getValue() == null)
                    throw new RuntimeException("设置的key-value含有空的值");
            });
            whereMap.entrySet().stream().forEach(entry -> {
                if (entry.getValue() == null)
                    throw new RuntimeException("设置的key-value含有空的值");
            });

            if (classT != null) {
                tableName = cache.getClassModel(classT).tableName;
            }

            return update(Sentence.jointUpdate(tableName, setMap, whereMap));
        }


    }

    private static ClassModel getClassModel(Connection connection, Class classT) throws EasyException {
        ClassModel result = cache.getClassModel(classT);
        if (result == null) {
            cache.cacheClass(connection, classT);
            result = getClassModel(connection, classT);
        }
        return result;
    }


}
