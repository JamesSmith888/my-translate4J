package org.jim.mytranslate4j.gui.pane;

import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jim
 */
@Component
public class TranslatePaneService {

    @Autowired
    private List<TranslatePane> translatePanes;

    public void clear() {
        translatePanes.forEach(TranslatePane::clear);
    }

    public void updateTextArea(TranslatePane translatePane, String text) {
        translatePanes.forEach(f -> Platform.runLater(() -> f.textArea().setText(text)));
    }

}
