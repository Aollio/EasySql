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


    public int insert(Object object) throws EasyException, SQLException {
        initObject(object);
        return executeUpdate(jointSql());
    }

    private String jointSql() throws EasyException, SQLException {
        if (mObjectFieldValue.fieldValueInfoMap.isEmpty()) {
            throw new EasyException("The model object is empty or isn't initialize");
        }
        return Sentence.jointInsert(mTable,mObjectFieldValue);
    }
}
