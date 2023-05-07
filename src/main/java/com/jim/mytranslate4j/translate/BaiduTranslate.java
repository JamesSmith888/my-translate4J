package com.jim.mytranslate4j.translate;

import com.jim.mytranslate4j.config.Config;
import com.jim.mytranslate4j.enums.TranslateType;
import com.jim.mytranslate4j.event.UpdateTextAreaEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * @author jim
 */
@Component
@Slf4j
public class BaiduTranslate implements Translate {

    private static final String API_URL = "https://api.fanyi.baidu.com/api/trans/vip/translate";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String translate(String content) {
        return translateText(content, "auto", "zh");
    }

    @Override
    public void updateTranslateResult(String result) {
        applicationEventPublisher.publishEvent(new UpdateTextAreaEvent(this, TranslateType.Baidu, result));
    }


    public String translateText(String sourceText, String from, String to) {
        sourceText.replace("+", "%2B");
        String salt = String.valueOf(System.currentTimeMillis());

        String sign = md5(Config.get("baidu.appId").toString() + sourceText + salt + Config.get("baidu.appSecret").toString());

        URI uri = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("q", sourceText)
                .queryParam("from", from)
                .queryParam("to", to)
                .queryParam("appid", "20230425001655623")
                .queryParam("salt", salt)
                .queryParam("sign", sign)
                .build()
                .toUri();


        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
        Map<String, Object> responseBody = response.getBody();
        log.info("baidu translate response: {}", responseBody);

        List<Map<String, String>> translatedResults = (List<Map<String, String>>) responseBody.get("trans_result");
        if (CollectionUtils.isEmpty(translatedResults)) {
            return null;
        }

        return translatedResults.stream()
                .map(result -> result.get("dst"))
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");
    }

    /**
     * MD5 ，得到 32 位小写的 sign。
     */
    private static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密错误", e);
        }
    }


}
