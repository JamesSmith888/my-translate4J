//package com.jim.mytranslate4j.test;
//
//import org.apache.http.client.fluent.Request;
//import org.apache.http.client.fluent.Response;
//import org.apache.http.client.utils.URIBuilder;
//import org.json.JSONObject;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//
//public class MyMemoryTranslatedExample {
//    public static void main(String[] args) {
//        // 需要翻译的文本
//        String textToTranslate = "你好，世界！";
//
//
//        RestTemplate restTemplate = new RestTemplate();
//
//
//
//        // 调用 MyMemory Translated API
//        try {
//            URI uri = new URIBuilder("https://api.mymemory.translated.net/get")
//                    .addParameter("q", textToTranslate)
//                    .addParameter("langpair", "zh-CN|en")
//                    .build();
//
//            Response response = Request.Get(uri).execute();
//            String jsonResponse = response.returnContent().asString();
//
//            // 解析 JSON 响应
//            JSONObject jsonObject = new JSONObject(jsonResponse);
//            String translatedText = jsonObject.getJSONObject("responseData").getString("translatedText");
//
//            // 输出翻译结果
//            System.out.println("原文: " + textToTranslate);
//            System.out.println("翻译后: " + translatedText);
//        } catch (URISyntaxException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
