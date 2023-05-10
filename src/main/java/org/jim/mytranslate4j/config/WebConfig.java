package org.jim.mytranslate4j.config;

import org.jim.mytranslate4j.translate.mymemory.MyMemoryTranslatedClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * @author jim
 */
@Configuration
public class WebConfig {

    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.mymemory.translated.net/")
                .build();
    }


    @Bean
    MyMemoryTranslatedClient postClient(WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient))
                        .build();
        return httpServiceProxyFactory.createClient(MyMemoryTranslatedClient.class);
    }
}
