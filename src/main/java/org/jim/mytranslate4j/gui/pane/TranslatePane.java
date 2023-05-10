package org.jim.mytranslate4j.gui.pane;

import org.jim.mytranslate4j.gui.JavaFxComponent;
import org.jim.mytranslate4j.util.SpringContextUtils;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;

/**
 * @author jim
 * 基于实现类的数量，绘制翻译结果的面板
 */
public interface TranslatePane {


    default TitledPane pane() {
        JavaFxComponent javaFxComponent = SpringContextUtils.getBean(JavaFxComponent.class);

        TextArea textArea = new TextArea();
        TitledPane titledPane = new TitledPane(title(), textArea);
        // 标题图标
        titledPane.setGraphic(javaFxComponent.graphicImg(path()));
        // 隐藏标题栏
        titledPane.setCollapsible(false);

        setTextArea(textArea);
        return titledPane;
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
