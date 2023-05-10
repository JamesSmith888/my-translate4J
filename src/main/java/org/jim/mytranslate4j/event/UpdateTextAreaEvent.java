package org.jim.mytranslate4j.event;

import org.jim.mytranslate4j.enums.TranslateType;
import org.springframework.context.ApplicationEvent;

/**
 * @author jim
 */
public class UpdateTextAreaEvent extends ApplicationEvent {
    private TranslateType translateType;

    private String translatedText;

    public UpdateTextAreaEvent(Object source, TranslateType translateType, String translatedText) {
        super(source);
        this.translateType = translateType;
        this.translatedText = translatedText;
    }

    public TranslateType getTranslateType() {
        return translateType;
    }

    public void setTranslateType(TranslateType translateType) {
        this.translateType = translateType;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }
}
