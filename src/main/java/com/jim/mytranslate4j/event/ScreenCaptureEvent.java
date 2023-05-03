package com.jim.mytranslate4j.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author jim
 */
public class ScreenCaptureEvent extends ApplicationEvent {
    public ScreenCaptureEvent(Object source) {
        super(source);
    }


}
