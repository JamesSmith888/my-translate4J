package org.jim.mytranslate4j.translate.opus;

import org.jim.mytranslate4j.enums.TranslateType;
import org.jim.mytranslate4j.event.UpdateTextAreaEvent;
import org.jim.mytranslate4j.translate.Translate;
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
    private OpusService opusService;

    @Override
    public String translate(String content) {
        return opusService.translate(content);
    }

    @Override
    public void updateTranslateResult(String result) {
        applicationEventPublisher.publishEvent(new UpdateTextAreaEvent(this, TranslateType.Opus_mt_en_zh, result));

    }
}
