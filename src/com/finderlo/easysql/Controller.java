package com.finderlo.easysql;

import com.finderlo.easysql.model.ClassModel;
import com.finderlo.easysql.model.ObjectFieldValue;
import com.finderlo.easysql.model.TableModel;
import com.finderlo.easysql.test.Model;
import com.finderlo.easysql.utility.EasyException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.finderlo.easysql.Sentence.jointDelete;
import static com.finderlo.easysql.SqlDao.executeUpdate;

/**
 * Created by Finderlo on 2016/10/23.
 */
public class Controller {

    Cache cache = new Cacheimp();
    ConnectionPool connectionPool = new ConnectionPoolimp();

    public Controller() throws EasyException {
    }

    //作为一个模板类，典型的执行一个操作。参入为对象时，先解析class，然后在拼接语句，最后执行
    int executeDelete(Object object) throws EasyException {
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

    private ClassModel getClassModel(Connection connection, Class classT) throws EasyException {
        ClassModel result = cache.getClassModel(classT);
        if (result == null) {
            cache.cacheClass(connection, classT);
            result = getClassModel(connection, classT);
        }
        return result;
    }

    /**
     * 用作删除表中某一行的数据。
     *
     * @param columnName
     * @param args       在表中的列名字段和参数。建议输入主键和主键值
     * @return 是否删除成功
     **/
    boolean executeDelete(String tableName, String columnName, String args) throws EasyException {
        boolean result = false;
        SpecConnection specConnection = connectionPool.getConnection();
        result = SqlDao.executeUpdate(specConnection.getConnection(), Sentence.jointDelete(tableName, columnName, args)) == 1;
        specConnection.returnConn();
        return result;
    }


    int executeInsert(Object object) throws EasyException {
        SpecConnection specConnection = connectionPool.getConnection();
        ObjectFieldValue objectFieldValue = Util.getObjectFieldValue(object);
        ClassModel classModel = cache.getClassModel(object.getClass());
        String sql = Sentence.jointInsert(classModel.tableModel, objectFieldValue);
        int result = SqlDao.executeUpdate(specConnection.getConnection(), sql);
        specConnection.returnConn();
        return result;
    }

    List executeQuery(Class classT, String column, String arg) throws EasyException {
        SpecConnection specConnection = connectionPool.getConnection();
        ClassModel classModel = cache.getClassModel(classT);
        String sql = Sentence.jointQueryWithArg(classModel.tableModel, column, arg);
        ResultSet resultSet = SqlDao.executeQuery(specConnection.getConnection(), sql);
        List result = SqlDao.parseResult(classModel, resultSet);
        specConnection.returnConn();
        return result;
    }


    List executeQuery(Class classT) throws EasyException {
        SpecConnection specConnection = connectionPool.getConnection();
        ClassModel classModel = cache.getClassModel(classT);
        String sql = Sentence.jointQueryWithoutArg(cache.getTableModel(classModel.tableName));
        ResultSet resultSet = SqlDao.executeQuery(specConnection.getConnection(), sql);
        List result = SqlDao.parseResult(classModel, resultSet);
        specConnection.returnConn();
        return result;
    }

    public void executeQuery(String sql, Parser parser) throws EasyException {
        SpecConnection specConnection = connectionPool.getConnection();
        ResultSet resultSet = SqlDao.executeQuery(specConnection.getConnection(), sql);
        int index = 0;
        try {
            while (resultSet.next()) {
                parser.parse(resultSet, index);
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            specConnection.returnConn();
        }
        specConnection.returnConn();
    }

    public static void main(String[] args) throws EasyException, IOException {
        Controller controller = new Controller();
        Model model = (Model) controller.executeQuery(Model.class, "primary_one", "tom").get(0);
        System.out.println(model.primary_one);
        System.out.println(model.primary_two);
        System.out.println(model.foreign_one);
        System.out.println(model.foreign.nameone);
        System.out.println(model.foreign.test_id);
    }

    public boolean executeUpdate(String name, String Setkey, String Setvalue, String whereKey, String whereValue) {
        String sql = Sentence.jointUpdate(name, Setkey, Setvalue, whereKey, whereValue);
        return false;
    }
}
