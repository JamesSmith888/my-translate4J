package org.jim.mytranslate4j.plugin;

import jakarta.annotation.Resource;
import javafx.scene.control.TitledPane;
import org.jim.TranslatePlugin;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jim
 */
@Component
public class PluginService {

    @Resource
    private PluginLoader pluginLoader;


    private ServiceLoader<TranslatePlugin> getPlugin() {
        return pluginLoader.plugins();
    }

    /**
     * 初始化插件翻译面板，并且构造缓存插件适配器对象
     */
    public List<TitledPane> initPluginPane() {
        ServiceLoader<TranslatePlugin> plugins = getPlugin();
        if (plugins == null) {
            return null;
        }

        return plugins.stream()
                .map(ServiceLoader.Provider::get)
                .map(plugin -> {
                    TranslatePluginAdapter translatePluginAdapter = new TranslatePluginAdapter(plugin);
                    // 初始化插件面板对象
                    translatePluginAdapter.setPluginGui(new PluginGui());

                    // 插件适配器对象缓存起来，方便后面使用
                    TranslatePluginAdapter.TRANSLATE_PLUGINS.put(plugin, translatePluginAdapter);

                    // 初始化并返回插件面板
                    return translatePluginAdapter.initPluginPane();
                }).toList();
    }


    /**
     * 翻译并且显示
     */
    public void translateAndShow(String newValue) {
        List<TranslatePluginAdapter> pluginAdapters = getPluginAdapters();
        if (pluginAdapters == null) {
            return;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(pluginAdapters.size());

        pluginAdapters.forEach(pluginAdapter -> {
            executorService.submit(() -> {
                PluginGui pluginGui = pluginAdapter.getPluginGui();

                pluginGui.updateTextArea("翻译中...");
                pluginGui.updateTextArea(pluginAdapter.translate(newValue));
            });

        });

    }

    /**
     * 获取插件adapter
     */
    public TranslatePluginAdapter getPluginAdapter(TranslatePlugin plugin) {
        return TranslatePluginAdapter.TRANSLATE_PLUGINS.get(plugin);
    }

    /**
     * 获取插件adapters
     */
    public List<TranslatePluginAdapter> getPluginAdapters() {
        return List.copyOf(TranslatePluginAdapter.TRANSLATE_PLUGINS.values());
    }

    /**
     * 判断插件是否为空
     */
    public boolean isEmpty() {
        return getPlugin() == null;
    }

}
