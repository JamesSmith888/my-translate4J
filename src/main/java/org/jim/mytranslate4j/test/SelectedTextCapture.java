package org.jim.mytranslate4j.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SelectedTextCapture {

    public static void main(String[] args) {
        try {
            System.out.println(getSelectedText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSelectedText() throws IOException {
        String selectedText = "";

        // 替换下面的路径为你的PowerShell脚本实际路径
        String scriptPath = "src/main/resources/get_selected_text.ps1";

        ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-ExecutionPolicy", "Bypass", "-File", scriptPath);
        Process process = processBuilder.start();

        // 设置 InputStreamReader 的编码为 UTF-8
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        selectedText = reader.readLine();

        return selectedText;
    }
}
