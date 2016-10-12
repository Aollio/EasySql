package com.finderlo.easysql;

import javafx.scene.control.Tab;

import java.util.List;
import java.util.Map;

import static com.finderlo.easysql.Util.*;

/**
 * Created by Finderlo on 2016/10/11.
 */
class Sentence {

    static String jointDelete(String tableName, String columName, String args) {
        return "delete from " + tableName + " where " + columName + "='" + args + "'";
    }

    static String jointDelete(Table table, ObjectFieldValue objectFieldValue) {

        String sql = "";
        if (objectFieldValue.fieldValueInfoMap.size() >= 2) {
            sql = "delete from " + table.tableName + " where ? = '?' and ? = '?'";
        } else {
            sql = "delete from " + table.tableName + " where ? = '?'";
        }

        int nextIndex = sql.indexOf("?");
        //替换？为value
        for (int i = 0; i <table.columnsKey.size(); i++) {
            String key = table.columnsKey.get(i);
            if (objectFieldValue.isFieldExist(key)){
                sql = replace(nextIndex, key, sql);
                nextIndex = nextIndex + 5 + key.length() - 1;
                String value = objectFieldValue.getValue(key).toString();
                sql = replace(nextIndex,value,sql);
                nextIndex = nextIndex + 7 + value.length() -1;
                if (nextIndex > sql.length()){
                    break;
                }
            }
        }
        return sql;
    }

    /**
     * 拼接插入语句
     **/
    static String jointInsert(Table table,ObjectFieldValue objectFieldValue) throws EasyException {
        if (table.columnsKey.isEmpty()) {
            throw new EasyException("表中的列名为空，初始化失败");
        }
        String sql = initInsert(table.tableName, table.columnsKey.size());

        //使用指针来确定替换位置，防止插入失败
        int nextIndex = sql.indexOf("$");
        //如果要替换字段中含有$的话，会造成插入失败
        //替换$为key
        for (int i = 0; i < table.columnsKey.size(); i++) {
//            sql = sql.replaceFirst("\\$", fieldsName.get(i));
            sql = replace(nextIndex, table.columnsKey.get(i), sql);
            nextIndex = nextIndex + 2 + table.columnsKey.get(i).length() - 1;
        }
        //使用指针下标来确定替换位置，防止插入失败
        nextIndex = sql.indexOf("?");
        //如果要替换值中含有？的话，会造成插入失败
        //替换？为value
        for (int i = 0; i < table.columnsKey.size(); i++) {
            Object value = objectFieldValue.getValue(table.columnsKey.get(i));
            if (null==value){
                continue;
            }
            int valueCount = objectFieldValue.getValue(table.columnsKey.get(i)).toString().length();
            sql = replace(nextIndex, objectFieldValue.getValue(table.columnsKey.get(i)).toString(), sql);
            nextIndex = (nextIndex + 4 + valueCount - 1);
//            sql = sql.replaceFirst("\\?", fieldsValuesMap.get(fieldsName.get(i)).value.toString());
        }
        return sql;
    }


    /**
     * 返回有2*length的占位符的插入语句，字段使用$占位，值使用？占位
     **/
    private static String initInsert(String tableName, int length) {
        String head = "insert into " + tableName + " (";
        String mid = ") values(";
        String tail = ")";
        StringBuilder stringBuilderValue = new StringBuilder();
        StringBuilder stringBuilderKey = new StringBuilder();

        if (length == 1) {
            stringBuilderKey.append("$");
        } else if (length == 2) {
            stringBuilderKey.append("$,$");
        } else {
            for (int i = 0; i < length - 1; i++) {
                stringBuilderKey.append("$,");
            }
            stringBuilderKey.append("$");
        }

        if (length == 1) {
            stringBuilderValue.append("'?'");
        } else if (length == 2) {
            stringBuilderValue.append("'?','?'");
        } else {
            for (int i = 0; i < length - 1; i++) {
                stringBuilderValue.append("'?',");
            }
            stringBuilderValue.append("'?'");
        }
        String value = stringBuilderValue.toString();
        String key = stringBuilderKey.toString();
        return head + key + mid + value + tail;
    }

}
