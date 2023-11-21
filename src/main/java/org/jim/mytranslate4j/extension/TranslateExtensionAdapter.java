package org.jim.mytranslate4j.extension;

import javafx.scene.control.TitledPane;
import lombok.Data;
import org.jim.TranslatePlugin;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jim
 */
@Data
public class TranslateExtensionAdapter {

    /**
     * 缓存插件适配器对象
     */
    public static final ConcurrentHashMap<TranslatePlugin, TranslateExtensionAdapter> TRANSLATE_PLUGINS = new ConcurrentHashMap<>();

    /**
     * 当前插件
     */
    private TranslatePlugin plugin;

    /**
     * PluginGui
     */
    private ExtensionGui extensionGui;


    public String translate(String text) {
        return plugin.translate(text);
    }

    public TranslateExtensionAdapter(TranslatePlugin plugin, ExtensionGui extensionGui) {
        this.plugin = plugin;
        this.extensionGui = extensionGui;
    }

    public TranslateExtensionAdapter(TranslatePlugin plugin) {
        this.plugin = plugin;
    }

    public TitledPane initPluginPane() {

        return extensionGui.initPluginPane(getPlugin());
    }


}
