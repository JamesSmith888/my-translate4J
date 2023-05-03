package com.jim.mytranslate4j.event;

import com.jim.mytranslate4j.gui.ScreenCapture;
import com.jim.mytranslate4j.ocr.OcrService;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author jim
 */
@Component
public class Listener {

    @Resource
    private OcrService ocrService;

    @Resource
    private ScreenCapture screenCapture;

    @EventListener
    @Async
    public void onApplicationEvent(ScreenCaptureEvent event) {

        String ocr = ocrService.doOcr();
        System.out.println("ocr = " + ocr);

    }


    @EventListener
    @Async
    public void onApplicationEvent(ShowOverlayEvent event) {
        System.out.println("ShowOverlayEvent");
        screenCapture.showOverlay();
    }
}
