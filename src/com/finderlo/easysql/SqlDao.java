package com.finderlo.easysql;

import com.finderlo.easysql.model.ClassModel;
import com.finderlo.easysql.utility.EasyException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Finderlo on 2016/10/23.
 */
public class SqlDao {


    static int executeUpdate(Connection connection, String sql) {
        int count = 0;
        try {
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            Statement statement = connection.createStatement();
            count = statement.executeUpdate(sql);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            count = 0;
        }
        return count;
    }

    static ResultSet executeQuery(Connection connection, String sql) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    static List parseResult(ClassModel classModel, ResultSet resultSet,boolean isJoinQuery) throws EasyException {
        List result = new ArrayList();
        try {
            //遍历数据库中的每一行
            while (resultSet.next()) {

                Object object = classModel.classType.newInstance();
                //遍历表中字段中的每一个属性，并将值赋予这个对象
                for (String fieldName : classModel.tableModel.columnsKey) {
                    Field field = classModel.getField(fieldName);
                    field.setAccessible(true);

                    //前四个判断用于基本属性，
                    if (field.getType().getSimpleName().equals("int")) {
                        field.setInt(object, resultSet.getInt(field.getName()));
                    } else if (field.getType().getSimpleName().equals("boolean")) {
                        field.setBoolean(object, resultSet.getBoolean(field.getName()));
                    } else if (field.getType().getSimpleName().equals("float")) {
                        field.setFloat(object, resultSet.getFloat(field.getName()));
                    } else if (field.getType().getSimpleName().equals("String")) {
                        field.set(object, resultSet.getString(field.getName()));
                    } else {
                        field.set(object, resultSet.getString(field.getName()));
                    }
                }
                //如果需要连接查询，则遍历这个表的所有外键，进行递归查询
                if (isJoinQuery) {
                    for (ClassModel.ForeignKeyModel foreignKeyModel : classModel.foreignKeys) {
                        //外键字段的值
                        Object foreignValue = foreignKeyModel.field.get(object);
                        //外键对应的表
                        String tableName = foreignKeyModel.tableName;
                        //外键对应的实体类对象,这个方法用于查询指定表的值,最后一个字段，用来表示是否是继续关联查找
                        List list  = EasySql.query(foreignKeyModel.foreignField.getType(),
                                foreignKeyModel.columnName, foreignValue.toString(), true);
                        Object foreignObject = null;
                        if (!list.isEmpty()){
                            foreignObject = list.get(0);
                        }
                        //将主要对象中的外键对应属性设置
                        foreignKeyModel.foreignField.set(object, foreignObject);
                    }
                }
                result.add(object);


            }

        } catch (IllegalAccessException e) {
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            throw new EasyException(classModel.classType.getName()
                    + " : The default constructor is private . Or The class isn't have default constructor");
        }

        return result;
    }

}
