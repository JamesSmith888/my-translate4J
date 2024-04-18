package org.jim.mytranslate4j.ocr;

/**
 * @author A
 */
public interface OcrService {

    /**
     * 加载ocr模型，目前先写死。后续有思路再改
     */
    default void loadModel() {

    }

    String doOcr(String imagePath);

    default String doOcr() {
        // 获取项目绝对路径
        String path = System.getProperty("user.dir");

        return doOcr(path + "/temp/screenshot.png");
    }
}
