package org.jim.mytranslate4j.extension;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * @author James Smith
 */
@Slf4j
public class ExtensionClassLoader extends URLClassLoader {

    public static Map<String, List<String>> classMap = Maps.newHashMap();

    public ExtensionClassLoader() throws IOException {
        super(new URL[]{});

        // 解压未解压的扩展
        unzip();

        File pluginFolder = new File("extensions");

        if (!pluginFolder.exists()) {
            log.error("extensions folder not exists");
            return;
        }
        log.info("pluginFolder path: {}", pluginFolder.getAbsolutePath());

        // get all jar files in the extension subfolder
        File[] files = Arrays.stream(pluginFolder.listFiles((dir, name) -> !name.endsWith(".zip")))
                .filter(Objects::nonNull)
                // TODO 只保留jar文件
                .toArray(File[]::new);


        for (File file : files) {
            log.info("load jar file: {}", file.getAbsolutePath());

            URL urls = new URL("jar:" + file.toURI().toURL() + "!/");

            addURL(urls);

            classMap.put(file.getName(), getJarClass(file));
        }
    }

    /**
     * 获取jar中类的类路径
     */
    public List<String> getJarClass(File file) throws IOException {
        // 打开JAR文件
        JarFile jarFile = new JarFile(file);

        // 获取JAR文件中的所有条目（包括目录和文件）
        Enumeration<JarEntry> entries = jarFile.entries();


        List<String> classList = Lists.newArrayList();


        // 遍历所有条目
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            // 检查条目是否在org.jim包下
            if (entry.getName().startsWith("org/jim/") && entry.getName().endsWith(".class")) {

                // 打印类名（删除.class后缀并将/替换为.）
                String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                classList.add(className);
            }

        }

        // 关闭JAR文件
        jarFile.close();

        return classList;
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
                // TODO 过滤已经解压的文件
                .filter(f -> {
                    return true;
                })
                .forEach(f -> {
                    ZipUtil.unzip(f, f.getParentFile().getAbsolutePath());
                });


    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/').concat(".class");
        log.info("path: {}", path);
        return super.findClass(name);
    }
}
