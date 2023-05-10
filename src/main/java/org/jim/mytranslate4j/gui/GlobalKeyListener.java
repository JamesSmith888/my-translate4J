package org.jim.mytranslate4j.gui;

import org.jim.mytranslate4j.event.SelectedTextCaptureEvent;
import org.jim.mytranslate4j.event.ShowOverlayEvent;
import jakarta.annotation.Resource;
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

    /**
     * 获取鼠标选中的文本
     */
    public static String getSelectedText() throws IOException {
        String scriptPath = "src/main/resources/get_selected_text.ps1";

        ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-ExecutionPolicy", "Bypass", "-File", scriptPath);
        Process process = processBuilder.start();

        // 设置 InputStreamReader 的编码为 UTF-8
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        String s = reader.readLine();
        return s;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        // 判断是否按下alt，如果没有按下alt，则不处理
        if ((nativeKeyEvent.getModifiers() & NativeKeyEvent.ALT_MASK) == 0) {
            return;
        }


        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_S) {
            // 发布显示遮罩层的事件
            applicationEventPublisher.publishEvent(new ShowOverlayEvent(this));
        }

        // 判断是否按下alt+z
        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_Z) {

            try {
                // 发布获取选中文本的事件
                applicationEventPublisher.publishEvent(new SelectedTextCaptureEvent(this, getSelectedText()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    }
}

