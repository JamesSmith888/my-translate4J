package com.jim.mytranslate4j.ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * @author A
 */
public interface OcrService {
    default String doOcr(String imagePath) {
        File imageFile = new File(imagePath);
        Tesseract tesseract = new Tesseract();
        // 识别中文+英文
        tesseract.setLanguage("eng+chi_sim");

        tesseract.setDatapath("tessdata");

        try {
            String s = tesseract.doOCR(imageFile);

            // 替换特殊字符与换行符之类的为空格
            String s1 = s.replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5.，,。？“”]+", " ");
            // 驼峰替换为空格
            String s2 = s1.replaceAll("([a-z])([A-Z])", "$1 $2");
            return s2;
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
    }

    default String doOcr() {
        return doOcr("temp/screenshot.png");
    }
}
