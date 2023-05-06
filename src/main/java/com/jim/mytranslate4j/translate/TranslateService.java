package com.jim.mytranslate4j.translate;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jim
 */
@Component
public class TranslateService {

    @Resource
    private List<Translate> translates;

    /**
     * 翻译并更新界面的翻译结果文本
     */
    public void translateAndUpdate(String content) {
        translates.parallelStream().forEach(translate -> {
            translate.updateTranslateResult("翻译中...");
            String translatedText = translate.translate(content);
            translate.updateTranslateResult(translatedText);
        });
    }
}
