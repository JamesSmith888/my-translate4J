package com.jim.mytranslate4j.translate.baidu;

import com.jim.mytranslate4j.enums.TranslateType;
import com.jim.mytranslate4j.event.UpdateTextAreaEvent;
import com.jim.mytranslate4j.translate.Translate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author jim
 */
@Component
@Slf4j
public class BaiduTranslate implements Translate {

    private static final String API_URL = "https://api.fanyi.baidu.com/api/trans/vip/translate";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private TransApi transApi;

    @Override
    public String translate(String content) {
        return transApi.getTranslationValue(content);
    }

    @Override
    public void updateTranslateResult(String result) {
        applicationEventPublisher.publishEvent(new UpdateTextAreaEvent(this, TranslateType.Baidu, result));
    }
}
