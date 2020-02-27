package com.john.sqlSession;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author:wenwei
 * @date:2020/02/22
 * @description:
 */
public interface SqlSession {

    //查询所有
    public <T> List<T> selectList(String statementId,Object... params) throws IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException;

    //根据条件，查询
    public <E> E selectOne(String statementId,Object... params) throws IllegalAccessException, ClassNotFoundException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException;


    public void insert(String statementId,Object...params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException;

    public void update(String statementId,Object...params) throws ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException;

    public void delete(String statementId,Object...params) throws ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException;

    //为Dao层接口生成代理实现类
    public <T> T getMapper(Class<?> mapperClass);

    public void commit();

    public void rollBack();

}

