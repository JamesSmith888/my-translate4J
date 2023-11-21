package org.jim.mytranslate4j.extension;

import jakarta.annotation.Resource;
import javafx.scene.control.TitledPane;
import org.jim.TranslatePlugin;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jim
 */
@Component
public class ExtensionService {

    @Resource
    private ExtensionLoader extensionLoader;


    private ServiceLoader<TranslatePlugin> getPlugin() {
        return extensionLoader.plugins();
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
                    TranslateExtensionAdapter translateExtensionAdapter = new TranslateExtensionAdapter(plugin);
                    // 初始化插件面板对象
                    translateExtensionAdapter.setExtensionGui(new ExtensionGui());

                    // 插件适配器对象缓存起来，方便后面使用
                    TranslateExtensionAdapter.TRANSLATE_PLUGINS.put(plugin, translateExtensionAdapter);

                    // 初始化并返回插件面板
                    return translateExtensionAdapter.initPluginPane();
                }).toList();
    }


    /**
     * 翻译并且显示
     */
    public void translateAndShow(String newValue) {
        List<TranslateExtensionAdapter> pluginAdapters = getPluginAdapters();
        if (CollectionUtils.isEmpty(pluginAdapters)) {
            return;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(pluginAdapters.size());

        pluginAdapters.forEach(pluginAdapter -> {
            executorService.submit(() -> {
                ExtensionGui extensionGui = pluginAdapter.getExtensionGui();

                extensionGui.updateTextArea("翻译中...");
                extensionGui.updateTextArea(pluginAdapter.translate(newValue));
            });

        });

    }

    /**
     * 获取插件adapter
     */
    public TranslateExtensionAdapter getPluginAdapter(TranslatePlugin plugin) {
        return TranslateExtensionAdapter.TRANSLATE_PLUGINS.get(plugin);
    }

    /**
     * 获取插件adapters
     */
    public List<TranslateExtensionAdapter> getPluginAdapters() {
        return List.copyOf(TranslateExtensionAdapter.TRANSLATE_PLUGINS.values());
    }

    /**
     * 判断插件是否为空
     */
    public boolean isEmpty() {
        return getPlugin() == null;
    }

}
