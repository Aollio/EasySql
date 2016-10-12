package com.finderlo.easysql;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Finderlo on 2016/10/11.
 */
public class Util {

    //解析表中所有列名，在此设置均为小写
    static Table parseTable(Connection connection, String tableName) {
        return new Table(tableName,parse(connection,tableName,"primary"), parse(connection,tableName," "));
    }

    static ClassType parseClassType(Class classT){
        return new ClassType(classT,getClassFields(classT));
    }

    static ObjectFieldValue getObjectFieldValue(Object object) throws EasyException {
        Map<Field,ObjectFieldValue.ValueInfo> result = new HashMap<>();

        for (Field field:object.getClass().getDeclaredFields()){
            field.setAccessible(true);
            Class fieldType = field.getType();

            try {
                //判断属性的类型,放入map中
                if (fieldType.getSimpleName().equals("String")) {
                    result.put(field, new ObjectFieldValue.ValueInfo(String.class, field.get(object)));
                } else if (fieldType.getSimpleName().equals("int")) {
                    result.put(field, new ObjectFieldValue.ValueInfo(int.class, field.getInt(object)));
                } else if (fieldType.getSimpleName().equals("boolean")) {
                    result.put(field, new ObjectFieldValue.ValueInfo(boolean.class, field.getBoolean(object)));
                } else if (fieldType.getSimpleName().equals("float")) {
                    result.put(field, new ObjectFieldValue.ValueInfo(float.class, field.getFloat(object)));
                } else {
                    result.put(field, new ObjectFieldValue.ValueInfo(fieldType, field.get(object)));
                }
            }catch (IllegalAccessException e){
                throw new EasyException("输入的object实体类（model）和class不匹配");
            }
        }
        return new ObjectFieldValue(result);
    }

    private static List<String> parse(Connection connection, String tableName, String type) {
        List<String> result = new ArrayList<>();
        try {
            ResultSet resultSet = null;
            if (type.equals("primary")) {
                resultSet = connection.getMetaData().getPrimaryKeys(null, null, tableName);
            } else {
                resultSet = connection.getMetaData().getColumns(null, null, tableName, null);
            }
            while (resultSet.next()) {
                result.add(resultSet.getString("COLUMN_NAME").toLowerCase());//INDEX:4 ,name
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Map<String,Field> getClassFields(Class classT){
        Map<String,Field> results = new HashMap<>();
        for (Field field:classT.getDeclaredFields()){
            field.setAccessible(true);
            results.put(field.getName().toLowerCase(),field);
        }
        return results;
    }
    /**
     *在指定的字符串索引位置替换字符
     **/
    static String replace(int index, String newString, String oriString) {
        return oriString.substring(0, index) + newString + oriString.substring(index + 1);
    }
}
