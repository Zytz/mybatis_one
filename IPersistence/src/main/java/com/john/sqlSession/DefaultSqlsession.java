package com.john.sqlSession;

import com.john.builder.Configuration;
import com.john.builder.MapperStatement;
import com.john.builder.SqlCommandType;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

/**
 * @author:wenwei
 * @date:2020/02/22
 * @description:
 */
public class DefaultSqlsession implements SqlSession {
    private Configuration configuration;

    public DefaultSqlsession(Configuration configuration) {
        this.configuration = configuration;
    }

    public <T> List<T> selectList(String statementId, Object... params) throws IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MapperStatement mapperStatementMap = configuration.getMapperStatementMap().get(statementId);
        List<Object> list = simpleExecutor.query(configuration, mapperStatementMap, params);
        //将要完成对simpleExecutor的到调用
        return (List<T>) list;
    }

    public <E> E selectOne(String statementId, Object... params) throws IllegalAccessException, ClassNotFoundException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {

        List<Object> objects = selectList(statementId, params);
        if (objects.size() == 1) {
            return (E) objects.get(0);
        } else {
            throw new RuntimeException("查询结果为空或者返回为空");
        }
    }

    public void insert(String statementId, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        simpleExecutor.insert(configuration, mapperStatement, params);

    }

    public void update(String statementId, Object... params) throws ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        simpleExecutor.update(configuration, mapperStatement, params);

    }

    public void delete(String statementId, Object... params) throws ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MapperStatement mapperStatement = configuration.getMapperStatementMap().get(statementId);
        simpleExecutor.delete(configuration, mapperStatement, params);
    }

    public <T> T getMapper(Class<?> mapperClass) {
        //使用JDK动态代理为Dao接口生产代理对象，并且返回

        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlsession.class.getClassLoader()
                , new Class[]{mapperClass}, new InvocationHandler() {
                    //proxy 当前对象的引用
                    //method 当前方法的引用
                    //args 传入参数
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //底层实现还是执行JDBC代码。根据不同的情况调用selectList、selectOne

                        //准备参数 1:statenebtId:sql语句的唯一标志 namespace.id = 接口全限定名.方法
                        //方法名： findAll
                        String methodName = method.getName();
                        //方法的全限定名
                        String className = method.getDeclaringClass().getName();

                        String statementId = className + "." + methodName;

                        //准备参数2：params : args
                        //获取被调用方法的返回值类型
                        Type genericReturnType = method.getGenericReturnType();
                        //判断是否进行了范型参数化
                        return handleSqlCommandType(statementId, genericReturnType, args);


                    }
                });
        return (T) proxyInstance;

    }


    @Override
    public void commit() {
        try {
            this.configuration.getDataSource().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollBack() {
        System.out.println("rollback");
    }


    private Object handleSqlCommandType(String statementId, Type genericReturnType, Object[] args) throws IllegalAccessException, ClassNotFoundException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        SqlCommandType sqlCommandType = configuration.getMapperStatementMap().get(statementId).getSqlCommandType();
        switch (sqlCommandType) {
            case DELETE:
            case INSERT:
            case UPDATE:
                return handleUpdate(statementId, genericReturnType, args);
            case SELECT:
                return handleSelect(statementId, genericReturnType, args);
            default:

                throw new RuntimeException();
        }

    }

    private Object handleUpdate(String statementId, Type genericReturnType, Object[] args) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        update(statementId,args);
        return Boolean.TRUE;
    }

    private Object handleSelect(String statementId, Type genericReturnType, Object[] args) throws IllegalAccessException, ClassNotFoundException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        if (genericReturnType instanceof ParameterizedType) {
            List<Object> objects = selectList(statementId, args);
            return objects;
        }

        return selectOne(statementId, args);
    }

}
