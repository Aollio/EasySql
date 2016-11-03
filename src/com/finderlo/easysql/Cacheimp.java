package com.finderlo.easysql;

import com.finderlo.easysql.annotation.ForeignKey;
import com.finderlo.easysql.annotation.PrimaryKey;
import com.finderlo.easysql.annotation.Table;
import com.finderlo.easysql.model.ClassModel;
import com.finderlo.easysql.model.TableModel;
import com.finderlo.easysql.utility.EasyException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import static com.finderlo.easysql.Util.parseClassType;
import static com.finderlo.easysql.Util.parseConnection;
import static com.finderlo.easysql.Util.parseTable;

/**
 * Created by Finderlo on 2016/10/29.
 */
public class Cacheimp implements Cache {


    //表中和表中列名和主键的对应map,列名和主键从数据库中获得。
    //表名列名均为小写
    Map<String, TableModel> tableMap = new HashMap<>();
    //获取类中的所有属性的对应map，通过反射获得
    //类中均为小写
    Map<String, ClassModel> classTypeMap = new HashMap<>();

    private static Cacheimp singleton = new Cacheimp();

    private Cacheimp() {
    }

    public static Cacheimp getInstance(){
        return singleton;
    }

    @Override
    public ClassModel getClassModel(Class classT) throws EasyException {
        for (String className : classTypeMap.keySet()) {
            if (classT.getSimpleName().toLowerCase().equals(className)) {
                return classTypeMap.get(className);
            }
        }
        return null;
    }

    @Override
    public TableModel getTableModel(String table) throws EasyException {
        for (String tableName : tableMap.keySet()) {
            if (table.toLowerCase().equals(tableName)) {
                return tableMap.get(tableName);
            }
        }
        return null;
    }

    @Override
    public void cacheTable(Connection connection, String tableName) {
        if (tableMap.get(tableName.toLowerCase()) != null) {
            return;
        }
        tableMap.put(tableName.toLowerCase(), parseTable(connection, tableName));
    }

    @Override
    public void cacheClass(Connection connection, Class classT) throws EasyException {
        if (classTypeMap.get(classT.getSimpleName().toLowerCase()) != null) {
            return;
        }
        ClassModel classModel = parseClassType(connection, classT);
        classTypeMap.put(classT.getSimpleName().toLowerCase(), classModel);
    }

    /**
     * 创建解析的class信息，用作缓存。创建一个classType对象。
     */
    ClassModel parseClassType(Connection connection, Class classT) throws EasyException {
        ClassModel result = new ClassModel();
        result.className = classT.getSimpleName();
        result.classType = classT;

        Map<String, Field> fieldMap = new HashMap<>();//解析的fields映射

        //判断注解是否存在
        if (!classT.isAnnotationPresent(Table.class))
            throw new EasyException("实体类对应的表名没有注入");


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
                foreignKeyModel.classModel = parseClassType(connection,foreignField.getType());
                foreignKeyModel.field = field;
                result.foreignKeys.add(foreignKeyModel);
            }
        }
        if (result.primaryKeys.size() == 0)
            throw new EasyException("实体类对应的主键没有注入");
        result.allFieldsMap = fieldMap;

        //解析实体类对应的表名的注解信息
        result.tableName = ((Table) classT.getAnnotation(Table.class)).value().toLowerCase();
        //解析表的相关信息
        cacheTable(connection, result.tableName);
        result.tableModel = getTableModel(result.tableName);

        return result;
    }

    private void cacheTable(Connection connection) {
        parseConnection(connection).forEach(tableModel -> tableMap.put(tableModel.tableName, tableModel));
    }
}
