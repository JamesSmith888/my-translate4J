package com.jim.mytranslate4j.gui;

import com.jim.mytranslate4j.event.ScreenCaptureEvent;
import jakarta.annotation.Resource;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author jim
 */
@Component
public class ScreenCapture {

    private double startX;
    private double startY;
    private Rectangle selectionRectangle;

    @Resource
    private GlobalKeyListener globalKeyListener;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 弹出截屏界面
     */
    public void showOverlay() {
        Stage primaryStage = new Stage();

        Pane pane = new Pane();
        Scene scene = new Scene(pane);

        pane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.5), CornerRadii.EMPTY, Insets.EMPTY)));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Custom Screenshot Tool");
        primaryStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());

        // Keep the primaryStage on top of all other windows
        primaryStage.setAlwaysOnTop(true);

        primaryStage.show();

        scene.setOnMousePressed(event -> onMousePressed(event, pane));
        scene.setOnMouseDragged(this::onMouseDragged);
        scene.setOnMouseReleased(event -> onMouseReleased(event, primaryStage));

        // ESC取消截屏
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                primaryStage.close();
            }
        });


    }


    private void onMousePressed(MouseEvent event, Pane pane) {
        if (event.getButton() == MouseButton.PRIMARY) {
            pane.getChildren().remove(selectionRectangle);

            startX = event.getX();
            startY = event.getY();

            selectionRectangle = new Rectangle(startX, startY, 0, 0);
            selectionRectangle.setStroke(Color.BLUE);
            selectionRectangle.setFill(Color.TRANSPARENT);
            selectionRectangle.setStrokeWidth(1);

            pane.getChildren().add(selectionRectangle);
        }
    }

    private void onMouseDragged(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double currentX = event.getX();
            double currentY = event.getY();

            selectionRectangle.setWidth(Math.abs(currentX - startX));
            selectionRectangle.setHeight(Math.abs(currentY - startY));
            selectionRectangle.setX(Math.min(currentX, startX));
            selectionRectangle.setY(Math.min(currentY, startY));
        }
    }

    private void onMouseReleased(MouseEvent event, Stage primaryStage) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double width = selectionRectangle.getWidth();
            double height = selectionRectangle.getHeight();

            if (width > 0 && height > 0) {
                primaryStage.hide();
                captureAndSaveScreenshot(primaryStage);
            }
        }
    }


    /**
     * 截取用户选择的区域并保存到文件
     */
    private void captureAndSaveScreenshot(Stage primaryStage) {
        // Hide the primaryStage before capturing
        primaryStage.hide();

        // Capture the screen using Robot
        Rectangle2D selectionBounds = new Rectangle2D(selectionRectangle.getX(), selectionRectangle.getY(), selectionRectangle.getWidth(), selectionRectangle.getHeight());
        Robot robot = new Robot();
        javafx.scene.image.Image capturedImage = robot.getScreenCapture(null, selectionBounds);

        // Convert the Image to a WritableImage
        javafx.scene.image.WritableImage snapshot = new javafx.scene.image.WritableImage((int) selectionRectangle.getWidth(), (int) selectionRectangle.getHeight());
        PixelReader reader = capturedImage.getPixelReader();
        PixelWriter writer = snapshot.getPixelWriter();

        for (int y = 0; y < (int) selectionRectangle.getHeight(); y++) {
            for (int x = 0; x < (int) selectionRectangle.getWidth(); x++) {
                Color color = reader.getColor(x, y);
                writer.setColor(x, y, color);
            }
        }

        // Save the snapshot to the project root folder
        File outputDirectory = new File("temp");
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }

        File outputFile = new File(outputDirectory, "screenshot.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        primaryStage.close();

        // 发起已经截图的事件
        applicationEventPublisher.publishEvent(new ScreenCaptureEvent(this));
    }


    public void init() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(e.getMessage());
            System.exit(1);
        }

        // Disable the default console output to avoid the native key events spamming in the console.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);

        // Instantiate GlobalKeyListener
        GlobalScreen.addNativeKeyListener(globalKeyListener);

    }


}

