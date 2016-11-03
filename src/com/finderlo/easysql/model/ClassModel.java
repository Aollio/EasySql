package com.finderlo.easysql.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Finderlo on 2016/10/12.
 */
public class ClassModel {

    public String className;    //类名
    public String tableName;   //实体类对应的表名

    public Class classType;    //类的引用
    public TableModel tableModel;

    public List<String> primaryKeys = new ArrayList<>();    //属性中对应的主键，可能有多个

    public List<ForeignKeyModel> foreignKeys = new ArrayList<>();    //属性中对应的外键，可能有多个,映射关系为

    public Map<String, Field> allFieldsMap = new HashMap<>();    //类中的属性的映射

    enum ClassKey{}

    public ClassModel() {
    }

    public ClassModel(Class classType, Map<String, Field> allFieldsMap) {
        this.classType = classType;
        this.className = classType.getSimpleName().toLowerCase();
        this.allFieldsMap = allFieldsMap;
    }

    boolean isFieldExist(String fieldName) {
        for (String key : allFieldsMap.keySet()) {
            if (key.toLowerCase().equals(fieldName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public Field getField(String fieldName) {
        for (String key : allFieldsMap.keySet()) {
            if (key.toLowerCase().equals(fieldName.toLowerCase())) {
                return allFieldsMap.get(key.toLowerCase());
            }
        }
        return null;
    }


    public static class ForeignKeyModel {

        public String fieldName;//外键在类中的属性

        public String columnName;//外键在对应表中对应的列名

        public String tableName;//外键对应表的表名

        public Field field;//这个字段自己的属性

        public ClassModel classModel;//外键对应的实体类

        public Field foreignField;//连接查询时用于赋值的属性对象
    }
}
