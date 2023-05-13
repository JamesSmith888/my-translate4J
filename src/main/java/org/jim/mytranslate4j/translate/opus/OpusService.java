package org.jim.mytranslate4j.translate.opus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * @author jim
 */
@Component
@Slf4j
public class OpusService {

    public String translate(String content) {
        try {
            Socket socket = OpusSocket.getSocket();
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            // 发送翻译内容(需要加上\n，否则python脚本无法读取)
            out.println(content + "\n");
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 启动opus_mt_en_zh_translate.py服务，建立socket连接
     */
    public void init() throws IOException {
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
                throw new RuntimeException(e);
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
}
