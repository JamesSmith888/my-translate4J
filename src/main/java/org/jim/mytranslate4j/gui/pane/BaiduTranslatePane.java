package org.jim.mytranslate4j.gui.pane;

import javafx.scene.control.TextArea;
import org.springframework.stereotype.Component;

/**
 * @author jim
 */
@Component
public class BaiduTranslatePane implements TranslatePane {

    private static TextArea textArea;


    @Override
    public String path() {
        return "baidu.png";
    }

    @Override
    public TextArea textArea() {
        if (BaiduTranslatePane.textArea == null) {
            throw new RuntimeException("BaiduTranslatePane.textArea is null");
        }

        return BaiduTranslatePane.textArea;
    }

    @Override
    public void setTextArea(TextArea textArea) {
        if (BaiduTranslatePane.textArea != null) {
            return;
        }

        BaiduTranslatePane.textArea = textArea;
    }


}
