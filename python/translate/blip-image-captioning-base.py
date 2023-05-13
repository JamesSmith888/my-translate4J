import io
import os
import sys
from PIL import Image
from flask import Flask, request, jsonify
from transformers import BlipProcessor, BlipForConditionalGeneration

os.environ['FLASK_RUN_FROM_CLI'] = 'false'
sys.stdout.reconfigure(encoding='utf-8')

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

app = Flask(__name__)

model_path = sys.argv[1]

# Load the model and processor
processor = BlipProcessor.from_pretrained(model_path)
model = BlipForConditionalGeneration.from_pretrained(model_path).to("cuda")


@app.route('/status', methods=['GET'])
def status():
    return "ok"


@app.route('/process_image', methods=['POST'])
def process_image():
    try:
        image = Image.open("temp/screenshot.png").convert('RGB')

        text = request.json['text']

        # 全精度处理。
        # 数值范围：使用全精度的浮点数（float32），可以表示更广范围的数值。这对于处理具有较大数值范围的任务非常重要，例如图像分类或语言生成，因为模型参数和计算中间结果可能涉及到很大或很小的值。
        #
        # 数值精度：全精度浮点数提供更高的数值精度，即能够表示更多小数位。这对于保持计算的准确性非常重要，特别是在执行复杂的数学运算时，例如矩阵乘法和梯度计算。较高的数值精度可以减少由于舍入误差引起的计算错误。
        #
        # 计算稳定性：全精度计算可以提高计算的稳定性。在深度学习模型中，许多计算都涉及到大量的矩阵乘法、加法和激活函数等操作。全精度可以减少数值溢出或下溢的风险，并降低计算中的不稳定性。
        #
        # 尽管全精度模式提供了更高的精度和计算准确性，但它需要更多的计算资源和内存来存储和处理大量的浮点数。在某些情况下，例如在资源受限的环境下或对速度要求较高的推理任务中，可以考虑使用半精度模式（float16）来权衡计算速度和模型的准确性。
        if text == "" or text is None:
            print("no text")
            # 无提示文本
            inputs = processor(image, return_tensors="pt").to("cuda")
        else:
            print("text: " + text)
            inputs = processor(image, text, return_tensors="pt").to("cuda")

        out = model.generate(**inputs, max_new_tokens=1000)

        result = processor.decode(out[0], skip_special_tokens=True)

        print(result)
        return result

    except Exception as e:
        return jsonify({"error": str(e)})


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
