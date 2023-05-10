package org.jim.mytranslate4j.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author jim
 */
public class ShowOverlayEvent extends ApplicationEvent {
    public ShowOverlayEvent(Object source) {
        super(source);
    }


}
