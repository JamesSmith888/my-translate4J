package com.jim.mytranslate4j;

import javafx.application.Platform;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyListener implements NativeKeyListener {

    private ScreenCapture screenCapture;

    public GlobalKeyListener(ScreenCapture screenCapture) {
        this.screenCapture = screenCapture;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));
        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_S && (nativeKeyEvent.getModifiers() & NativeKeyEvent.ALT_MASK) != 0) {
            // Show the screen capture overlay
            Platform.runLater(() -> {
                screenCapture.showOverlay();
            });
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    }
}

