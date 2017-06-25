package com.aollio.easysql.test.easysql;

import com.aollio.easysql.annotation.ForeignKey;
import com.aollio.easysql.annotation.PrimaryKey;
import com.aollio.easysql.annotation.Table;

/**
 * Created by Finderlo on 2016/10/22.
 */
@Table("test_model")
public class Model {

    @PrimaryKey
    public String primary_one;

    @PrimaryKey
    public String primary_two;

    @ForeignKey(coloum = "test_id",table = "test_foregin", modelKey = "foreign")
    public String foreign_one;

    public ModelForeign foreign;

}
