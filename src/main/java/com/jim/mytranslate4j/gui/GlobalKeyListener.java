package com.jim.mytranslate4j.gui;

import com.jim.mytranslate4j.event.ShowOverlayEvent;
import jakarta.annotation.Resource;
import javafx.application.Platform;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author jim
 */
@Component
public class GlobalKeyListener implements NativeKeyListener {


    @Resource
    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));
        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_S && (nativeKeyEvent.getModifiers() & NativeKeyEvent.ALT_MASK) != 0) {
            // Show the screen capture overlay
            Platform.runLater(() -> applicationEventPublisher.publishEvent(new ShowOverlayEvent(this)));
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    }
}

