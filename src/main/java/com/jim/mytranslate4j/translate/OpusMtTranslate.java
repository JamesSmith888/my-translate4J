package com.jim.mytranslate4j.translate;

import com.jim.mytranslate4j.enums.TranslateType;
import com.jim.mytranslate4j.event.UpdateTextAreaEvent;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * @author jim
 */
@Component
public class OpusMtTranslate implements Translate {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String translate(String content) {
        try {
            String scriptPath = Paths.get("python/translate/opus_mt_en_zh_translate.py").toString();
            String modelPath = Paths.get("opus-mt-en-zh-local").toAbsolutePath().toString();
            ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath, content, modelPath);
            Process process = processBuilder.start();

            BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));

            String line;

            StringBuilder output = new StringBuilder();
            while ((line = outputReader.readLine()) != null) {
                output.append(line);
            }

            while ((line = errorReader.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor();
            return output.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTranslateResult(String result) {
        applicationEventPublisher.publishEvent(new UpdateTextAreaEvent(this, TranslateType.Opus_mt_en_zh, result));

    }
}
