import io
import sys

from transformers import MarianMTModel, MarianTokenizer

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')


def translate(model_name, text):
    tokenizer = MarianTokenizer.from_pretrained(model_name)
    model = MarianMTModel.from_pretrained(model_name)
    input_ids = tokenizer.encode(text, return_tensors="pt")
    output_ids = model.generate(input_ids, max_length=1000)
    return tokenizer.decode(output_ids[0], skip_special_tokens=True)


if __name__ == "__main__":
    input_text = sys.argv[1]
    model_path = sys.argv[2]
    translated_text = translate(model_path, input_text)
    print(translated_text)
