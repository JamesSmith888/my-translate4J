package com.jim.mytranslate4j.event;

import org.springframework.context.ApplicationEvent;

/**
 * 鼠标选中文本事件
 *
 * @author jim
 */
public class SelectedTextCaptureEvent extends ApplicationEvent {

    private String selectedText;

    public SelectedTextCaptureEvent(Object source, String selectedText) {
        super(source);
        this.selectedText = selectedText;
    }


    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }
}
