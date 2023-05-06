package com.jim.mytranslate4j.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class JavaPythonBridge {
    public static void main(String[] args) {
        try {
            String inputText = "Hello, world!";
            String scriptPath = Paths.get("python/translate/opus_mt_en_zh_translate.py").toString();
            String modelPath = Paths.get("opus-mt-en-zh-local").toAbsolutePath().toString();
            ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath, inputText, modelPath);
            Process process = processBuilder.start();

            BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));

            String line;

            System.out.println("Output:");
            while ((line = outputReader.readLine()) != null) {
                System.out.println(line);
            }

            System.out.println("Error:");
            while ((line = errorReader.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
