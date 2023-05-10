package org.jim.mytranslate4j.gui.pane;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import org.apache.commons.lang3.StringUtils;
import org.jim.mytranslate4j.gui.JavaFxComponent;
import org.jim.mytranslate4j.util.SpringContextUtils;

import java.util.function.Consumer;

/**
 * @author jim
 * 基于实现类的数量，绘制翻译结果的面板
 */
public interface TranslatePane {


    default TitledPane pane() {
        TitledPane titledPane = buildPane(title(), path(), this::setTextArea);

        return titledPane;
    }

    /**
     * 创建翻译结果面板
     *
     * @param title 面板左上角标题
     * @param path  面板左上角图标
     */
    static TitledPane buildPane(String title, String path, Consumer<TextArea> consumer) {
        JavaFxComponent javaFxComponent = SpringContextUtils.getBean(JavaFxComponent.class);

        TextArea textArea = new TextArea();
        TitledPane titledPane = new TitledPane(title, textArea);
        // 标题图标
        if (StringUtils.isNoneBlank(path)) {
            titledPane.setGraphic(javaFxComponent.graphicImg("/img/" + path));
        }
        // 隐藏标题栏
        titledPane.setCollapsible(false);


        // 目前只是为了setTextArea(textArea);
        consumer.accept(textArea);

        return titledPane;
    }

    static TitledPane buildPane(String title, String path) {
        return buildPane(title, path, textArea -> {
        });
    }

    default String title() {
        return "";
    }

    String path();

    TextArea textArea();

    /**
     * 更新文本框
     */
    default void updateTextArea(String text) {
        Platform.runLater(() -> textArea().setText(text));
    }

    void setTextArea(TextArea textArea);

    /**
     * 清空文本框
     */
    default void clear() {
        Platform.runLater(() -> textArea().clear());
    }
}
