package org.jim.mytranslate4j.event;

import jakarta.annotation.Resource;
import javafx.application.Platform;
import org.jim.mytranslate4j.ai.imagecaptioning.ImageCaptioningService;
import org.jim.mytranslate4j.gui.ScreenCapture;
import org.jim.mytranslate4j.gui.Start;
import org.jim.mytranslate4j.ocr.OcrService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author jim
 */
@Component
public class Listener {

    @Resource(name = "paddleocrService")
    private OcrService ocrService;

    @Resource
    private ScreenCapture screenCapture;

    @Resource
    private Start start;

    @Resource
    private ImageCaptioningService imageCaptioningService;


    @EventListener
    @Async
    public void onApplicationEvent(ScreenCaptureEvent event) {
        start.showWindow();


        if (event.getScreenCapture() == org.jim.mytranslate4j.common.ScreenCapture.TRANSLATE) {
            String ocr = ocrService.doOcr();

            // 显示OCR结果
            start.updateTextAreaAndTranslate(ocr);
            return;
        }


        // 图片回显到面板
        start.setOriginalImageView();

    }


    @EventListener
    @Async
    public void onApplicationEvent(ShowOverlayEvent event) {
        // 展示截屏窗口
        Platform.runLater(() -> screenCapture.showOverlay(event.getScreenCapture()));
    }


    @EventListener
    public void onApplicationEvent(SelectedTextCaptureEvent event) {
        String selectedText = event.getSelectedText();

        // 更新UI以显示OCR结果
        start.showWindow();
        start.updateTextAreaAndTranslate(selectedText);

    }


    @EventListener
    @Async
    public void onApplicationEvent(UpdateTextAreaEvent event) {
        Consumer<String> consumer = event.getTranslateType().getConsumer();

        // 更新UI并显示结果
        consumer.accept(event.getTranslatedText());
    }

}
