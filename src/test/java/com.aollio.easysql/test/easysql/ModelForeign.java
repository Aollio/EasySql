package com.aollio.easysql.test.easysql;

import com.aollio.easysql.annotation.PrimaryKey;
import com.aollio.easysql.annotation.Table;

/**
 * Created by Finderlo on 2016/10/22.
 */
@Table("test_foregin")
public class ModelForeign {

    @PrimaryKey
    public String test_id;

    public String nameone;
}
