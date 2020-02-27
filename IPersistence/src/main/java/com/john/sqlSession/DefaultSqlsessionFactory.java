package com.john.sqlSession;

import com.john.builder.Configuration;

import java.sql.SQLException;

/**
 * @author:wenwei
 * @date:2020/02/22
 * @description:
 */
public class DefaultSqlsessionFactory  implements SqlSessionFactory{
    private Configuration configuration;

    public DefaultSqlsessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {
        return new DefaultSqlsession(configuration);
    }

    public void commit() throws SQLException {
        configuration.getDataSource().getConnection().commit();

    }

    public void rollBack() {
         new DefaultSqlsessionFactory(configuration).rollBack();
    }
}
