package com.jim.mytranslate4j.enums;

import com.jim.mytranslate4j.gui.pane.BaiduTranslatePane;
import com.jim.mytranslate4j.gui.pane.ChatGptTranslatePane;
import com.jim.mytranslate4j.gui.pane.MyMemoryTranslatePane;
import com.jim.mytranslate4j.gui.pane.OpusTranslatePane;
import com.jim.mytranslate4j.util.SpringContextUtils;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * @author jim
 */

@Getter
public enum TranslateType {

    Baidu(str -> SpringContextUtils.getBean(BaiduTranslatePane.class).updateTextArea(str)),

    Google((str) -> {
    }),

    Opus_mt_en_zh(str -> SpringContextUtils.getBean(OpusTranslatePane.class).updateTextArea(str)),

    My_Memory(str -> SpringContextUtils.getBean(MyMemoryTranslatePane.class).updateTextArea(str)),

    Chat_GPT(str -> SpringContextUtils.getBean(ChatGptTranslatePane.class).updateTextArea(str));


    private Consumer<String> consumer;


    public void setConsumer(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    TranslateType(Consumer<String> consumer) {
        this.consumer = consumer;
    }
}
