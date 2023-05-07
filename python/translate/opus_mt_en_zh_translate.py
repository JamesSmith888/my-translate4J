import io
import socket
import sys
from transformers import MarianMTModel, MarianTokenizer

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')


def translate(model, tokenizer, text):
    # 翻译文本
    input_ids = tokenizer.encode(text, return_tensors="pt")
    output_ids = model.generate(input_ids, max_length=1000)

    return tokenizer.decode(output_ids[0], skip_special_tokens=True)


def main(model_path):
    tokenizer = MarianTokenizer.from_pretrained(model_path)
    model = MarianMTModel.from_pretrained(model_path)

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind(('localhost', 12345))
    server_socket.listen(1)
    print("Listening for connections...")

    # socket 服务启动标志
    with open("server_ready.txt", "w") as f:
        f.write("Server is ready")

    try:
        while True:
            connection, client_address = server_socket.accept()
            print("Accepted connection from", client_address)
            try:
                while True:
                    data = connection.recv(1024)
                    if data:
                        input_text = data.decode("utf-8")
                        translated_text = translate(model, tokenizer, input_text)
                        connection.sendall((translated_text + '\n').encode("utf-8"))
                    else:
                        break
            finally:
                connection.close()
    except KeyboardInterrupt:
        print("Server is closing")
    finally:
        server_socket.close()


if __name__ == "__main__":
    model_path = sys.argv[1]
    main(model_path)
    print("Server is closed")
