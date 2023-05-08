package com.jim.mytranslate4j.translate.chagpt;

import com.jim.mytranslate4j.enums.TranslateType;
import com.jim.mytranslate4j.event.UpdateTextAreaEvent;
import com.jim.mytranslate4j.translate.Translate;
import com.jim.mytranslate4j.translate.opus.Translator;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.util.Proxys;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.net.Proxy;
import java.util.Arrays;

/**
 * @author jim
 */
@Component
public class ChatGptTranslate implements Translate {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private Translator translator;

    @Override
    public String translate(String content) {
        //国内需要代理 国外不需要
        Proxy proxy = Proxys.http("127.0.0.1", 7890);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("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")
                .proxy(proxy)
                .timeout(900)
                .apiHost("https://api.openai.com/") //反向代理地址
                .build()
                .init();

        Message system = Message.ofSystem("下面我让你来充当翻译家，你的目标是把任何语言翻译成中文，请翻译时不要带翻译腔，而是要翻译得自然、流畅和地道，使用优美和高雅的表达方式。记住用中文表达，不要用英文表达。");
        Message message = Message.of(content);

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(Arrays.asList(system, message))
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        Message res = response.getChoices().get(0).getMessage();

        return res.getContent();
    }


    @Override
    public void updateTranslateResult(String result) {
        applicationEventPublisher.publishEvent(new UpdateTextAreaEvent(this, TranslateType.Chat_GPT, result));

    }
}
