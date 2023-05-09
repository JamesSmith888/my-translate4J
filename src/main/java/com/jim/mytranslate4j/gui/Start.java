package com.jim.mytranslate4j.gui;

import com.jim.mytranslate4j.config.Config;
import com.jim.mytranslate4j.event.gui.UntranslatedTextAreaEvent;
import com.jim.mytranslate4j.gui.pane.TranslatePane;
import com.jim.mytranslate4j.gui.pane.TranslatePaneService;
import jakarta.annotation.Resource;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    @Resource
    private JavaFxComponent javaFxComponent;

    @Autowired
    private List<TranslatePane> translatePanes;

    @Resource
    private TranslatePaneService translatePaneService;

    private Timeline translationTimeline;

    private TextArea textArea;

    private TextArea baiduTextArea;

    private TextArea opusMtTextArea;

    private Stage stage;


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

            translationTimeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
                if (!newValue.equals(textArea.getText())) {
                    System.out.println("文本框内容已经变化，不执行翻译");
                    return;
                }

                untranslatedTextAreaEvent.translated(newValue);

            }));
            translationTimeline.play();
        });


        TitledPane titledPane = new TitledPane("", textArea);
        // 隐藏标题栏
        titledPane.setCollapsible(false);


        ObservableList<Node> children = vBox.getChildren();
        children.add(titledPane);


        translatePanes.forEach(f -> children.add(f.pane()));


        stage.setScene(new Scene(vBox, 400, 500));

        // 添加关闭请求处理程序
        stage.setOnCloseRequest(event -> {
            event.consume(); // 阻止默认关闭操作
            stage.hide(); // 隐藏窗口
        });

        // 窗口隐藏时，清空文本框内容
        stage.setOnHidden(event -> translatePaneService.clear());

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
        settingsMenuItem.setOnAction(event -> {
            // 显示设置面板
            showSettingsPanel();
        });

        // 添加设置菜单项到菜单栏
        Menu settingsMenu = new Menu("设置");
        settingsMenu.getItems().add(settingsMenuItem);
        menuBar.getMenus().add(settingsMenu);

        // 添加菜单栏到布局
        vBox.getChildren().add(menuBar);
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
            }else if ("opus-mt-en-zh".equals(newValue)) {
                // 显示opus-mt-en-zh设置面板
                showOpusMtEnZhSettings(settingsRoot);
            }else if ("chatgpt".equals(newValue)) {
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
            alert.setContentText("配置已成功保存。");
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
     * 更新baiduTextArea的内容
     */
    public void updateBaiduTextArea(String text) {
        Platform.runLater(() -> baiduTextArea.setText(text));
    }

    /**
     * 更新opusMtTextArea的内容
     */
    public void updateOpusMtTextArea(String text) {
        Platform.runLater(() -> opusMtTextArea.setText(text));
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
