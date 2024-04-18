package org.jim.mytranslate4j.extension;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jim.TranslatePlugin;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

/**
 * @author jim
 * TODO 实现插件安全加载
 */
@Component
@Slf4j
public class ExtensionLoader {


    private final List<TranslatePlugin> plugins = Lists.newArrayList();

    public void loaderPlugin() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        // TODO 判断是否已经加载过


        log.info("PluginLoader load plugin");
        ExtensionClassLoader extensionClassLoader = new ExtensionClassLoader();
        log.info("extensionClassLoader: {}", extensionClassLoader);



        List<String> classNames = ExtensionClassLoader.classMap.values().stream().flatMap(Collection::stream).toList();

        for (String className : classNames) {
            Class<?> clazz = extensionClassLoader.loadClass(className);
            if (!TranslatePlugin.class.isAssignableFrom(clazz)) {
                continue;
            }

            TranslatePlugin plugin = (TranslatePlugin) clazz.getDeclaredConstructor().newInstance();
            plugins.add(plugin);
        }


        checkPlugin();

        log.info("PluginLoader load plugin success");
    }

    /**
     * 检查加载的插件是否安全
     */
    public void checkPlugin() {
        // TODO
    }

    protected List<TranslatePlugin> plugins() {
        return plugins;
    }

}

