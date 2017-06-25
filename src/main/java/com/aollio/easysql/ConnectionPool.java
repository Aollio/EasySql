package com.aollio.easysql;

/**
 * Created by Finderlo on 2016/10/29.
 */
public interface ConnectionPool {

    int getPoolConnCount();

    SpecConnection getConnection() ;

    boolean closePool();

    void returnConnection(SpecConnection specConnection);

}
