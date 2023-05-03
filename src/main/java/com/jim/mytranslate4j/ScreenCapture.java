package com.jim.mytranslate4j;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenCapture extends Application {

    private double startX;
    private double startY;
    private Rectangle selectionRectangle;
    private Pane pane;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        pane = new Pane();
        Scene scene = new Scene(pane, Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setFullScreen(true);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                try {
                    startScreenCapture();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        });

        selectionRectangle = createSelectionRectangle();
        pane.getChildren().add(selectionRectangle);

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            startX = event.getScreenX();
            startY = event.getScreenY();
            selectionRectangle.setX(event.getX());
            selectionRectangle.setY(event.getY());
            selectionRectangle.setWidth(0);
            selectionRectangle.setHeight(0);
        });

        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            double offsetX = event.getScreenX() - startX;
            double offsetY = event.getScreenY() - startY;
            selectionRectangle.setWidth(Math.abs(offsetX));
            selectionRectangle.setHeight(Math.abs(offsetY));
            if (offsetX < 0) {
                selectionRectangle.setX(event.getX());
            }
            if (offsetY < 0) {
                selectionRectangle.setY(event.getY());
            }
        });

        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            try {
                captureAndSaveScreenshot(selectionRectangle);
            } catch (AWTException | IOException e) {
                e.printStackTrace();
            }
        });

        primaryStage.show();
    }

    private void startScreenCapture() throws AWTException {
        Robot robot = new Robot();
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        BufferedImage screenCapture = robot.createScreenCapture(new java.awt.Rectangle((int) screenBounds.getWidth(), (int) screenBounds.getHeight()));
        WritableImage screenImage = SwingFXUtils.toFXImage(screenCapture, null);

        pane.getChildren().clear();
        pane.getChildren().add(new ImageView(screenImage));
    }

    private Rectangle createSelectionRectangle() {
        Rectangle rect = new Rectangle();
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.BLACK);
        rect.getStrokeDashArray().addAll(3.0, 7.0, 3.0, 7.0);
        return rect;
    }

    private void captureAndSaveScreenshot(Rectangle selectionRectangle) throws AWTException, IOException {

        int x = (int) selectionRectangle.getX();
        int y = (int) selectionRectangle.getY();
        int width = (int) selectionRectangle.getWidth();
        int height = (int) selectionRectangle.getHeight();

        Robot robot = new Robot();
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        BufferedImage screenCapture = robot.createScreenCapture(new java.awt.Rectangle(x, y, width, height));
        WritableImage croppedImage = SwingFXUtils.toFXImage(screenCapture, null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(croppedImage, null);

        try {
            Path tempDir = Paths.get("temp");
            if (!Files.exists(tempDir)) {
                Files.createDirectories(tempDir);
            }
            File outputFile = new File(tempDir.toFile(), "screenshot.png");
            ImageIO.write(bufferedImage, "png", outputFile);
            System.out.println("Screenshot saved at: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.close();
    }
}
