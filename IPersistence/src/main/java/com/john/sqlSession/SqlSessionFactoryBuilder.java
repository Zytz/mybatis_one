package com.john.sqlSession;

import com.john.config.XmlConfigBuilder;
import com.john.builder.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @author:wenwei
 * @date:2020/02/21
 * @description:
 */
public class SqlSessionFactoryBuilder {
    public SqlSessionFactory buid(InputStream inputStream) throws PropertyVetoException, DocumentException {
        //  第一： 使用dom4j解析配置文件， 将解析出来额内容封装到Configuration中
        XmlConfigBuilder xmlConfigBuiler = new XmlConfigBuilder();
        Configuration configuration = xmlConfigBuiler.parseConfig(inputStream);


        // 第二：创建sqlSessionFactory对象：工厂类：生产sqlSession:会话对象
        DefaultSqlsessionFactory defaultSqlsessionFactory = new DefaultSqlsessionFactory(configuration);


        return defaultSqlsessionFactory;

    }
}
