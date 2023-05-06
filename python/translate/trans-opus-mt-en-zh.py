from transformers import MarianMTModel, MarianTokenizer

mode_name = 'D:/ideaWork\my-translate4J/trans-opus-mt-en-zh'


def translate(model_name, text):
    tokenizer = MarianTokenizer.from_pretrained(model_name)
    model = MarianMTModel.from_pretrained(model_name)
    input_ids = tokenizer.encode(text, return_tensors="pt")
    output_ids = model.generate(input_ids, max_length=1000)
    return tokenizer.decode(output_ids[0], skip_special_tokens=True)


if __name__ == "__main__":
    input_text = 'FutureWarning: The class `AutoModelWithLMHead` is deprecated and will be removed in a future version. Please use `AutoModelForCausalLM` for causal language models, `AutoModelForMaskedLM` for masked language models and `AutoModelForSeq2SeqLM` for encoder-decoder models.'
    model_path = mode_name
    translated_text = translate(model_path, input_text)
    print(translated_text)
