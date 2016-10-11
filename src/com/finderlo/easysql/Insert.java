package com.finderlo.easysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Finderlo on 2016/10/10.
 */
public class Insert extends BaseSQLDao {


    Insert(Connection connection, String tableName) {
        super(connection, tableName);
    }


    public boolean insert(Object object, Class classT) throws EasyException {
        getFields(object, classT);
        Statement statement = null;
        int result = 0;
        try {
            mConnection.setAutoCommit(false);
            statement = mConnection.createStatement();
            System.out.println(jointSql());
            result = statement.executeUpdate(jointSql());
            mConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                mConnection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return false;
        }

        return result == 1;
    }

    public boolean insert(Object object) throws  EasyException {
        return insert(object, object.getClass());
    }

    private String jointSql() throws EasyException, SQLException {
        if (mFieldsName.isEmpty()) {
            throw new EasyException("The model object is empty or isn't initialize");
        }
//        String sql = jointSql(mFieldsName.size());
//        //替换$为key
//        for (int i = 0; i < mFieldsName.size(); i++) {
//            sql = sql.replaceFirst("\\$", mFieldsName.get(i));
//        }
//        //替换？为value
//        for (int i = 0; i < mFieldsName.size(); i++) {
//            sql = sql.replaceFirst("\\?", "'" + mNameAndTyper.get(mFieldsName.get(i)).value.toString() + "'");
//        }
        return Sentence.jointInsert(mTableName,mFieldsName,mNameAndTyper);

    }
//
//    private String jointSql(int length) {
//        String head = "insert into " + mTableName + " (";
//        String mid = ") values(";
//        String tail = ")";
//        StringBuilder stringBuilderValue = new StringBuilder();
//        StringBuilder stringBuilderKey = new StringBuilder();
//
//        if (length == 1) {
//            stringBuilderValue.append("?");
//        } else if (length == 2) {
//            stringBuilderValue.append("?,?");
//        } else {
//            for (int i = 0; i < length - 1; i++) {
//                stringBuilderValue.append("?,");
//            }
//            stringBuilderValue.append("?");
//        }
//
//        if (length == 1) {
//            stringBuilderKey.append("$");
//        } else if (length == 2) {
//            stringBuilderKey.append("$,$");
//        } else {
//            for (int i = 0; i < length - 1; i++) {
//                stringBuilderKey.append("$,");
//            }
//            stringBuilderKey.append("$");
//        }
//
//        String value = stringBuilderValue.toString();
//        String key = stringBuilderKey.toString();
//        return head + key + mid + value + tail;
//    }


//    public void insert(Object object, Class classT) throws SQLException, IllegalAccessException {
//        getField(object, classT);
//        String sql = jointSql(fieldsName.size());
//
//
//        //替换$为key
//        for (int i = 0; i < fieldsName.size(); i++) {
//            sql = sql.replaceFirst("\\$", fieldsName.get(i));
//        }
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//
//        //替换？为value
//        for (int i = 1; i < fieldsName.size() + 1; i++) {
//            preparedStatement.setString(i, nameAndTyper.get(fieldsName.get(i - 1)).value.toString());
//        }
//        System.out.println(sql);
//        System.out.println(preparedStatement.toString());
//        int count = preparedStatement.executeUpdate();
//        preparedStatement.close();
//        connection.close();
//
//    }
}
