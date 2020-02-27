package com.john.config;

import com.john.utils.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:wenwei
 * @date:2020/02/22
 * @description:
 */
public class BoundSql {
    private String sqlText;//解析出来的sql
    private List<ParameterMapping> parameterMappingList = new ArrayList();

    public BoundSql(String sqlText, List<ParameterMapping> parameterMappingList) {
        this.sqlText = sqlText;
        this.parameterMappingList = parameterMappingList;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public List<ParameterMapping> getParameterMappingList() {
        return parameterMappingList;
    }

    public void setParameterMappingList(List<ParameterMapping> parameterMappingList) {
        this.parameterMappingList = parameterMappingList;
    }
}
