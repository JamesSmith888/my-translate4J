package org.jim.mytranslate4j.ocr;

/**
 * @author A
 */
public interface OcrService {

    /**
     * 加载ocr模型，目前先写死。后续有思路再改
     */
    void loadModel();

    String doOcr(String imagePath);

    default String doOcr() {
        return doOcr("temp/screenshot.png");
    }
}
