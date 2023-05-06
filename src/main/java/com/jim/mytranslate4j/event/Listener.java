package com.jim.mytranslate4j.event;

import com.jim.mytranslate4j.gui.ScreenCapture;
import com.jim.mytranslate4j.gui.Start;
import com.jim.mytranslate4j.ocr.OcrService;
import com.jim.mytranslate4j.translate.Translate;
import jakarta.annotation.Resource;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author jim
 */
@Component
public class Listener {

    @Resource
    private OcrService ocrService;

    @Resource
    private ScreenCapture screenCapture;

    @Resource
    private Start start;

    @Autowired
    private List<Translate> translates;

    @EventListener
    @Async
    public void onApplicationEvent(ScreenCaptureEvent event) {
        Platform.runLater(() -> start.showWindow());

        String ocr = ocrService.doOcr();

        // 显示OCR结果
        Platform.runLater(() -> start.updateTextArea(ocr));


        // 翻译
/*        translates.parallelStream().forEach(translate -> {
            System.out.println("开始翻译...");
            translate.updateTranslateResult("翻译中...");

            String translatedText = translate.translate(ocr);
            Platform.runLater(() -> translate.updateTranslateResult(translatedText));
        });*/
    }


    @EventListener
    @Async
    public void onApplicationEvent(ShowOverlayEvent event) {
        // 展示截屏窗口
        Platform.runLater(() -> screenCapture.showOverlay());
    }


    @EventListener
    public void onApplicationEvent(SelectedTextCaptureEvent event) {
        // 展示翻译窗口
        start.showWindow();

        String selectedText = event.getSelectedText();

        // 更新UI以显示OCR结果
        Platform.runLater(() -> start.updateTextArea(selectedText));
        // 翻译
        translates.parallelStream().forEach(translate -> {
            translate.updateTranslateResult("翻译中...");
            String translatedText = translate.translate(selectedText);
            Platform.runLater(() -> translate.updateTranslateResult(translatedText));
        });

    }


    @EventListener
    @Async
    public void onApplicationEvent(UpdateTextAreaEvent event) {
        BiConsumer<Start, String> consumer = event.getTranslateType().getConsumer();

        // 更新UI并显示结果
        consumer.accept(start, event.getTranslatedText());
    }

}
