package com.fpsboost.plugin;

import com.fpsboost.Access;
import com.fpsboost.annotations.system.Command;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.command.CommandHandle;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LangYa
 * @since 2024/8/16 下午2:18
 */
public class PluginManager implements Access.InstanceAccess {

    private final File dir = new File(Access.DIRECTORY,"Plugins");

    public void init() {
        File oldPluginDir = new File(Access.DIRECTORY,"plugins");
        if (oldPluginDir.exists()) {
            dir.delete();
        }
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
                        Access.getInstance().getModuleManager().register(clazz,module.name(),"",module.category(),module.description());
                        System.out.println("注册插件 >> " + clazz.getName());
                    }
                    if(clazz.isAnnotationPresent(Command.class)){
                        if(clazz.isAnnotationPresent(Command.class)){
                            Command command = clazz.getAnnotation(Command.class);
                            Object inst = null;
                            try {
                                inst = Access.getInstance().getInvoke().createInstance(clazz);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Object finalInst = inst;
                            try {
                                assert finalInst != null;
                                Access.getInstance().getInvoke().autoWired(finalInst);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Access.getInstance().getInvoke().registerBean(finalInst);
                            CommandHandle handle = new CommandHandle() {

                                @Override
                                public void run(String[] args) {
                                    for (Method handler : getHandlers()) {
                                        try {
                                            Access.getInstance().getInvoke().invokeMethod(finalInst,handler,args);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public String usage() {
                                    return command.usage();
                                }
                            };

                            for (Method method : clazz.getDeclaredMethods()) {
                                if(method.isAnnotationPresent(Command.Handler.class)){
                                    if(method.getParameters().length > 0){
                                        if(method.getParameters()[0].getType() == String[].class){
                                            handle.getHandlers().add(method);
                                        }else {
                                            System.out.println("注册指令 >> " + clazz.getName());
                                        }
                                    }else {
                                        System.out.println("注册指令 >> " + clazz.getName());
                                    }
                                }
                            }

                            if(handle.getHandlers().size() > 0){
                                Access.getInstance().getCommandManager().register(handle,command.value());
                            }
                        }
                        System.out.println("注册指令 >> " + clazz.getName());
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
