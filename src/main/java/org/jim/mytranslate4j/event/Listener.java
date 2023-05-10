package org.jim.mytranslate4j.event;

import org.jim.mytranslate4j.gui.ScreenCapture;
import org.jim.mytranslate4j.gui.Start;
import org.jim.mytranslate4j.ocr.OcrService;
import jakarta.annotation.Resource;
import javafx.application.Platform;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

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


    @EventListener
    @Async
    public void onApplicationEvent(ScreenCaptureEvent event) {
        start.showWindow();

        String ocr = ocrService.doOcr();

        // 显示OCR结果
        start.updateTextArea(ocr);
    }


    @EventListener
    @Async
    public void onApplicationEvent(ShowOverlayEvent event) {
        // 展示截屏窗口
        Platform.runLater(() -> screenCapture.showOverlay());
    }


    @EventListener
    public void onApplicationEvent(SelectedTextCaptureEvent event) {
        String selectedText = event.getSelectedText();

        // 更新UI以显示OCR结果
        start.showWindow();
        start.updateTextArea(selectedText);

    }


    @EventListener
    @Async
    public void onApplicationEvent(UpdateTextAreaEvent event) {
        Consumer<String> consumer = event.getTranslateType().getConsumer();

        // 更新UI并显示结果
        consumer.accept(event.getTranslatedText());
    }

}
