package com.fpsboost.plugin;

import com.fpsboost.Access;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassLoaderUtil {

    public static Class<?> load(String className,String classPackage) {
        try {
            // URL to the class file
            URL url = new URL(String.format(Access.CLIENT_WEBSITE + "%s.class",className));
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

    public static List<Class<?>> load(File jarFile) {
        List<Class<?>> classes = new ArrayList<>();

        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();

            FPSBoostClassLoader classLoader = new FPSBoostClassLoader();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();

                // Only process class files
                if (entryName.endsWith(".class")) {
                    String className = entryName.replace('/', '.').replace(".class", "");

                    // Load the class
                    try (InputStream is = jar.getInputStream(entry)) {
                        byte[] classBytes = readInputStream(is);
                        Class<?> clazz = classLoader.defineClass(className, classBytes);
                        classes.add(clazz);
                        System.out.println("加载插件失败 Loaded class: " + clazz.getName());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("加载插件失败 Error loading classes from jar file: " + e.getMessage(), e);
        }

        return classes;
    }

    private static byte[] readInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int bytesRead;

        while ((bytesRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

}

class FPSBoostClassLoader extends ClassLoader {
    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}
