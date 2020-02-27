package com.john.builder;

/**
 * @author:wenwei
 * @date:2020/02/21
 * @description:
 */
public class MapperStatement {
//    id
    private String id ;
//    返回类型
    private String resultType;
//    传入参数
    private String parameterType;

    private String sql;
    //sql执行乐行
    private SqlCommandType sqlCommandType;

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
}
