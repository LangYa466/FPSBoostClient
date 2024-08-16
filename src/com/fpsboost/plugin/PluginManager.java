package com.fpsboost.plugin;

import com.fpsboost.Access;
import com.fpsboost.annotations.system.Module;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LangYa
 * @since 2024/8/16 下午2:18
 */
public class PluginManager implements Access.InstanceAccess {

    private final File dir = new File(Access.DIRECTORY,"plugins");

    public void init() {
        if (!dir.exists()) {
            dir.mkdir();
        }

        List<File> jarFiles = getJarFiles(dir);
        if (jarFiles.isEmpty()) {
            System.out.println("未寻找到插件");
        } else {
            for (File jarFile : jarFiles) {
                for (Class<?> clazz : ClassLoaderUtil.load(jarFile)) {
                    if(clazz.isAnnotationPresent(Module.class)){
                        Module module = clazz.getAnnotation(Module.class);
                        Access.getInstance().getModuleManager().register(clazz,module.value(),module.category());
                        System.out.println("注册插件 >> " + clazz.getName());
                    }
                }
            }
        }
    }

    public static List<File> getJarFiles(File directory) {
        List<File> jarFiles = new ArrayList<>();

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".jar")) {
                        jarFiles.add(file);
                    }
                }
            }
        }

        return jarFiles;
    }

}
