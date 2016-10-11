package com.finderlo.easysql;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Finderlo on 2016/10/10.
 */
public abstract class BaseSQLDao {

    public static final String INT = "int";
    public static final String STRING = "String";
    public static final String BOOLEAN = "boolean";
    public static final String FLOAT = "float";

    protected Class mClassT;
    //每个类中所有属性的名称容器
    protected List<String> mFieldsName = new ArrayList<>();
    //类中所有属性的集合
    protected List<Field> mFields = new ArrayList<>();
    //每个属性的类型和值的映射
    protected Map<String, Typer> mNameAndTyper = new HashMap<>();

    //插入数据库的连接
    protected Connection mConnection = null;
    //插入数据库的表名
    protected String mTableName ;

    protected boolean mIsAlreadyGetFields = false;
    protected boolean mIsAlreadyGetObjectValues = false;

    BaseSQLDao(Connection connection, String tableName) {
        this.mConnection = connection;
        this.mTableName = tableName;
        initConnection();
    }

    protected  void initConnection(){
        try {
            mConnection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void getFields(Object object, Class classT) throws EasyException {
        getFields(classT);
        mNameAndTyper = Util.getFieldsNameAndValues(object, classT);
    }
    protected void getFields(Class classT){
        mClassT = classT;
        Util.getFields(classT).forEach(field -> {mFields.add(field);mFieldsName.add(field.getName());});
        mIsAlreadyGetFields = true;
    }

     int executeUpdate(String sql){
        int count = 0;
        try {
            if (mConnection.getAutoCommit()){
                mConnection.setAutoCommit(false);
            }
            Statement statement = mConnection.createStatement();
            count = statement.executeUpdate(sql);
            mConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                mConnection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            count = 0;
        }
        return count;
    }

    ResultSet executeQuery(String sql){
        ResultSet resultSet = null;
        try {
            if (mConnection.getAutoCommit()){
                mConnection.setAutoCommit(false);
            }
            Statement statement = mConnection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


//    protected void getFields(Object object, Class classT) throws IllegalAccessException {
//        getFields(classT);
//        for (int i = 0; i < mFields.size(); i++) {
//            Field field = mFields.get(i);
//            field.setAccessible(true);
//            Class fieldType = field.getType();
//            String fieldName = field.getName();
//
//            //判断属性的类型,放入map中
//            if (fieldType.getSimpleName().equals(STRING)) {
//                mNameAndTyper.put(fieldName, new com.finderlo.easysql.Typer(String.class, field.get(object)));
//            } else if (fieldType.getSimpleName().equals(INT)) {
//                mNameAndTyper.put(fieldName, new com.finderlo.easysql.Typer(int.class, field.getInt(object)));
//            } else if (fieldType.getSimpleName().equals(BOOLEAN)) {
//                mNameAndTyper.put(fieldName, new com.finderlo.easysql.Typer(boolean.class, field.getBoolean(object)));
//            } else if (fieldType.getSimpleName().equals(FLOAT)) {
//                mNameAndTyper.put(fieldName, new com.finderlo.easysql.Typer(float.class, field.getFloat(object)));
//            } else {
//                mNameAndTyper.put(fieldName, new com.finderlo.easysql.Typer(fieldType, field.get(object)));
//            }
//        }
//    }

//    protected void getFields(Class classT){
//        mClassT = classT;
//    Field[] tFields = classT.getDeclaredFields();
//        for (int i = 0; i < tFields.length; i++) {
//        mFields.add(tFields[i]);
//        //将每个属性的名称放入数组中
//        mFieldsName.add(tFields[i].getName());
//        tFields[i].setAccessible(true);
//    }
//    mIsAlreadyGetFields = true;
//}



}
