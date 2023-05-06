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


            // 替换驼峰-_为空格
            s = s.replaceAll("([a-z])([A-Z])", "$1 $2");
            return s;
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
    }

    default String doOcr() {
        return doOcr("temp/screenshot.png");
    }
}
