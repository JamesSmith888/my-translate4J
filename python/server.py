import cv2
from flask import Flask, request, jsonify
from paddleocr import PaddleOCR

app = Flask(__name__)

# 初始化OCR模型
ocr = PaddleOCR(use_angle_cls=True)


@app.route('/ocr')
def ocr_endpoint():
    img_array = cv2.imread("temp/screenshot.png", cv2.IMREAD_COLOR)

    # 使用PaddleOCR进行文本识别
    result = ocr.ocr(img_array)

    # 提取识别结果并返回
    extracted_text = []
    for line in result:
        for word_info in line:
            extracted_text.append(word_info[1][0])

    return jsonify(extracted_text)

# test the server is running
@app.route('/')
def index():
    return 'server is running'


if __name__ == '__main__':
    app.run(port=5000)
