package org.jim.mytranslate4j.event;

import org.jim.mytranslate4j.common.ScreenCapture;
import org.springframework.context.ApplicationEvent;

/**
 * @author jim
 */
public class ShowOverlayEvent extends ApplicationEvent {

    private ScreenCapture screenCapture = ScreenCapture.TRANSLATE;

    public ShowOverlayEvent(Object source) {
        super(source);
    }

    public ShowOverlayEvent(Object source, ScreenCapture screenCapture) {
        super(source);
        this.screenCapture = screenCapture;
    }

    public ScreenCapture getScreenCapture() {
        return screenCapture;
    }

    public void setScreenCapture(ScreenCapture screenCapture) {
        this.screenCapture = screenCapture;
    }
}
