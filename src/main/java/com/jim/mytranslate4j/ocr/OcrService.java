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
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
    }

    default String doOcr() {
        return doOcr("temp/screenshot.png");
    }
}
