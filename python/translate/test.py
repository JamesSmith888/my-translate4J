from paddleocr import PaddleOCR

ocr = PaddleOCR(use_angle_cls=True)  # 中英文混合识别

import cv2

img_path = '../../temp/screenshot.png'
img = cv2.imread(img_path)

result = ocr.ocr(img)
print(result)
for line in result:
    for word_info in line:
        print(word_info[-1])
