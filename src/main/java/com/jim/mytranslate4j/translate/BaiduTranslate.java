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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigInteger;
import java.net.URLEncoder;
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

    private static final String API_URL = "https://fanyi-api.baidu.com/api/trans/vip/translate";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String translate(String content) {
        return translateText(content, "en", "zh");
    }

    @Override
    public void updateTranslateResult(String result) {
        applicationEventPublisher.publishEvent(new UpdateTextAreaEvent(this, TranslateType.Baidu, result));
    }


    public String translateText(String sourceText, String from, String to) {
        String salt = String.valueOf(System.currentTimeMillis());
        String encodedSourceText = URLEncoder.encode(sourceText, StandardCharsets.UTF_8);
        String sign = getMD5(Config.get("baidu.appId").toString() + encodedSourceText + salt + Config.get("baidu.appSecret").toString());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("q", encodedSourceText)
                .queryParam("from", from)
                .queryParam("to", to)
                .queryParam("appid", Config.get("baidu.appId").toString())
                .queryParam("salt", salt)
                .queryParam("sign", sign);


        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Map.class);
        Map<String, Object> responseBody = response.getBody();
        log.info("baidu translate response: {}", responseBody);

        List<Map<String, String>> translatedResults = (List<Map<String, String>>) responseBody.get("trans_result");
        if (translatedResults != null && !translatedResults.isEmpty()) {
            return translatedResults.get(0).get("dst");
        }

        return null;

    }


    /**
     * MD5 ，得到 32 位小写的 sign。
     */
    private String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes(StandardCharsets.UTF_8));
            String hash = new BigInteger(1, md.digest()).toString(16);
            while (hash.length() < 32) {
                hash = "0" + hash;
            }
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 签名计算失败", e);
        }
    }


}
