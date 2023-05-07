package com.jim.mytranslate4j.ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author jim
 */
@Component
public class DefaultOcrService implements OcrService {
    private Tesseract tesseract;

    @Override
    public void loadModel() {
        if (tesseract != null) {
            return;
        }

        tesseract = new Tesseract();
        // 识别中文+英文
        tesseract.setLanguage("eng+chi_sim");

        tesseract.setDatapath("tessdata");
    }

    @Override
    public String doOcr(String imagePath) {
        loadModel();

        File imageFile = new File(imagePath);
        try {
            String s = tesseract.doOCR(imageFile);
            // 替换驼峰-_为空格
            s = s.replaceAll("([a-z])([A-Z])", "$1 $2");
            return s;
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
    }
}
