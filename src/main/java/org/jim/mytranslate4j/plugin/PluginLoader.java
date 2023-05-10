package org.jim.mytranslate4j.plugin;

import lombok.extern.slf4j.Slf4j;
import org.jim.TranslatePlugin;
import org.springframework.stereotype.Component;

import java.util.ServiceLoader;

/**
 * @author jim
 * TODO 实现插件安全加载
 */
@Component
@Slf4j
public class PluginLoader {

    private ServiceLoader<TranslatePlugin> loader;

    public void loaderPlugin() {
        // 判断是否已经加载过
        if (loader != null) {
            return;
        }

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

