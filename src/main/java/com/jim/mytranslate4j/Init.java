package com.jim.mytranslate4j;

import com.jim.mytranslate4j.translate.opus.OpusSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * @author jim
 * 容器启动时初始化
 */
@Component
@Slf4j
public class Init implements CommandLineRunner {

    /**
     * 启动opus_mt_en_zh_translate.py服务，建立socket连接
     */
    public void startOpusMtEnZhTranslateService() throws IOException {
        File serverReadyFile = new File("server_ready.txt");
        // 判断是否存在server_ready.txt文件，如果存在则先删除该文件
        if (serverReadyFile.exists()) {
            serverReadyFile.delete();
        }


        String scriptPath = Paths.get("python/translate/opus_mt_en_zh_translate.py").toString();
        String modelPath = Paths.get("opus-mt-en-zh-local").toAbsolutePath().toString();
        ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath, modelPath);
        Process process = processBuilder.start();
        log.info("OpusMtEnZhTranslateService starting...");

        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));

        // 使用单独的线程读取错误输出
        new Thread(() -> {
            String line;
            try {
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

        // 使用单独的线程读取Python脚本的输出
        new Thread(() -> {
            String line;
            try {
                while ((line = outputReader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        log.info("OpusMtEnZhTranslateService started successfully");


        // client 建立socket连接
        int maxRetries = 10;
        int retries = 0;
        while (!serverReadyFile.exists() && retries < maxRetries) {
            try {
                Thread.sleep(3000); // Wait for 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retries++;
        }

        if (!serverReadyFile.exists()) {
            throw new RuntimeException("OpusMtEnZhTranslateService start failed");
        }


        OpusSocket.setSocket(new Socket("localhost", 12345));
        log.info("OpusMtEnZhTranslateService socket connected");
    }


    @Override
    public void run(String... args) throws Exception {
        startOpusMtEnZhTranslateService();
    }
}
