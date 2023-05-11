My-Translate4J
==============

My-Translate4J 是一个基于Jdk17、Spring Boot 3.0.6、Python 3.10、PowerShell 和 JavaFX 的 Windows 桌面客户端翻译软件，但目前还处于开发优化阶段，适合有相关知识的开发者。它支持多种翻译来源，如百度翻译（需要申请 API）、ChatGPT（需要申请 token 和本地代理）、MyMemory 和 Opus\_mt\_en\_zh（需要下载翻译模型）。

该软件还支持插件/扩展功能，基于 SPI 实现。插件 SDK 和插件 demo 可在以下链接中找到：

*   插件 SDK：[https://github.com/youdontknow-hash/my-translate4J-plugin-api](https://github.com/youdontknow-hash/my-translate4J-plugin-api)
*   插件 demo：[https://github.com/youdontknow-hash/my-translate4J-plugin-api-demo](https://github.com/youdontknow-hash/my-translate4J-plugin-api-demo)

OCR 识图功能基于 Tess4J 项目，目前英文识别效果较好，但中文识别率不太高。需要下载相关模型后放入 tessdata 文件夹中。

使用快捷键 Alt+S 进行截图翻译，Alt+Z 对选中的文本进行翻译。请注意检查是否有快捷键冲突。

如何开始
----

### 安装 JavaFX

由于新版本的 JDK 已经剥离了 JavaFX 相关的库，您需要手动下载并导入到项目中。运行时请添加以下 VM 选项：


```css
--module-path javafx路径/lib --add-modules javafx.controls,javafx.fxml
```

### Opus\_mt\_en\_zh 翻译模型

从以下链接下载所有文件并放入项目根目录下的 `opus-mt-en-zh-local` 文件夹中：

[https://huggingface.co/Helsinki-NLP/opus-mt-en-zh/tree/main](https://huggingface.co/Helsinki-NLP/opus-mt-en-zh/tree/main)

### OCR 识图模型

从 Tesseract 官网下载相关模型，并放入项目的 `tessdata` 文件夹中。

贡献
--

欢迎通过创建插件或改进现有功能为 My-Translate4J 做出贡献。有关插件开发的更多信息，请参考插件 SDK 和插件 demo。

许可证
---

本项目基于 [MIT 许可证](LICENSE)。

---

My-Translate4J
==============

My-Translate4J is a desktop translation software for Windows, built with Jdk17、Spring Boot 3.0.6, Python 3.10, PowerShell, and JavaFX,but currently, it is still in the development and optimization stage, suitable for developers with relevant knowledge. It supports various translation sources, such as Baidu Translate (requires API application), ChatGPT (requires token and local proxy application), MyMemory, and Opus\_mt\_en\_zh (requires translation model download).

The software also supports plugin/extension features, implemented based on SPI. The plugin SDK and plugin demo can be found in the following links:

*   Plugin SDK: [https://github.com/youdontknow-hash/my-translate4J-plugin-api](https://github.com/youdontknow-hash/my-translate4J-plugin-api)
*   Plugin demo: [https://github.com/youdontknow-hash/my-translate4J-plugin-api-demo](https://github.com/youdontknow-hash/my-translate4J-plugin-api-demo)

The OCR image recognition feature is based on the Tess4J project. Currently, English recognition is good, but Chinese recognition is not very high. You need

to download the relevant models and place them in the `tessdata` folder.

Use the shortcut Alt+S for screenshot translation and Alt+Z for translating selected text. Please check for shortcut key conflicts.

Click the top left corner to switch between Chinese/English documentation.

Getting Started
---------------

### Install JavaFX

Since the new version of JDK has stripped the JavaFX-related libraries, you need to manually download and import them into the project. When running, add the following VM options:


```css
--module-path javafx_path/lib --add-modules javafx.controls,javafx.fxml
```

### Opus\_mt\_en\_zh Translation Model

Download all files from the link below and place them in the `opus-mt-en-zh-local` folder under the project root directory:

[https://huggingface.co/Helsinki-NLP/opus-mt-en-zh/tree/main](https://huggingface.co/Helsinki-NLP/opus-mt-en-zh/tree/main)

### OCR Image Recognition Model

Download the relevant models from the Tesseract official website and place them in the `tessdata` folder of the project.

Contributing
------------

You are welcome to contribute to My-Translate4J by creating plugins or improving existing features. For more information on plugin development, please refer to the plugin SDK and plugin demo.

License
-------

This project is licensed under the [MIT License](LICENSE).
