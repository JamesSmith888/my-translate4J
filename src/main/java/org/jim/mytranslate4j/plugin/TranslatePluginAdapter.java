package org.jim.mytranslate4j.plugin;

import javafx.scene.control.TitledPane;
import lombok.Data;
import org.jim.TranslatePlugin;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jim
 */
@Data
public class TranslatePluginAdapter {

    /**
     * 缓存插件适配器对象
     */
    public static final ConcurrentHashMap<TranslatePlugin, TranslatePluginAdapter> TRANSLATE_PLUGINS = new ConcurrentHashMap<>();

    /**
     * 当前插件
     */
    private TranslatePlugin plugin;

    /**
     * PluginGui
     */
    private PluginGui pluginGui;


    public String translate(String text) {
        return plugin.translate(text);
    }

    public TranslatePluginAdapter(TranslatePlugin plugin, PluginGui pluginGui) {
        this.plugin = plugin;
        this.pluginGui = pluginGui;
    }

    public TranslatePluginAdapter(TranslatePlugin plugin) {
        this.plugin = plugin;
    }

    public TitledPane initPluginPane() {

        return pluginGui.initPluginPane(getPlugin());
    }


}
