package org.jim.mytranslate4j.extension;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import lombok.Data;
import org.jim.TranslatePlugin;
import org.jim.mytranslate4j.gui.pane.TranslatePane;

/**
 * @author jim
 */
@Data
public class ExtensionGui {


    /**
     * 当前插件的翻译结果文本框实例
     */
    private TextArea textArea;


    protected TitledPane initPluginPane(TranslatePlugin plugin) {


        return TranslatePane.buildPane(plugin.translatePaneTitle(), plugin.translatePaneIconPath(), this::setTextArea);
    }

    /**
     * 更新翻译结果文本框
     */
    public void updateTextArea(String text) {
        Platform.runLater(() -> getTextArea().setText(text));
    }


}
