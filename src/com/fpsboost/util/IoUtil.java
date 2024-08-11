package com.fpsboost.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
 
/**
 * @class_name: IoUtil
 * @description:
 * @author: wm_yu
 * @create: 2019/08/22
 **/
public class IoUtil {
 
    /**
     * 字符串转换为BufferedReader
     * @param source
     * @return
     */
    public static BufferedReader StringToBufferedReader(String source){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(source.getBytes());
        InputStream inputStream = byteArrayInputStream;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return reader;
    }
 
}
 