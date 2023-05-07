package com.jim.mytranslate4j.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = {
                new X509TrustManager() {
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HostnameVerifier allHostsValid = (hostname, session) -> true;

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(sc.getSocketFactory());
                    ((HttpsURLConnection) connection).setHostnameVerifier(allHostsValid);
                }
                super.prepareConnection(connection, httpMethod);
            }
        };

        RestTemplate restTemplate = new RestTemplate(requestFactory);


        // 预热 RestTemplate
        warmUpRestTemplate(restTemplate);

        return restTemplate;
    }

    private void warmUpRestTemplate(RestTemplate restTemplate) {
        String warmUpUrl = "https://httpbin.org/get";

        // 发送预热请求
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(warmUpUrl, String.class);
            System.out.println("Warm-up request completed.");
        } catch (Exception e) {
            System.err.println("Warm-up request failed: " + e.getMessage());
        }
    }
}
