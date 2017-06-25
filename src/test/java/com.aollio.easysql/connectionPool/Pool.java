package com.aollio.easysql.connectionPool;

import java.sql.Connection;

/**
 * Created by Finderlo on 2016/10/20.
 */
public interface Pool {

    Connection getConnection();

    int getPoolCount();


    int getIncrementalCount();
}
