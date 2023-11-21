package org.jim.mytranslate4j.extension;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Objects;


/**
 * @author James Smith
 */
@Slf4j
public class ExtensionClassLoader extends URLClassLoader {
    public ExtensionClassLoader() {
        super(new URL[]{});

        // 解压未解压的扩展
        unzip();

        File pluginFolder = new File("extensions");

        if (!pluginFolder.exists()) {
            log.error("extensions folder not exists");
            return;
        }

        // get all jar files in the extension subfolder
        File[] files = Arrays.stream(pluginFolder.listFiles((dir, name) -> !name.endsWith(".zip")))
                // get subfolder
                .map(file -> file.listFiles((dir, name) -> name.endsWith(".jar")))
                .filter(Objects::nonNull)
                // to array
                .flatMap(Arrays::stream)
                .toArray(File[]::new);


        if (files == null) {
            log.info("there is no extension zip file");
            return;
        }

        for (File file : files) {
            try {
                addURL(file.toURI().toURL());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

/*        Executors.newFixedThreadPool(1)
                .execute(() -> {
                    try {
                        ExtensionLoader extensionLoader = new ExtensionLoader();
                        extensionLoader.loaderPlugin();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });*/

    }


    /**
     * 解压未解压的扩展
     */
    private void unzip() {
        File zipFolder = new File("extensions");

        if (!zipFolder.exists()) {
            log.error("extensions folder not exists");
            return;
        }

        File[] zipFiles = zipFolder.listFiles((dir, name) -> name.endsWith(".zip"));
        if (zipFiles == null) {
            log.info("there is no extension zip file");
            return;
        }

        Arrays.stream(zipFiles)
                // 过滤已经解压的文件
                .filter(f -> {
                    return true;
                })
                .forEach(f -> {
                    ZipUtil.unzip(f, f.getParentFile().getAbsolutePath());
                });


    }


}
