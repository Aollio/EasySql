package com.finderlo.easysql;

import java.sql.Connection;

/**
 * Created by Finderlo on 2016/10/11.
 */
public class Update extends BaseSQLDao {

    Update(Connection connection, String tableName) {
        super(connection, tableName);
    }

//    public boolean update(String willUpdateKey,Object object, Class classT) throws com.finderlo.easysql.EasyException {
////        getFields(object, classT);
////        //先删除
////
////        return result == 1;
//    }

    public boolean update(String setKey, String setValue, String whereKey, String whereValue) {
        return executeUpdate(jointSql(setKey, setValue, whereKey, whereValue)) == 1;
    }

    private String jointSql(String setKey, String setValue, String whereKey, String whereValue) {
        return " update " + mTableName + " set " + setKey + " = '" + setValue + "' where " + whereKey + " = '" + whereValue + "'";
    }
}
