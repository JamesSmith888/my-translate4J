package com.jim.mytranslate4j.gui.pane;

import javafx.scene.control.TextArea;
import org.springframework.stereotype.Component;

/**
 * @author jim
 */
@Component
public class ChatGptTranslatePane implements TranslatePane {
    private static TextArea textArea;


    @Override
    public String path() {
        return "/img/openai.jpg";
    }

    @Override
    public TextArea textArea() {
        if (ChatGptTranslatePane.textArea == null) {
            throw new RuntimeException("ChatGptTranslatePane.textArea is null");
        }

        return ChatGptTranslatePane.textArea;

    }

    @Override
    public void setTextArea(TextArea textArea) {
        if (ChatGptTranslatePane.textArea != null) {
            return;
        }

        ChatGptTranslatePane.textArea = textArea;
    }
}
