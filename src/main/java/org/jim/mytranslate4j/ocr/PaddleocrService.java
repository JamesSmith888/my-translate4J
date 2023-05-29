package org.jim.mytranslate4j.ocr;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<?> data = restTemplate.getForObject("http://localhost:5000/ocr/" + imagePath, List.class);
        log.info("ocr result: {}", data);
        if (CollectionUtils.isEmpty(data)) {
            return "";
        }

        return ((ArrayList) data.get(0)).stream()
                .map(m -> {
                    List<List<?>> l = (List<List<?>>) m;
                    return l.get(1).get(0);
                })
                .collect(Collectors.joining("\n")).toString();
    }

}
