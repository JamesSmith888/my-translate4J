package com.jim.mytranslate4j.translate.mymemory;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

/**
 * @author jim
 */
@HttpExchange
public interface MyMemoryTranslatedClient {

    @GetExchange("/get?q={content}&langpair=en|zh-CN")
    Mono<MyMemory> translate(@PathVariable("content") String content);


}