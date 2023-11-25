package org.jim.mytranslate4j.ocr;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author jim
 */
@Component
@Slf4j
public class PaddleocrService implements OcrService {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public String doOcr(String imagePath) {
        // 调用python restful接口进行ocr识别
        List<String> data = restTemplate.getForObject("http://localhost:5000/ocr", List.class);
        log.info("ocr result: {}", data);
        if (CollectionUtils.isEmpty(data)) {
            return "";
        }

        return String.join("\n", data);
    }

}
