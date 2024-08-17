package com.fpsboost.module;

import com.fpsboost.annotations.system.Init;
import com.fpsboost.events.misc.WorldLoadEvent;
import com.fpsboost.plugin.ClassLoaderUtil;
import com.google.gson.*;
import net.minecraft.util.EnumChatFormatting;
import com.fpsboost.Access;
import com.fpsboost.Initializer;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Binding;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.annotations.system.Startup;
import com.fpsboost.events.EventManager;
import com.fpsboost.events.misc.KeyInputEvent;
import com.fpsboost.module.handlers.ModuleHandle;
import com.fpsboost.module.handlers.SubModuleHandle;
import com.fpsboost.value.AbstractValue;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.ComboValue;
import com.fpsboost.value.impl.NumberValue;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Module Manager, Manage and access module.
 *
 * @author cubk
 */
public final class ModuleManager implements Initializer {
    /**
     * Index Maps
     */
    private final HashMap<Class<?>, ModuleHandle> modules = new HashMap<>();
    private final HashMap<String, Class<?>> nameMap = new HashMap<>();
    private final HashMap<Object, Class<?>> objectMap = new HashMap<>();

    /**
     * Initialize modules
     */
    public ModuleManager() {

    }

    public void init(){
        // Register Event
        EventManager.register(this);

        initialize(clazz -> {
            if(clazz.isAnnotationPresent(Module.class)){
                Module module = clazz.getAnnotation(Module.class);
                register(clazz,module.value(),module.category());
            }
        });
    }

    public void registerURLModule(String className,String classPackage) {
        Class<?> clazz = ClassLoaderUtil.load(className,classPackage);
        Module module = clazz.getAnnotation(Module.class);
        register(clazz,module.value(),module.category());
    }
    @EventTarget
    public void onKey(KeyInputEvent event) {
        for (Class<?> module : modules.keySet())
            if (getKey(module) == event.getKey())
                toggle(module);
    }

