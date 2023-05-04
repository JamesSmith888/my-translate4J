package com.jim.mytranslate4j.test;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class SelectedTextCapture {

    public static void main(String[] args) {
        try {
            System.out.println(getSelectedText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSelectedText() throws IOException, UnsupportedFlavorException {
        String selectedText = "";
        try {
            Robot robot = new Robot();
            // Simulate CTRL + C (copy) key press
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_C);
            robot.keyRelease(KeyEvent.VK_C);
            robot.keyRelease(KeyEvent.VK_CONTROL);

            // Wait for the text to be copied to the clipboard
            Thread.sleep(500);

            // Get the text from the clipboard
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = clipboard.getContents(null);
            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                selectedText = (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
        }
        return selectedText;
    }
}
