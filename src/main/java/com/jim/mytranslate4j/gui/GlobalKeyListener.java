package com.jim.mytranslate4j.gui;

import com.jim.mytranslate4j.event.SelectedTextCaptureEvent;
import com.jim.mytranslate4j.event.ShowOverlayEvent;
import jakarta.annotation.Resource;
import javafx.application.Platform;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author jim
 */
@Component
public class GlobalKeyListener implements NativeKeyListener {


    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    public static String getSelectedText() throws IOException {
        System.out.println("start time: " + System.currentTimeMillis());

        // 替换下面的路径为你的PowerShell脚本实际路径
        String scriptPath = "src/main/resources/get_selected_text.ps1";

        ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-ExecutionPolicy", "Bypass", "-File", scriptPath);
        System.out.println("start time 2: " + System.currentTimeMillis());
        Process process = processBuilder.start();
        System.out.println("start time 3: " + System.currentTimeMillis());

        // 设置 InputStreamReader 的编码为 UTF-8
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        System.out.println("start time 4: " + System.currentTimeMillis());
        String s = reader.readLine();
        System.out.println("end time: " + System.currentTimeMillis());
        System.out.println("all time: " + (System.currentTimeMillis() - System.currentTimeMillis()));
        return s;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_S && (nativeKeyEvent.getModifiers() & NativeKeyEvent.ALT_MASK) != 0) {
            // Show the screen capture overlay
            applicationEventPublisher.publishEvent(new ShowOverlayEvent(this));
        }

        // 判断是否按下alt+z
        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_Z && (nativeKeyEvent.getModifiers() & NativeKeyEvent.ALT_MASK) != 0) {


            Platform.runLater(() -> {
                try {
                    applicationEventPublisher.publishEvent(new SelectedTextCaptureEvent(this, getSelectedText()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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

