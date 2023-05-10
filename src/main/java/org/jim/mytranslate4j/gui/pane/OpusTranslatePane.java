package org.jim.mytranslate4j.gui.pane;

import javafx.scene.control.TextArea;
import org.springframework.stereotype.Component;

/**
 * @author jim
 */
@Component
public class OpusTranslatePane implements TranslatePane {
    private static TextArea textArea;

    @Override
    public String title() {
        return "opus-mt-en-zh";
    }

    @Override
    public String path() {
        return "opus_mt.jpg";
    }

    @Override
    public TextArea textArea() {
        if (OpusTranslatePane.textArea == null) {
            throw new RuntimeException("OpusTranslatePane.textArea is null");
        }

        return OpusTranslatePane.textArea;

    }

    @Override
    public void setTextArea(TextArea textArea) {
        if (OpusTranslatePane.textArea != null) {
            return;
        }

        OpusTranslatePane.textArea = textArea;
    }
}
