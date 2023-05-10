package org.jim.mytranslate4j.event.gui;

import org.jim.mytranslate4j.translate.TranslateService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author jim
 */
@Component
public class UntranslatedTextAreaEvent {


    @Resource
    private TranslateService translateService;

    public void translated(String newValue) {
        translateService.translateAndUpdate(newValue);
    }

}
