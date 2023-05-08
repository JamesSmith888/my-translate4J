package com.jim.mytranslate4j.gui.pane;

import javafx.scene.control.TextArea;
import org.springframework.stereotype.Component;

/**
 * @author jim
 */
@Component
public class MyMemoryTranslatePane implements TranslatePane {
    private static TextArea textArea;

    @Override
    public String title() {
        return "MyMemoryTranslate";
    }

    @Override
    public String path() {
        return "/img/myMemory.jpg";
    }

    @Override
    public TextArea textArea() {
        if (MyMemoryTranslatePane.textArea == null) {
            throw new RuntimeException("MyMemoryTranslatePane.textArea is null");
        }

        return MyMemoryTranslatePane.textArea;

    }

    @Override
    public void setTextArea(TextArea textArea) {
        if (MyMemoryTranslatePane.textArea != null) {
            return;
        }

        MyMemoryTranslatePane.textArea = textArea;
    }
}
