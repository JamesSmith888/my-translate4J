package com.jim.mytranslate4j.enums;

import com.jim.mytranslate4j.gui.Start;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.BiConsumer;

/**
 * @author jim
 */

@AllArgsConstructor
@Getter
public enum TranslateType {

    Baidu(Start::updateBaiduTextArea),

    Google((start, str) -> {
    }),

    Opus_mt_en_zh(Start::updateOpusMtTextArea);


    private BiConsumer<Start, String> consumer;

    public void setConsumer(BiConsumer<Start, String> consumer) {
        this.consumer = consumer;
    }
}
