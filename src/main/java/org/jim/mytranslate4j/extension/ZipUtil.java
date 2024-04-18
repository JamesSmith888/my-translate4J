package org.jim.mytranslate4j.extension;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author James Smith
 */
@Slf4j
public class ZipUtil {

    /**
     * 解压zip文件时，基于zip文件名创建目录
     */
    public static void unzip(File zipFile, String targetDir) {
        unzip(zipFile.getAbsolutePath(), zipFile.getName().replace(".zip", ""), targetDir);
    }

    public static void unzip(String zipFile, String zipName, String targetDir) {
        log.info("unzip zipFile: {}, zipName: {}, targetDir: {}", zipFile, zipName, targetDir);

        // 创建解压目标目录
        String dirName = targetDir + "/" + zipName;
        File file = new File(dirName);
        if (!file.exists()) {
            file.mkdirs();
        }


        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            byte[] buffer = new byte[1024];
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                File entryFile = new File(dirName + "/" + entryName);
                log.info("unzip entry: {}", entryFile);

                if (!entry.isDirectory()) {
                    try (FileOutputStream fos = new FileOutputStream(entryFile)) {
                        int bytesRead;
                        while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }
            log.info("unzip success");
        } catch (IOException e) {
            log.error("unzip error", e);
            e.printStackTrace();
        }
    }
}
