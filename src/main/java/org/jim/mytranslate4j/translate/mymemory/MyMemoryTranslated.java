package org.jim.mytranslate4j.translate.mymemory;

import org.jim.mytranslate4j.enums.TranslateType;
import org.jim.mytranslate4j.event.UpdateTextAreaEvent;
import org.jim.mytranslate4j.translate.Translate;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author jim
 */
@Component
public class MyMemoryTranslated implements Translate {


    @Resource
    private MyMemoryTranslatedClient myMemoryTranslatedClient;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @SneakyThrows
    @Override
    public String translate(String content) {
        Mono<MyMemory> myMemoryMono = myMemoryTranslatedClient.translate(content);

        MyMemory myMemory = myMemoryMono.block(); // This will wait for the response

        if (myMemory == null) {
            return "翻译失败";
        }

        return myMemory.getResponseData().getTranslatedText();
    }

    @Override
    public void updateTranslateResult(String result) {
        applicationEventPublisher.publishEvent(new UpdateTextAreaEvent(this, TranslateType.My_Memory, result));
    }
}
