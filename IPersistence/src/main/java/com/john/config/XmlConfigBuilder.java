package com.john.config;

import com.john.io.Resources;
import com.john.builder.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author:wenwei
 * @date:2020/02/21
 * @description:
 */
public class XmlConfigBuilder {

    private Configuration configuration;

    public XmlConfigBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * 使用dom4j对配置文件进行解析
     * @param inputStream
     * @return
     */
    public Configuration parseConfig(InputStream inputStream) throws DocumentException, PropertyVetoException {


        //configuration
        Document document = new SAXReader().read(inputStream);
        //得到整个文档根目录
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//property");
        //数据库信息都配置在propertys
        Properties properties = new Properties();
        for (Element element : list){
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name,value);
        }

        //数据库链接池数据的初始化

        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();;
        comboPooledDataSource.setDriverClass( properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));

        configuration.setDataSource(comboPooledDataSource);


        //mapper
        List<Element> mapperList = rootElement.selectNodes("//mapper");

        for(Element element : mapperList){
            String mapperPath = element.attributeValue("resource");
            InputStream resourceAsStream = Resources.getResourceAsStream(mapperPath);
            XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
            xmlMapperBuilder.parse(resourceAsStream);
        }

        return configuration;
    }
}
