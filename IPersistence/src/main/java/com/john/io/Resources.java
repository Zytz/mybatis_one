package com.john.io;

import java.io.InputStream;

/**
 * @author:wenwei
 * @date:2020/02/21
 * @description:
 */
public class Resources {
//    根据配置文件的路径，将配置文件加载成字节输入流，存储在内存中
    public static InputStream getResourceAsStream(String path){
        InputStream resourceStream = Resources.class.getClassLoader().getResourceAsStream(path);
        return resourceStream;
    }
}
