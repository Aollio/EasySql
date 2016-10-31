package com.finderlo.easysql;

import com.finderlo.easysql.utility.EasyException;

/**
 * Created by Finderlo on 2016/10/29.
 */
public interface ConnectionPool {

    int getPoolConnCount();

    SpecConnection getConnection() throws EasyException;

    boolean closePool();

    void returnConnection(SpecConnection specConnection);

}
