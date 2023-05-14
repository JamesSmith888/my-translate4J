package org.jim.mytranslate4j.gui;

import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import jakarta.annotation.Resource;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.jim.mytranslate4j.ai.imagecaptioning.ImageCaptioningService;
import org.jim.mytranslate4j.config.Config;
import org.jim.mytranslate4j.event.ShowOverlayEvent;
import org.jim.mytranslate4j.event.gui.UntranslatedTextAreaEvent;
import org.jim.mytranslate4j.gui.pane.TranslatePane;
import org.jim.mytranslate4j.gui.pane.TranslatePaneService;
import org.jim.mytranslate4j.plugin.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;


/**
 * @author jim
 */
@Component
public class Start {

    @Resource
    private ScreenCapture screenCapture;

    @Resource
    private Config config;

    @Resource
    private UntranslatedTextAreaEvent untranslatedTextAreaEvent;

    @Autowired
    private List<TranslatePane> translatePanes;

    @Resource
    private TranslatePaneService translatePaneService;

    @Resource
    private ImageCaptioningService imageCaptioningService;

    private Timeline translationTimeline;

    private TextArea textArea;

    private Stage stage;

    private ImageView originalImageView;

    private Stage imgStage;


    @Resource
    private PluginService pluginService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;


    public void start() {
        // 阻止JavaFX应用程序在最后一个窗口关闭时退出（确保关闭主窗口时不会退出应用程序，导致后续的截屏功能无法使用）
        Platform.setImplicitExit(false);

        Stage stage = initUi();

        // 在AWT线程上初始化托盘图标
        EventQueue.invokeLater(() -> initTrayIcon(stage));

        // 截屏
        screenCapture.init();
    }


    /**
     * 初始化界面
     */
    private Stage initUi() {
        stage = new Stage();
        stage.setTitle("My Translate");
        // 设置任务栏图标
        javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResourceAsStream("/img/translate.png"));
        stage.getIcons().add(image);

        VBox vBox = new VBox();

        // 菜单栏
        initMenuBar(vBox);


        // 来源文本框
        textArea = new TextArea();
        // 监听文本框的变化
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (StringUtils.isBlank(newValue)) {
                return;
            }

            if (translationTimeline != null) {
                translationTimeline.stop();
            }

            // 判断是否有\n，如果有，立即翻译
            if (newValue.contains("\n")) {
                // 插件翻译并且对应的翻译面板显示翻译结果
                untranslatedTextAreaEvent.translated(newValue);

                // 插件翻译并且对应的翻译面板显示翻译结果
                pluginService.translateAndShow(newValue);
                return;
            }

            translationTimeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
                if (!newValue.equals(textArea.getText())) {
                    System.out.println("文本框内容已经变化，不执行翻译");
                    return;
                }

                untranslatedTextAreaEvent.translated(newValue);

