package com.finderlo.easysql;

import java.sql.Connection;

import static com.finderlo.easysql.Sentence.*;

/**
 * Created by Finderlo on 2016/10/10.
 */
public class Delete extends BaseSQLDao {

    Delete(Connection connection, String tableName) {
        super(connection, tableName);
    }

    public boolean delete(Object object, Class classT) throws EasyException {
        getFields(object, classT);
        return executeUpdate(jointSql()) == 1;
    }

    public boolean delete(Object object) throws EasyException {
        return delete(object, object.getClass());
    }

    /**
     * 用作删除表中某一行的数据。
     *
     * @param columnName
     * @param args       在表中的列名字段和参数。建议输入主键和主键值
     * @return 是否删除成功
     **/
    public boolean delete(String columnName, String args){
        return executeUpdate(jointSql(columnName, args)) == 1;
    }

    private String jointSql(String columName, String args) {
        return jointDelete(mTableName,columName,args);
    }


    private String jointSql() throws EasyException {
        if (mFieldsName.isEmpty()) {
            throw new EasyException("The model object is empty or isn't initialize");
        }
//
//        String sql = "";
//        if (mFieldsName.size() >= 2) {
//            sql = "delete from " + mTableName + " where ? = '?' and ? = '?'";
//        } else {
//            sql = "delete from " + mTableName + " where ? = '?'";
//        }
//
//        //替换？为value
//        for (int i = 0; i < mFieldsName.size(); i++) {
//            sql = sql.replaceFirst("\\?", mFieldsName.get(i));
//            sql = sql.replaceFirst("\\?", mNameAndTyper.get(mFieldsName.get(i)).value.toString());
//        }
        return jointDelete(mTableName,mFieldsName,mNameAndTyper);
    }


}
