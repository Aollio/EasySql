package com.finderlo.easysql;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by Finderlo on 2016/10/25.
 */
public interface Parser{
    void parse(ResultSet resultSet, int i);
}
