package com.jim.mytranslate4j.gui;

import com.jim.mytranslate4j.config.Config;
import jakarta.annotation.Resource;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.MediaTracker;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * @author jim
 */
@Component
public class Start {

    @Resource
    private ScreenCapture screenCapture;

    @Resource
    private Config config;

    private TextArea textArea;

    private TextArea baiduTextArea;

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
        Stage stage = new Stage();
        stage.setTitle("My Translate");
        // 设置任务栏图标
        javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResourceAsStream("/img/icon.png"));
        stage.getIcons().add(image);

        VBox vBox = new VBox();

        // 菜单栏
        initMenuBar(vBox);

        // 来源文本框
        textArea = new TextArea();
        // 监听文本框的变化
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("oldValue = " + oldValue);
            System.out.println("newValue = " + newValue);
        });
        TitledPane tilePane1 = new TitledPane("来源", textArea);

        // baidu 翻译结果文本框
        baiduTextArea = new TextArea();
        TitledPane tilePane2 = new TitledPane("baidu", baiduTextArea);

        TitledPane tilePane3 = new TitledPane("opus-mt-en-zh", new Button("button3"));

        vBox.getChildren().addAll(tilePane1, tilePane2, tilePane3);


        stage.setScene(new Scene(vBox, 400, 500));

        // 添加关闭请求处理程序
        stage.setOnCloseRequest(event -> {
            event.consume(); // 阻止默认关闭操作
            stage.hide(); // 隐藏窗口
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
        settingsMenu.getItems().addAll("Baidu翻译", "Google翻译", "opus-mt-en-zh");
        settingsMenu.setPrefWidth(100);

        // 设置选中事件
        settingsMenu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Baidu翻译".equals(newValue)) {
                // 显示Baidu翻译设置面板
                showBaiduSettings(settingsRoot);
            } else if ("Google翻译".equals(newValue)) {
                // 显示Google翻译设置面板
                showGoogleSettings(settingsRoot);
            }
        });


        // 将垂直菜单栏添加到布局的左侧
        settingsRoot.setLeft(settingsMenu);

        settingsStage.setScene(new Scene(settingsRoot, 500, 300));
        settingsStage.show();
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
     * 更新textArea的内容
     */
    public void updateTextArea(String text) {
        textArea.setText(text);
    }

    /**
     * 更新baiduTextArea的内容
     */
    public void updateBaiduTextArea(String text) {
        baiduTextArea.setText(text);
    }

    /**
     * 判断窗口是否已经显示，如果没有显示，则显示窗口
     */
    public void showWindow() {
        Stage stage = (Stage) textArea.getScene().getWindow();
        if (!stage.isShowing()) {
            stage.show();
        }
    }


    private void initTrayIcon(Stage stage) {
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/icon.png"));

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
