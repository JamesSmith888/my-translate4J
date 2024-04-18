package org.jim.mytranslate4j.ai.imagecaptioning;

import jakarta.annotation.Resource;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author jim
 */
@Component
@Slf4j
public class ImageCaptioningService {

    @Resource
    private RestTemplate restTemplate;

    public void init() throws IOException {
        String scriptPath = Paths.get("python/translate/blip-image-captioning-base.py").toString();
        String modelPath = Paths.get("blip-image-captioning-base").toAbsolutePath().toString();
        ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath, modelPath);
        Map<String, String> environment = processBuilder.environment();
        environment.put("PYTHONIOENCODING", "utf-8");


        Process process = processBuilder.start();

        log.info("ImageCaptioningService starting...");

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

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        // 使用单独的线程读取标准输出
        new Thread(() -> {
            String line;
            try {
                while ((line = inputReader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).start();

    }


    public String processor(String captioning) {
        var requestBody = """
                {
                    "text": "%s"
                }
                """.formatted(captioning);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        var request = new HttpEntity<>(requestBody, headers);

        String path = System.getProperty("user.dir") + "/temp/screenshot.png";

        var response = restTemplate.postForEntity("http://localhost:5000/process_image/" + path, request, String.class);
        log.info("response: {}", response);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }


        throw new RuntimeException("ImageCaptioningService error");
    }


    public boolean serverStatus() {
        try {
            var response = restTemplate.getForEntity("http://localhost:5000/status", String.class);
            log.info("serverStatus response: {}", response);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("ImageCaptioningService error", e);
            return false;
        }
    }

    public String getImageDescription(Image image, String prompt) {
        // 图片保存到temp/screenshot.png
        File file = new File("temp/screenshot.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return processor(prompt);
    }
}
