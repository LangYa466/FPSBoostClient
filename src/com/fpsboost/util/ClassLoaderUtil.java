package com.fpsboost.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassLoaderUtil {

    public static Class<?> load(String className,String classPackage) {
        try {
            // URL to the class file
            URL url = new URL(String.format("http://122.51.47.169/%s.class",className));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get the class file as an InputStream
            InputStream inputStream = connection.getInputStream();

            // Read the class bytes using ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();

            // Convert ByteArrayOutputStream to byte array
            byte[] classBytes = byteArrayOutputStream.toByteArray();

            // Define the class
            FPSBoostClassLoader classLoader = new FPSBoostClassLoader();
            Class<?> clazz = classLoader.defineClass(classPackage, classBytes);
            System.out.println("云更新: " + clazz.getName());

            return clazz;

        } catch (Exception e) {
            throw new RuntimeException("云更新错误: " + e.getMessage(), e);
        }
    }
}

class FPSBoostClassLoader extends ClassLoader {
    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}
