package com.jim.mytranslate4j.translate;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TranslateService {

    @Resource
    private List<Translate> translates;

    // 创建一个固定大小的线程池
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    /**
     * 翻译并更新界面的翻译结果文本
     */
    public void translateAndUpdate(String content) {
        // 为每个翻译任务创建一个 CompletableFuture
        translates.forEach(translate -> {
            executorService.submit(() -> {
                translate.updateTranslateResult("翻译中...");
                String translatedText = translate.translate(content);
                translate.updateTranslateResult(translatedText);
            });
        });

    }
}