                // 插件翻译并且对应的翻译面板显示翻译结果
                pluginService.translateAndShow(newValue);

            }));
            translationTimeline.play();
        });


        TitledPane titledPane = new TitledPane("", textArea);
        // 隐藏标题栏
        titledPane.setCollapsible(false);


        ObservableList<Node> children = vBox.getChildren();
        children.add(titledPane);


        translatePanes.forEach(f -> children.add(f.pane()));

        // 创建插件面板
        children.addAll(pluginService.initPluginPane());


        stage.setScene(new Scene(vBox, 400, 500));

        // 添加关闭请求处理程序
        stage.setOnCloseRequest(event -> {
            event.consume(); // 阻止默认关闭操作
            stage.hide(); // 隐藏窗口
        });

        // 窗口隐藏时，清空文本框内容
        stage.setOnHidden(event -> {
            // 清空原文本
            textArea.clear();

            translatePaneService.clear();
        });

        // esc键隐藏窗口
        stage.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                stage.hide();
            }
        });


        stage.show();


        return stage;
    }


    /**
     * 菜单栏
     */
    private void initMenuBar(VBox vBox) {
        // 创建菜单栏
        MenuBar menuBar = new MenuBar();

        // 创建设置菜单项
        MenuItem settingsMenuItem = new MenuItem("设置");
        settingsMenuItem.setOnAction(event -> showSettingsPanel());
        // 添加设置菜单项到菜单栏
        Menu settingsMenu = new Menu("设置");
        settingsMenu.getItems().add(settingsMenuItem);

        // 试验性功能
        MenuItem experimentalMenuItem = new MenuItem("图片字幕翻译（Image Captioning）");
        experimentalMenuItem.setOnAction(event -> showExperimentalPanel());
        // 添加 图片描述翻译（Image Captioning） 菜单项到菜单栏
        Menu imageCaptioningMenu = new Menu("试验性功能");
        imageCaptioningMenu.getItems().add(experimentalMenuItem);


        menuBar.getMenus().addAll(settingsMenu, imageCaptioningMenu);

        // 添加菜单栏到布局
        vBox.getChildren().add(menuBar);
    }

    private void showExperimentalPanel() {
        imgStage = new Stage();
        imgStage.setTitle("Image Processing");

        BorderPane root = new BorderPane();

        // Top bar with buttons
        HBox topBar = new HBox();
        topBar.setSpacing(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(15, 15, 15, 15));

        Button screenshotButton = new Button("截屏");
        topBar.getChildren().addAll(screenshotButton);
        root.setTop(topBar);

        Label serverStatusLabel = new Label("服务启动中...");

        // 设置Label外观和位置
        serverStatusLabel.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        serverStatusLabel.setCursor(Cursor.HAND);

        // 单击刷新服务器状态
        serverStatusLabel.setOnMouseClicked(event -> serverStatus(serverStatusLabel));

        // 将Label放置到BorderPane底部
        HBox serverStatusBox = new HBox();
        serverStatusBox.setAlignment(Pos.CENTER_LEFT);
        serverStatusBox.setPadding(new Insets(0, 0, 10, 10));
        serverStatusBox.getChildren().add(serverStatusLabel);
        root.setBottom(serverStatusBox);

        // Image view for the original image
        originalImageView = new ImageView();

        // Set a fixed size for the image view and preserve the aspect ratio
        originalImageView.setPreserveRatio(true);
        originalImageView.setFitHeight(600);
        originalImageView.setFitWidth(600);

        Label label = new Label("点击上传图片");
        // 设置文本样式
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // 创建一个StackPane将ImageView、Label和边框组合在一起
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().addAll(originalImageView, label);

        // 设置StackPane的边框和鼠标样式
        stackPane.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-border-style: dashed;");
        stackPane.setCursor(Cursor.HAND);

        // Center content with image view and label
        VBox centerContent = new VBox();
        centerContent.setSpacing(10);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(20, 20, 20, 20));

        HBox imageHBox = new HBox();
        imageHBox.setSpacing(20);
        imageHBox.setAlignment(Pos.CENTER);

        VBox originalImageBox = new VBox();
        originalImageBox.setSpacing(5);
        originalImageBox.setAlignment(Pos.CENTER);

        originalImageBox.getChildren().add(stackPane);

        imageHBox.getChildren().addAll(originalImageBox);
        centerContent.getChildren().add(imageHBox);

        // Input text field for user prompt and submit button
        TextArea promptInput = new TextArea();
        promptInput.setPromptText("（非必填）图片提示词，用于补充模型忽略的描述。例如：一只狗在草地上。");

        // 设置TextArea宽度和高度
        promptInput.setPrefWidth(550);
        promptInput.setPrefHeight(70);

        // 执行按钮
        Button submitButton = new Button("运行");

        // 将TextArea和Button放在同一行
        HBox inputAndSubmit = new HBox();
        inputAndSubmit.setSpacing(10);
        inputAndSubmit.setAlignment(Pos.CENTER);
        inputAndSubmit.getChildren().addAll(promptInput, submitButton);

        centerContent.getChildren().add(inputAndSubmit);


        // Label for displaying the image description
        Label imageDescriptionLabel = new Label();
        centerContent.getChildren().add(imageDescriptionLabel);

        // File chooser for image upload
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");

        // Check server status on startup
        serverStatus(serverStatusLabel);

        // Event handlers
        stackPane.setOnMouseClicked(e -> {
            File file = fileChooser.showOpenDialog(imgStage);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                originalImageView.setImage(image);
            }
        });

        submitButton.setOnAction(event -> {
            submitButton.setText("运行中...");

            // 异步执行，防止界面卡死
            Thread thread = new Thread(() -> {
                Image image = originalImageView.getImage();
                String prompt = promptInput.getText();
                String imageDescription = imageCaptioningService.getImageDescription(image, prompt);

                Platform.runLater(() -> {
                    imageDescriptionLabel.setText(imageDescription);
                    submitButton.setText("运行");
                    // 翻译窗口置顶
                    stage.toFront();
                });

                // 将图片描述填充到文本框进行翻译
                updateTextAreaAndTranslate(imageDescription);

            });

            thread.start();
        });

        screenshotButton.setOnAction(e -> {
            // 隐藏当前窗口
            imgStage.hide();
            // 隐藏翻译窗口
            stage.hide();

            // 发布显示遮罩层的事件
            applicationEventPublisher.publishEvent(new ShowOverlayEvent(this, org.jim.mytranslate4j.common.ScreenCapture.IMAGE_CAPTIONING));
        });

        root.setTop(topBar);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 650, 800);

        imgStage.setScene(scene);
        imgStage.show();
    }

    public void setOriginalImageView() {
        Platform.runLater(() -> {
            // 显示图片处理窗口
            imgStage.show();
            // 显示翻译窗口
            stage.show();

            File file = new File("temp/screenshot.png");
            Image image = new Image(file.toURI().toString());
            originalImageView.setImage(image);
        });
    }


    private void serverStatus(Label serverStatusLabel) {
        serverStatusLabel.setText("检查中...");

        Thread thread = new Thread(() -> {
            boolean b = imageCaptioningService.serverStatus();

            Platform.runLater(() -> serverStatusLabel.setText(b ? "已连接模型服务" : "服务启动失败"));
        });

        thread.start();
    }


    private void showSettingsPanel() {
        Stage settingsStage = new Stage();
        settingsStage.setTitle("设置");

        BorderPane settingsRoot = new BorderPane();

        // 创建垂直菜单栏
        ListView<String> settingsMenu = new ListView<>();
        settingsMenu.getItems().addAll("Baidu翻译", "Google翻译", "opus-mt-en-zh", "chatgpt");
        settingsMenu.setPrefWidth(110);

        // 设置选中事件
        settingsMenu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Baidu翻译".equals(newValue)) {
                // 显示Baidu翻译设置面板
                showBaiduSettings(settingsRoot);
            } else if ("Google翻译".equals(newValue)) {
                // 显示Google翻译设置面板
                showGoogleSettings(settingsRoot);
            } else if ("opus-mt-en-zh".equals(newValue)) {
                // 显示opus-mt-en-zh设置面板
                showOpusMtEnZhSettings(settingsRoot);
            } else if ("chatgpt".equals(newValue)) {
                // 显示chatgpt设置面板
                showChatGptSettings(settingsRoot);
            }
        });


        // 将垂直菜单栏添加到布局的左侧
        settingsRoot.setLeft(settingsMenu);

        settingsStage.setScene(new Scene(settingsRoot, 500, 300));
        settingsStage.show();
    }

    private void showChatGptSettings(BorderPane settingsRoot) {
        VBox gptForm = new VBox(10);
        gptForm.setPadding(new Insets(10));

        // chatgpt token输入框
        Label tokenLabel = new Label("token:");
        TextField tokenField = new TextField();
        // chatgpt 模型选择GPT_3_5_TURBO/4.0
        Label modelLabel = new Label("模型:");
        ChoiceBox<String> modelChoiceBox = new ChoiceBox<>();

        modelChoiceBox.getItems().addAll(Arrays.stream(ChatCompletion.Model.values()).map(ChatCompletion.Model::getName).toList());
        modelChoiceBox.setValue(ChatCompletion.Model.GPT_3_5_TURBO.getName());

        // 保存按钮
        Button submitButton = new Button("保存");
        submitButton.setOnAction(event -> {
            // 处理提交表单操作
            System.out.println("token: " + tokenField.getText());
            System.out.println("模型: " + modelChoiceBox.getValue());

            // 将配置保存到config.yml文件中
            Config.set("chatgpt.token", tokenField.getText());
            Config.set("chatgpt.model", modelChoiceBox.getValue());
            config.saveConfig();

            // 显示保存成功提示
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("保存成功");
            alert.setHeaderText(null);
            alert.setContentText("配置已成功保存。现在可以关闭设置窗口。");
            alert.show();
        });


        gptForm.getChildren().addAll(tokenLabel, tokenField, modelLabel, modelChoiceBox, submitButton);
        settingsRoot.setCenter(gptForm);
    }

    private void showOpusMtEnZhSettings(BorderPane settingsRoot) {

    }

    private void showBaiduSettings(BorderPane settingsRoot) {
        VBox baiduForm = new VBox(10);
        baiduForm.setPadding(new Insets(10));

        Label appIdLabel = new Label("APP_ID:");
        TextField appIdField = new TextField();
        Label appSecretLabel = new Label("APP_SECRET:");
        TextField appSecretField = new TextField();
        Button submitButton = new Button("保存");
        submitButton.setOnAction(event -> {
            // 处理提交表单操作
            System.out.println("APP_ID: " + appIdField.getText());
            System.out.println("APP_SECRET: " + appSecretField.getText());

            // 将配置保存到config.yml文件中
            Config.set("baidu.appId", appIdField.getText());
            Config.set("baidu.appSecret", appSecretField.getText());
            config.saveConfig();

            // 显示保存成功提示
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("保存成功");
            alert.setHeaderText(null);
            alert.setContentText("配置已成功保存。现在可以关闭设置窗口。");
            alert.show();

        });

        baiduForm.getChildren().addAll(appIdLabel, appIdField, appSecretLabel, appSecretField, submitButton);

        settingsRoot.setCenter(baiduForm);
    }

    private void showGoogleSettings(BorderPane settingsRoot) {
        VBox googleForm = new VBox(10);
        googleForm.setPadding(new Insets(10));

        // 在此处添加Google翻译设置表单的其他UI元素和代码

        settingsRoot.setCenter(googleForm);
    }


    /**
     * 更新需要翻译的textArea的内容
     */
    public void updateTextArea(String text) {
        Platform.runLater(() -> textArea.setText(text));
    }

    /**
     * 更新需要翻译的textArea的内容，并且设置标志位，从而立即翻译
     */
    public void updateTextAreaAndTranslate(String text) {
        Platform.runLater(() -> textArea.setText(text + "\n"));
    }

    /**
     * 判断窗口是否已经显示，如果没有显示，则显示窗口
     */
    public void showWindow() {
        Platform.runLater(() -> {
            if (!stage.isShowing()) {
                stage.show();
            }

            // 将窗口置于最前
            stage.toFront();
        });
    }


    private void initTrayIcon(Stage stage) {
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/translate.png"));

        // 确保图片已加载
        MediaTracker tracker = new MediaTracker(new java.awt.Container());
        tracker.addImage(image, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TrayIcon trayIcon = new TrayIcon(image, "My Translate");
        // 根据系统托盘尺寸自动调整图标大小
        trayIcon.setImageAutoSize(true);

        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            try {
                systemTray.add(trayIcon);
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }

            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        Platform.runLater(() -> {
                            if (stage.isIconified()) {
                                stage.setIconified(false);
                            }
                            if (!stage.isShowing()) {
                                stage.show();
                            }
                            stage.toFront();
                        });
                    }
                }
            });

        } else {
            System.out.println("系统不支持托盘");
        }
    }


}
