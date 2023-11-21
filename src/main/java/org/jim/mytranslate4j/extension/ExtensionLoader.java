package org.jim.mytranslate4j.extension;

import lombok.extern.slf4j.Slf4j;
import org.jim.TranslatePlugin;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;

/**
 * @author jim
 * TODO 实现插件安全加载
 */
@Component
@Slf4j
public class ExtensionLoader {

    private ServiceLoader<TranslatePlugin> loader;

    public void loaderPlugin() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 判断是否已经加载过
        if (loader != null) {
            return;
        }

        ExtensionClassLoader extensionClassLoader = new ExtensionClassLoader();


        Class<?> pluginClass = extensionClassLoader.loadClass("org.jim.TestTranslatePlugin");
        TranslatePlugin plugin = (TranslatePlugin)pluginClass.getDeclaredConstructor().newInstance();
        String test = plugin.translate("test");
        System.out.println(test);


        loader = ServiceLoader.load(TranslatePlugin.class);

        checkPlugin();


        log.info("PluginLoader load plugin success");
    }

    /**
     * 检查加载的插件是否安全
     */
    public void checkPlugin() {
        // TODO
    }

    protected ServiceLoader<TranslatePlugin> plugins() {
        return loader;
    }

}

