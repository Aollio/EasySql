package com.finderlo.easysql;

import com.finderlo.easysql.model.ObjectFieldValue;
import com.finderlo.easysql.model.TableModel;
import com.finderlo.easysql.utility.EasyException;

import static com.finderlo.easysql.Util.*;

/**
 * Created by Finderlo on 2016/10/11.
 */
class Sentence {

    static String jointUpdate(String tableName, String setKey, String setValue, String whereKey, String whereValue) {
        return "update " + tableName + " set " + setKey + "=" + setValue + " where " + whereKey + "=" + whereValue;
    }

    static String jointDelete(String tableName, String columName, String args) {
        return "delete from " + tableName + " where " + columName + "='" + args + "'";
    }

    static String jointDelete(TableModel tableModel, ObjectFieldValue objectFieldValue) {

        String sql = "";

        String head = "delete from " + tableModel.tableName + " where ";
        StringBuilder middle = new StringBuilder();
        for (int i = 0; i < tableModel.primaryKey.size(); i++) {
            if (i == 0) {
                middle.append("$ = '?' ");
            } else {
                middle.append("and $ = '?' ");
            }
        }
        sql = head + middle.toString();

        //替换算法
        //替换参数
        int k = 0;
        int v = 0;
        char[] sqlChar = sql.toCharArray();

        StringBuilder sqlBuiler = new StringBuilder(sql);

        for (int i = sql.length() - 1; i >= 0; i--) {
            if (sqlChar[i] == '$' && k < tableModel.primaryKey.size()) {
                String key = tableModel.primaryKey.get(tableModel.primaryKey.size() - 1 - k);
                sqlBuiler.delete(i, i + 1);
                sqlBuiler.insert(i, key);
                k++;
            } else if (sqlChar[i] == '?' && v < tableModel.primaryKey.size()) {
                String key = tableModel.primaryKey.get(tableModel.primaryKey.size() - 1 - v);
                String value = objectFieldValue.getValue(key).toString();
                sqlBuiler.delete(i, i + 1);
                sqlBuiler.insert(i, value);
                v++;
            }
        }

        return sqlBuiler.toString();
    }

    /**
     * 拼接插入语句
     **/
    static String jointInsert(TableModel tableModel, ObjectFieldValue objectFieldValue) throws EasyException {
        if (tableModel.columnsKey.isEmpty()) {
            throw new EasyException("表中的列名为空，初始化失败");
        }
        String sql = initInsert(tableModel.tableName, tableModel.columnsKey.size());

        //替换算法
        //替换参数
        int k = 0;
        int v = 0;
        char[] sqlChar = sql.toCharArray();

        StringBuilder sqlBuiler = new StringBuilder(sql);

        for (int i = sql.length() - 1; i >= 0; i--) {
            if (sqlChar[i] == '$' && k < tableModel.columnsKey.size()) {
                String key = tableModel.columnsKey.get(tableModel.columnsKey.size() - 1 - k);
                sqlBuiler.delete(i, i + 1);
                sqlBuiler.insert(i, key);
                k++;
            } else if (sqlChar[i] == '?' && v < tableModel.columnsKey.size()) {
                String key = tableModel.columnsKey.get(tableModel.columnsKey.size() - 1 - v);
                String value = objectFieldValue.getValue(key).toString();
                sqlBuiler.delete(i, i + 1);
                sqlBuiler.insert(i, value);
                v++;
            }
        }
        return sqlBuiler.toString();
    }


    static String jointQueryWithArg(TableModel tableModel, String columnName, String arg) {
        return "select * from " + tableModel.tableName + " where " + columnName + " = '" + arg + "'";
    }

    static String jointQueryWithoutArg(TableModel tableModel) {
        return "select * from " + tableModel.tableName;
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
