package com.jim.mytranslate4j.translate;

import jakarta.annotation.Resource;
import javafx.application.Platform;
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
            Platform.runLater(() -> translate.updateTranslateResult("翻译中..."));
            String translatedText = translate.translate(content);
            Platform.runLater(() -> translate.updateTranslateResult(translatedText));
        });
    }
}
