package com.jim.mytranslate4j.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * gui component
 *
 * @author jim
 */
@Component
public class JavaFXComponent {


    public ImageView graphicImg(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }

        Image opusImage = new Image(getClass().getResource(path).toString());
        ImageView imageView = new ImageView(opusImage);
        // 设置图像高度
        imageView.setFitHeight(15);
        imageView.setPreserveRatio(true);
        return imageView;
    }
}
