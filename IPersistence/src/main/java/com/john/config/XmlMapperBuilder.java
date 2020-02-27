package com.john.config;

import com.john.builder.Configuration;
import com.john.builder.MapperStatement;
import com.john.builder.SqlCommandType;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author:wenwei
 * @date:2020/02/22
 * @description:
 */
public class XmlMapperBuilder {
    private Configuration configuration;

    public XmlMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");

        Iterator iterator = rootElement.elementIterator();

        while (iterator.hasNext()){
            Element element = (Element) iterator.next();
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("parameterType");
            String sqlText = element.getTextTrim();
            MapperStatement mapperStatement = new MapperStatement();
            mapperStatement.setId(id);
            mapperStatement.setResultType(resultType);
            mapperStatement.setParameterType(parameterType);
            mapperStatement.setSql(sqlText);
            String key = namespace + "." + id;

            if(element.getName().equals("select")) {
                mapperStatement.setSqlCommandType(SqlCommandType.SELECT);
            }else if(element.getName().equals("update")){
                mapperStatement.setSqlCommandType(SqlCommandType.UPDATE);
            }else if(element.getName().equals("insert")){
                mapperStatement.setSqlCommandType(SqlCommandType.INSERT);
            }else if(element.getName().equals("delete")){
                mapperStatement.setSqlCommandType(SqlCommandType.DELETE);
            }else {
                System.out.println("error type");
            }


            configuration.getMapperStatementMap().put(key, mapperStatement);
        }



    }
}
