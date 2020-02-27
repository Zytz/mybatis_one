package com.john.sqlSession;

import com.john.config.BoundSql;
import com.john.builder.Configuration;
import com.john.builder.MapperStatement;
import com.john.utils.GenericTokenParser;
import com.john.utils.ParameterMapping;
import com.john.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:wenwei
 * @date:2020/02/22
 * @description:
 */
public class SimpleExecutor implements Executor {
    //user
    @Override
    public <E> List<E> query(Configuration configuration, MapperStatement mapperStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException {

//        1： 注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();
        connection.setAutoCommit(false);


        //2:获取sql 语句 select * from users where id=#{id} and username=#{username}
        //转换sql语句 select * from user where id = ? and username=?
        String sql = mapperStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);

        //3: 获取预处理对象： prepareStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        //4：设置参数，

        //获取到参数的全路径
        String parameterType = mapperStatement.getParameterType();
        Class<?> parammeterTypeClass = getClassType(parameterType);


        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            //反射
            Field declaredFileld = parammeterTypeClass.getDeclaredField(content);
            declaredFileld.setAccessible(true);
            Object object = declaredFileld.get(params[0]);

            preparedStatement.setObject(i + 1, object);

        }

        //5:执行sql
        String resultType = mapperStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);


        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Object> resultObjects = new ArrayList();

        //6： 封装返回值
        while (resultSet.next()) {
            Object resultObject = resultTypeClass.newInstance();
            //愿数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //获取到字段名
                String columnName = metaData.getColumnName(i);
                //字断的值
                Object value = resultSet.getObject(columnName);
                //使用反射或者内省，根据数据库表和实体的对应关系，完成封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(resultObject, value);
            }

            resultObjects.add(resultObject);
        }

        return (List<E>) resultObjects;
    }

    @Override
    public void commit(Configuration configuration) throws SQLException {
        configuration.getDataSource().getConnection().commit();
    }
    @Override
    public void rollBack(Configuration configuration) throws SQLException {
        configuration.getDataSource().getConnection().rollback();

    }

    @Override
    public void insert(Configuration configuration, MapperStatement mapperStatement, Object[] params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {


        //        1： 注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();
//        connection.setAutoCommit(false);

        System.out.println( connection.getAutoCommit());

        //2:获取sql 语句 select * from users where id=#{id} and username=#{username}
        //转换sql语句 select * from user where id = ? and username=?
        String sql = mapperStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);

        //3: 获取预处理对象： prepareStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        //4：设置参数，

        //获取到参数的全路径
        String parameterType = mapperStatement.getParameterType();
        Class<?> parammeterTypeClass = getClassType(parameterType);


        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            //反射
            Object object;
            if (!isPrimitive(parammeterTypeClass)) {
                Field declaredFileld = parammeterTypeClass.getDeclaredField(content);
                declaredFileld.setAccessible(true);
                object = declaredFileld.get(params[0]);
            } else {
                object = params[0];
            }

            preparedStatement.setObject(i + 1, object);

        }

        preparedStatement.executeUpdate();

    }

    @Override
    public void update(Configuration configuration, MapperStatement mapperStatement, Object[] params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        insert(configuration, mapperStatement, params);
    }

    @Override
    public void delete(Configuration configuration, MapperStatement mapperStatement, Object[] params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        insert(configuration, mapperStatement, params);
    }

    private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
        if (parameterType != null) {
            Class<?> cls = Class.forName(parameterType);
            return cls;
        }
        return null;
    }

    /**
     * 完成#{} 的解析工作，
     * 1：将#{}使用？进行代替，
     * 2： 解析出#{} 里面的值，进行存储
     *
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        //标记处理类： 配置标记解析起来完成占位符的解析处理工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出来的sql
        String parse = genericTokenParser.parse(sql);
        //#{}里面解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        BoundSql boundSql = new BoundSql(parse, parameterMappings);
        return boundSql;
    }


    public static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || cls == String.class || cls == Boolean.class || cls == Character.class
                || Number.class.isAssignableFrom(cls) || Date.class.isAssignableFrom(cls);
    }

}
