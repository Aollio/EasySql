package com.finderlo.easysql;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Finderlo on 2016/10/10.
 */
public class Query extends BaseSQLDao {

    Query(Connection connection, String tableName) {
        super(connection, tableName);
    }

    /**
     * query from @tableName,requirement columnName and argument
     *
     * @param columnName
     * @param arg        the sql  : query from tableName where columnName = arg;
     * @param classT     the model class
     * @return the list of model.
     */
    List query(String columnName, String arg, Class classT) throws EasyException {
        initClass(classT);
        return parseResult(executeQuery(jointSql(columnName, arg)));
    }

    List query(Class classT) throws EasyException {
        initClass(classT);
        return parseResult(executeQuery(jointSql()));
    }

    List queryFuzzy() {
        return null;
    }

    private List parseResult(ResultSet resultSet) throws EasyException {
        List result = new ArrayList();
        try {
            if (resultSet.wasNull()) {
                System.out.println("result is null");
                return result;
            }

            //遍历数据库中的每一行
            while (resultSet.next()) {
                Object object = mClassType.classType.newInstance();
                //遍历类中的每一个属性，并将值赋予这个对象
                for (Field field : mClassType.classType.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (!mClassType.isFieldExist(field.getName())){
                        //// TODO: 2016/10/12 如果数据库中不存在这个属性，则返回
                        continue;
                    }
                    if (field.getType().getSimpleName().equals(INT)) {
                        field.setInt(object, resultSet.getInt(field.getName()));
                    } else if (field.getType().getSimpleName().equals(BOOLEAN)) {
                        field.setBoolean(object, resultSet.getBoolean(field.getName()));
                    } else if (field.getType().getSimpleName().equals(FLOAT)) {
                        field.setFloat(object, resultSet.getFloat(field.getName()));
                    } else if (field.getType().getSimpleName().equals(STRING)) {
                        field.set(object, resultSet.getString(field.getName()));
                    } else {
                        field.set(object, resultSet.getString(field.getName()));
                    }
                }
                result.add(object);
            }

        } catch (IllegalAccessException e) {
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new EasyException(mClassType.classType.getName()
                    + " : The default constructor is private . Or The class isn't have zero argument constructor");
        }

        return result;
    }

    private String jointSql(String columnName, String arg) {
        return "select * from " + mTable.tableName + " where " + columnName + " = '" + arg + "'";
    }

    private String jointSql() {
        return "select * from " + mTable.tableName;
    }

}
