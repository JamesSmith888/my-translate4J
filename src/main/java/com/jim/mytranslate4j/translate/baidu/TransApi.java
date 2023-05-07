package com.jim.mytranslate4j.translate.baidu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jim.mytranslate4j.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jim
 */
@Component
@Slf4j
public class TransApi {
    private static final String TRANS_API_HOST = "https://api.fanyi.baidu.com/api/trans/vip/translate";


    public String getTransResult(String query, String from, String to) {
        Map<String, String> params = buildParams(query, from, to);
        return HttpGet.get(TRANS_API_HOST, params);
    }

    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", Config.get("baidu.appId").toString());

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = Config.get("baidu.appId").toString() + query + salt + Config.get("baidu.appSecret").toString(); // 加密前的原文
        params.put("sign", MD5.md5(src));

        return params;
    }

    public TranslationResult getTranslationResult(String query) {
        String transResult = getTransResult(query, "auto", "zh");
        log.info("transResult: {}", transResult);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(transResult, TranslationResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String getTranslationValue(String query) {
        TranslationResult translationResult = getTranslationResult(query);

        if (translationResult == null) {
            return "";
        }

        return translationResult.getTransResult().stream()
                .map(TransResultItem::getDst)
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");
    }


}
