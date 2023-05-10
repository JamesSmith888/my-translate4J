package org.jim.mytranslate4j.translate;

/**
 * 翻译接口
 *
 * @author jim
 */
public interface Translate {

    /**
     * 传入要翻译的内容，返回翻译结果
     */
    String translate(String content);

    /**
     * 更新界面的翻译结果文本
     */
    void updateTranslateResult(String result);

}
