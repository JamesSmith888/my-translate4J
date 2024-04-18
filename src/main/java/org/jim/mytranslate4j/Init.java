package org.jim.mytranslate4j;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jim.mytranslate4j.ai.imagecaptioning.ImageCaptioningService;
import org.jim.mytranslate4j.extension.ExtensionLoader;
import org.jim.mytranslate4j.translate.opus.OpusService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author jim
 * 容器启动时初始化
 */
@Component
@Slf4j
public class Init implements CommandLineRunner {

    @Resource
    private ExtensionLoader extensionLoader;

    @Resource
    private ImageCaptioningService imageCaptioningService;

    @Resource
    private OpusService opusService;


    @Override
    public void run(String... args) throws Exception {
        // 启动ImageCaptioningService
        //imageCaptioningService.init();

        // 加载插件/扩展
        extensionLoader.loaderPlugin();

        // 启动OpusMtEnZhTranslateService
        //opusService.init();

    }
}
