package com.jim.mytranslate4j.translate.opus;

import com.jim.mytranslate4j.enums.TranslateType;
import com.jim.mytranslate4j.event.UpdateTextAreaEvent;
import com.jim.mytranslate4j.translate.Translate;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author jim
 */
@Component
public class OpusMtTranslate implements Translate {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private Translator translator;

    @Override
    public String translate(String content) {
        return translator.translate(content);
    }

    @Override
    public void updateTranslateResult(String result) {
        applicationEventPublisher.publishEvent(new UpdateTextAreaEvent(this, TranslateType.Opus_mt_en_zh, result));

    }
}
