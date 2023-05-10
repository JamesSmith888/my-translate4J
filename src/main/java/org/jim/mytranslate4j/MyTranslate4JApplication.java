package org.jim.mytranslate4j;

import org.jim.mytranslate4j.gui.Start;
import org.jim.mytranslate4j.util.SpringContextUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author jim
 */
@EnableAsync
@SpringBootApplication
public class MyTranslate4JApplication extends Application {

    public static void main(String[] args) {
        // 确保是在有的界面环境下运行
        System.setProperty("java.awt.headless", "false");


        SpringApplication.run(MyTranslate4JApplication.class, args);


        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.runLater(() -> {
            // 从spring容器中获取Start对象
            Start start = SpringContextUtils.getBean(Start.class);


            start.start();
        });
    }
}
