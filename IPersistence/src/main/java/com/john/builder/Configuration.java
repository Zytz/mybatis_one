package com.john.builder;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:wenwei
 * @date:2020/02/21
 * @description:
 */
public class Configuration {

    private DataSource dataSource;

    /**
     * key statementId : sql的唯一表示，namespace+id
     * value : 封装好的mapperStatement
     */
    Map<String,MapperStatement>  mapperStatementMap = new HashMap();;

    public DataSource getDataSource() {
        return dataSource;
    }

    public Map<String, MapperStatement> getMapperStatementMap() {
        return mapperStatementMap;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setMapperStatementMap(Map<String, MapperStatement> mapperStatementMap) {
        this.mapperStatementMap = mapperStatementMap;
    }
}
