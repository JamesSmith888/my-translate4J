//package com.jim.mytranslate4j.test;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//public class TranslatorRestTemplate {
//
//    private static final String SUBSCRIPTION_KEY = "YOUR_SUBSCRIPTION_KEY";
//    private static final String ENDPOINT = "https://api.cognitive.microsofttranslator.com/";
//    private static final String TRANSLATE_PATH = "translate?api-version=3.0";
//
//    public static void main(String[] args) {
//        String textToTranslate = "Hello, world!";
//        String targetLanguage = "zh-Hans"; // Simplified Chinese
//
//        try {
//            String translation = translate(textToTranslate, targetLanguage);
//            System.out.println("Translated text: " + translation);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String translate(String text, String targetLanguage) {
//        RestTemplate restTemplate = new RestTemplateBuilder().build();
//        String url = ENDPOINT + TRANSLATE_PATH + "&to=" + targetLanguage;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Ocp-Apim-Subscription-Key", SUBSCRIPTION_KEY);
//        headers.add("Content-Type", "application/json");
//
//        JsonObject textObj = new JsonObject();
//        textObj.addProperty("Text", text);
//
//        JsonArray requestBodyArray = new JsonArray();
//        requestBodyArray.add(textObj);
//
//        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyArray.toString(), headers);
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//
//        Gson gson = new Gson();
//        JsonArray translationResults = gson.fromJson(responseEntity.getBody(), JsonArray.class);
//        JsonObject translationResult = translationResults.get(0).getAsJsonObject();
//        JsonArray translations = translationResult.get("translations").getAsJsonArray();
//        JsonObject translation = translations.get(0).getAsJsonObject();
//
//        return translation.get("text").getAsString();
//    }
//}
