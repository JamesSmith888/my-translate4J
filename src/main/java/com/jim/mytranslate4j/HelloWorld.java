package com.jim.mytranslate4j;

import jakarta.annotation.Resource;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class HelloWorld extends Application {
    private double startX;
    private double startY;
    private Rectangle selectionRectangle;

    @Resource
    private ScreenCapture screenCapture;


    public static void main(String[] args) throws AWTException {
        launch(args);

        new ScreenCapture().start(new Stage());
    }

    @Override
    public void start(Stage primaryStage) {
        initUI(primaryStage);


    }


    /**
     * 初始化界面
     */
    private void initUI(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");

        VBox root = new VBox();


        TextArea textArea = new TextArea();
        // 监听文本框的变化
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("oldValue = " + oldValue);
            System.out.println("newValue = " + newValue);
        });

        TitledPane tilePane1 = new TitledPane("来源", textArea);
        TitledPane tilePane2 = new TitledPane("baidu", new Button("button2"));
        TitledPane tilePane3 = new TitledPane("opus-mt-en-zh", new Button("button3"));

        Accordion accordion = new Accordion();

        accordion.getPanes().addAll(tilePane1, tilePane2, tilePane3);

        // 设置默认展开的组件
        accordion.setExpandedPane(tilePane1);

        ObservableList<Node> children = root.getChildren();
        children.addAll(accordion);


        primaryStage.setScene(new Scene(root, 400, 500));
        primaryStage.show();
    }
}
