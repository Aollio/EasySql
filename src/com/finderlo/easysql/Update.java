package com.finderlo.easysql;

import java.sql.Connection;

/**
 * Created by Finderlo on 2016/10/11.
 */
public class Update extends BaseSQLDao {

    Update(Connection connection, String tableName) {
        super(connection, tableName);
    }


    public boolean update(String setKey, String setValue, String whereKey, String whereValue) {
        return executeUpdate(jointSql(setKey, setValue, whereKey, whereValue)) == 1;
    }

    private String jointSql(String setKey, String setValue, String whereKey, String whereValue) {
        return " update " + mTable.tableName + " set " + setKey + " = '" + setValue + "' where " + whereKey + " = '" + whereValue + "'";
    }
}
