package com.aollio.easysql;

import com.aollio.easysql.annotation.ForeignKey;
import com.aollio.easysql.model.ClassModel;
import com.aollio.easysql.annotation.PrimaryKey;
import com.aollio.easysql.annotation.Table;
import com.aollio.easysql.model.ObjectFieldValue;
import com.aollio.easysql.model.TableModel;
import com.aollio.easysql.utility.EasyException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * Created by Finderlo on 2016/10/11.
 */
public final class Util {

    public static Map<String, String> parseProperties() throws EasyException {
        //通过配置文件来获取连接信息;通过PROPERTIE文件信息获取
        Properties properties = new Properties();
        //这是默认项目下的easysql.config文件
        try {
            properties.load(new FileReader("easysql.config"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new EasyException("读取配置文件错误，请确保项目根目录下是否存在[easysql.config]文件，或者文件中格式不对");
        }
        HashMap<String, String> result = new HashMap<>();
        String url = properties.getProperty("url");
        String driver = properties.getProperty("driver");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        result.put("url", url);
        result.put("driver", driver);
        result.put("user", user);
        result.put("password", password);
        return result;
    }

    static Map<String, String> parseProperties(File file) throws IOException {
        //通过配置文件来获取连接信息;通过PROPERTIE文件信息获取
        Properties properties = new Properties();
        properties.load(new FileReader(file));

        HashMap<String, String> result = new HashMap<>();
        String url = properties.getProperty("url");
        String driver = properties.getProperty("driver");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        result.put("url", url);
        result.put("driver", driver);
        result.put("user", user);
        result.put("password", password);
        return result;
    }

    //在解析时将所有字段存为小写
    static String parseClassAnootation(Class classT) throws EasyException {

        Table annotation = (Table) classT.getAnnotation(TableModel.class);
        if (annotation == null) {
            throw new EasyException("实体类没有设置属性注解");
        }
        System.out.println(annotation.value());
        return annotation.value();
    }

    /**
     * 创建解析的class信息，用作缓存。创建一个classType对象。
     */
    static ClassModel parseClassType(Class classT) throws EasyException {
        ClassModel result = new ClassModel();
        result.className = classT.getSimpleName();
        result.classType = classT;

        Map<String, Field> fieldMap = new HashMap<>();//解析的fields映射

        //判断注解是否存在
        if (!classT.isAnnotationPresent(Table.class))
            throw new EasyException("实体类对应的表名没有注入");

        //解析实体类对应的表名的注解信息
        result.tableName = ((Table) classT.getAnnotation(Table.class)).value().toLowerCase();

        //解析主键和外键
        for (Field field : classT.getDeclaredFields()) {

            field.setAccessible(true);
            fieldMap.put(field.getName().toLowerCase(), field);

            //扫描主键
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                result.primaryKeys.add(((PrimaryKey) (field.getAnnotation(PrimaryKey.class))).value());
            }
            //扫描外键
            if (field.isAnnotationPresent(ForeignKey.class)) {
                ForeignKey foreignKey = (ForeignKey) (field.getAnnotation(ForeignKey.class));
                ClassModel.ForeignKeyModel foreignKeyModel = new ClassModel.ForeignKeyModel();
                foreignKeyModel.fieldName = field.getName();
                foreignKeyModel.columnName = foreignKey.coloum();
                foreignKeyModel.tableName = foreignKey.table();
                Field foreignField = null;
                try {
                    foreignField = classT.getDeclaredField(foreignKey.modelKey());
                } catch (NoSuchFieldException e) {
                    throw new EasyException("没有找到外键对应的实体类属性");
                }
                foreignKeyModel.foreignField = foreignField;
                foreignKeyModel.classModel = parseClassType(foreignField.getType());
                foreignKeyModel.field = field;
                result.foreignKeys.add(foreignKeyModel);
            }
        }
        if (result.primaryKeys.size() == 0)
            throw new EasyException("实体类对应的主键没有注入");
        result.allFieldsMap = fieldMap;
        return result;
    }

    private static Map<String, Field> getClassFields(Class classT) {
        Map<String, Field> results = new HashMap<>();
        for (Field field : classT.getDeclaredFields()) {
            field.setAccessible(true);
            results.put(field.getName().toLowerCase(), field);
        }
        return results;
    }

    static ObjectFieldValue getObjectFieldValue(Object object) throws EasyException {
        Map<Field, ObjectFieldValue.ValueInfo> result = new HashMap<>();

        for (Field field : object.getClass().getDeclaredFields()) {
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
            } catch (IllegalAccessException e) {
                throw new EasyException("输入的object实体类（model）和class不匹配");
            }
        }
        return new ObjectFieldValue(result);
    }

    //解析表中所有列名，在此设置均为小写
    static TableModel parseTable(Connection connection, String tableName) {
        return new TableModel(tableName.toLowerCase(), parse(connection, tableName, "primary"), parse(connection, tableName, " "));
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

    static List<TableModel> parseConnection(Connection connection) {
        List<TableModel> result = new ArrayList<>();
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet rs = meta.getTables(null, null, null,
                    new String[]{"TABLE"});
            while (rs.next()) {
                System.out.println("表名：" + rs.getString(3));
                result.add(parseTable(connection, rs.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 在指定的字符串索引位置替换字符
     **/
    static String replace(int index, String newString, String oriString) {
        return oriString.substring(0, index) + newString + oriString.substring(index + 1);
    }

    public static Connection getConnection() throws EasyException {
        Map<String, String> connConfig = null;
        connConfig = parseProperties();

        String driver = connConfig.get("driver");
        String url = connConfig.get("url");
        String user = connConfig.get("user");
        String password = connConfig.get("password");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new EasyException("配置文件中的driver（数据库驱动程序类）未找到");
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EasyException("通过配置文件获得连接失败，请确定url、user、password是否正确。" +
                    "['url':" + url + ";'user':" + user + ";'password':" + password + "]");
        }
        return connection;
    }


}
