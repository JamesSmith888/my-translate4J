package org.jim.mytranslate4j.plugin;

import org.jim.TestTranslatePlugin;
import org.jim.TranslatePlugin;
import org.springframework.stereotype.Component;

import java.util.ServiceLoader;

/**
 * @author jim
 */
@Component
public class PluginLoader {


    public ServiceLoader<TranslatePlugin> loaderPlugin() {
        ServiceLoader<TranslatePlugin> loader = ServiceLoader.load(TranslatePlugin.class);

        loader.forEach(f->{
            System.out.println(f instanceof TestTranslatePlugin);

            System.out.println(f);
        });

        return loader;
    }
}

