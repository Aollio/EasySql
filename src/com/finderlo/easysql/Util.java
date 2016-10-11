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

    static Table parseTable(Connection connection, String tableName) {
        return new Table(parse(connection,tableName,"primary"), parse(connection,tableName," "));
    }

    static List<Field> getFields(Class classT){
        List<Field> mFields =new ArrayList<>();
        for(Field field:classT.getDeclaredFields()){
            mFields.add(field);
            field.setAccessible(true);
        }
        return mFields;
    }

    static Map<Field, Typer> getFieldsAndValues(Object object,Class classT) throws EasyException {
        List<Field> mFields = getFields(classT);
        Map<Field,Typer> mNameAndTyper = new HashMap<>();
        for (Field field:mFields){
            field.setAccessible(true);
            Class fieldType = field.getType();

            try {
                //判断属性的类型,放入map中
                if (fieldType.getSimpleName().equals("String")) {
                    mNameAndTyper.put(field, new Typer(String.class, field.get(object)));
                } else if (fieldType.getSimpleName().equals("int")) {
                    mNameAndTyper.put(field, new Typer(int.class, field.getInt(object)));
                } else if (fieldType.getSimpleName().equals("boolean")) {
                    mNameAndTyper.put(field, new Typer(boolean.class, field.getBoolean(object)));
                } else if (fieldType.getSimpleName().equals("float")) {
                    mNameAndTyper.put(field, new Typer(float.class, field.getFloat(object)));
                } else {
                    mNameAndTyper.put(field, new Typer(fieldType, field.get(object)));
                }
            }catch (IllegalAccessException e){
                throw new EasyException("输入的object实体类（model）和class不匹配");
            }
        }
        return mNameAndTyper;
    }

    static Map<String,Typer> getFieldsNameAndValues(Object object,Class classT) throws EasyException {
        Map<Field,Typer> mNameAndTyper = getFieldsAndValues(object, classT);
        Map<String,Typer> mFieldsNameAndValues = new HashMap<>();
        mNameAndTyper.keySet().forEach(field -> {mFieldsNameAndValues.put(field.getName(),mNameAndTyper.get(field));});
        return mFieldsNameAndValues;
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
                result.add(resultSet.getString("COLUMN_NAME"));//INDEX:4 ,name
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *在指定的字符串索引位置替换字符
     **/
    static String replace(int index, String newString, String oriString) {
        return oriString.substring(0, index) + newString + oriString.substring(index + 1);
    }
}
