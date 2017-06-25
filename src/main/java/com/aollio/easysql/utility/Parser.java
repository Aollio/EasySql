package com.aollio.easysql.utility;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by Finderlo on 2016/10/25.
 */
public interface Parser<T>{
     T parse(ResultSet resultSet, int i);
}