    /**
     * Register a module to manager
     *
     * @param clazz    Module Class
     * @param name     Name
     * @param category Module Category, see {@link Category}
     */
    public void register(Class<?> clazz, String name, Category category) {
        try {
            Object instance = Access.getInstance().getInvoke().createInstance(clazz);

            Access.getInstance().getInvoke().autoWired(instance);
            Access.getInstance().getInvoke().registerBean(instance);

            ModuleHandle module = new ModuleHandle(name, category, instance);

            nameMap.put(name.toLowerCase(), clazz);
            objectMap.put(instance, clazz);

            if (clazz.isAnnotationPresent(Startup.class)) {
                module.setEnable(true);
            }

            if (clazz.isAnnotationPresent(Binding.class)) {
                Binding binding = clazz.getAnnotation(Binding.class);
                module.setKey(binding.value());
            }

            for (final Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    final Object obj = field.get(instance);
                    if (obj instanceof AbstractValue<?>) {
                        module.getValues().add((AbstractValue<?>) obj);
                        sortValue(module);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Init.class)) {
                    try {
                        Access.getInstance().getInvoke().invokeMethod(clazz,method);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            this.modules.put(clazz, module);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Register sub module
     *
     * @param bigFather 大爹
     * @param clazz     SubModule Class
     * @param name      SubModule Name
     * @return {@link SubModuleHandle}
     */
    SubModuleHandle registerSub(ModuleHandle bigFather,Class<?> clazz, String name) {
        try {
            Object instance = clazz.newInstance();
            SubModuleHandle module = new SubModuleHandle(bigFather,name, instance);

            for (final Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    final Object obj = field.get(instance);
                    if (obj instanceof AbstractValue<?>) {
                        AbstractValue<?> value = (AbstractValue<?>) obj;
                        value.addSupplier(() -> !bigFather.isEnabled());
                        bigFather.getValues().add(value);
                        sortValue(bigFather);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            return module;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all modules in a category
     *
     * @param category Target Category
     * @return {@link List}<{@link Class}<{@link ?}>>
     */
    public List<Class<?>> getModulesByCategory(Category category) {
        ArrayList<Class<?>> mods = new ArrayList<>();
        for(Class<?> module : getModules()) {
            if(getCategory(module) == category)
                mods.add(module);
        }
        return mods;
    }

    public List<ModuleHandle> getCModulesByCategory(Category category) {
        ArrayList<ModuleHandle> mods = new ArrayList<>();
        for(ModuleHandle module : modules.values()) {
            if(module.getCategory() == category)
                mods.add(module);
        }
        return mods;
    }

    /**
     * Get module category
     *
     * @param module Module Class
     * @return {@link Category}
     */
    public Category getCategory(Class<?> module){
        return modules.get(module).getCategory();
    }

    /**
     * sort value
     *
     * @param mod module handle
     */
    private void sortValue(ModuleHandle mod) {
        ArrayList<AbstractValue<?>> sorted = new ArrayList<>();
        ArrayList<AbstractValue<?>> values = mod.getValues();

        for (AbstractValue<?> v : values) {
            if (v instanceof ComboValue)
                sorted.add(v);
        }
        for (AbstractValue<?> v : values) {
            if (v instanceof NumberValue)
                sorted.add(v);
        }

        for (AbstractValue<?> v : values) {
            if (v instanceof BooleanValue)
                sorted.add(v);
        }

        mod.setValues(sorted);
    }

    /**
     * Get a module class by name
     *
     * @param name Module Name
     * @return {@link Class}<{@link ?}>
     */
    public Class<?> getModuleClass(String name) {
        return nameMap.get(name.toLowerCase());
    }

    /**
     * Toggle module state
     *
     * @param module module class
     */
    public boolean toggle(Class<?> module) {
        modules.get(module).setEnable(!modules.get(module).isEnabled());
        return modules.get(module).isEnabled();
    }

    /**
     * Get a module name
     *
     * @param module Module Class
     * @return {@link String}
     */
    public String getName(Class<?> module) {
        return modules.get(module).getName();
    }


    /**
     * Get a module key bind
     *
     * @param module module class
     * @return key bind code
     */
    public int getKey(Class<?> module) {
        return modules.get(module).getKey();
    }

    /**
     * Set a module key bind
     *
     * @param module module class
     * @param key    key bind code
     */
    public void setKey(Class<?> module, int key) {
        modules.get(module).setKey(key);
    }

    /**
     * Get all module classes
     *
     * @return {@link Set}<{@link Class}<{@link ?}>>
     */
    public List<Class<?>> getModules() {
        return new ArrayList<>(modules.keySet());
    }
    public Collection<ModuleHandle> getCModules() {
        return modules.values();
    }

    /**
     * format module to array display (+suffix)
     *
     * @param module module class
     * @return {@link String}
     */
    public String format(Class<?> module) {
        ModuleHandle m = modules.get(module);
        return m.getSuffix().isEmpty() ? m.getName() : String.format("%s %s%s", m.getName(), EnumChatFormatting.GRAY, m.getSuffix());
    }

    /**
     * Get handle
     *
     * @param module module class
     * @return {@link ModuleHandle}
     */
    public ModuleHandle getHandle(Class<?> module) {
        return modules.get(module);
    }

    /**
     * Get handle
     *
     * @param object module object
     * @return {@link ModuleHandle}
     */
    public ModuleHandle getHandle(Object object) {
        return modules.get(objectMap.get(object));
    }

    /**
     * Get all values for module
     *
     * @param module Module Class
     * @return {@link Iterable}<{@link AbstractValue}<{@link ?}>>
     */
    public Iterable<AbstractValue<?>> getValues(Class<?> module) {
        return new ArrayList<>(modules.get(module).getValues());
    }

    /**
     * Module has values?
     *
     * @param module Module Class
     * @return Has
     */
    public boolean hasValue(Class<?> module) {
        return !modules.get(module).getValues().isEmpty();
    }

    /**
     * Get a module enable status
     *
     * @param module Module Class
     * @return Status
     */
    public boolean isEnabled(Class<?> module) {
        return modules.get(module).isEnabled();
    }

    /**
     * Get a module visible status
     *
     * @param module Module Class
     * @return Status
     */
    public boolean isVisible(Class<?> module) {
        return modules.get(module).isVisible();
    }

    /**
     * Set a module enable status
     *
     * @param module Module Class
     * @param state  Status
     */
    public void setEnable(Class<?> module, boolean state) {
        this.modules.get(module).setEnable(state);
    }

    /**
     * Set a module visible status
     *
     * @param module Module Class
     * @param state  Status
     */
    public void setVisible(Class<?> module, boolean state) {
        this.modules.get(module).setVisible(state);
    }
    private static final File MODULE_DATA = new File(Access.DIRECTORY, "module.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonObject save() {
        JsonObject object = new JsonObject();
        for (ModuleHandle module : modules.values()){
            JsonObject moduleObj = new JsonObject();
            moduleObj.addProperty("Enable",module.isEnabled());
            moduleObj.addProperty("KeyCode",module.getKey());
            JsonObject valueObj = new JsonObject();
            moduleObj.add("Values",valueObj);
            for (AbstractValue<?> value : module.getValues()){
                if (value instanceof NumberValue) {
                    valueObj.addProperty(value.getName(), ((NumberValue) value).getValue());
                } else if (value instanceof BooleanValue) {
                    valueObj.addProperty(value.getName(), ((BooleanValue) value).getValue());
                } else if (value instanceof ComboValue) {
                    valueObj.addProperty(value.getName(), ((ComboValue) value).getValue());
                }
            }
            object.add(module.getName(),moduleObj);
        }
        return object;
    }

    public void load(JsonObject object) {
        for (ModuleHandle module : modules.values()) {
            if (object.has(module.getName())) {
                JsonObject moduleObject = object.get(module.getName()).getAsJsonObject();
                if (moduleObject.has("Enable")) {
                    module.setEnable(moduleObject.get("Enable").getAsBoolean());
                }
                if (moduleObject.has("KeyCode")) {
                    module.setKey(moduleObject.get("KeyCode").getAsInt());
                }
                if (moduleObject.has("Values")) {
                    JsonObject valuesObject = moduleObject.get("Values").getAsJsonObject();
                    for (AbstractValue<?> value : module.getValues()) {
                        if (valuesObject.has(value.getName())) {
                            JsonElement theValue = valuesObject.get(value.getName());
                            if (value instanceof NumberValue) {
                                ((NumberValue) value).setValue(theValue.getAsNumber().doubleValue());
                            } else if (value instanceof BooleanValue) {
                                ((BooleanValue) value).setValue(theValue.getAsBoolean());
                            } else if (value instanceof ComboValue) {
                                ((ComboValue) value).setValue(theValue.getAsString());
                            }
                        }
                    }
                }
            }
        }
    }

    public void loadConfig(String name) {
        if (MODULE_DATA.exists()) {
            System.out.println("Loading config: " + name);
            try {
                load(new JsonParser().parse(new FileReader(MODULE_DATA)).getAsJsonObject());
            } catch (FileNotFoundException e) {
                System.out.println("Failed to load config: " + name);
                e.printStackTrace();
            }

        } else {
            System.out.println("Config " + name + " doesn't exist, creating a new one...");
            saveConfig("module");
        }
    }
    public void saveConfig(String name) {
        try {
            System.out.println("Saving config: " + name);
            MODULE_DATA.createNewFile();
            FileUtils.writeByteArrayToFile(MODULE_DATA, gson.toJson(save()).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Failed to save config: " + name);
        }
    }

    private boolean loaded;
    @EventTarget
    public void onWorldLoad(WorldLoadEvent event) {
        if (!loaded) {
            loadConfig("module");
            loaded = true;
        }
    }


}
