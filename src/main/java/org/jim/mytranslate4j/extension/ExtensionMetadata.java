package org.jim.mytranslate4j.extension;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 插件元数据
 *
 * @author James Smith
 */
@Data
@Accessors(chain = true)
public class ExtensionMetadata {


    /**
     * 插件的名称，用于标识插件。
     */
    private String name;

    /**
     * 插件的版本号。
     */
    private String version;

    /**
     * 插件的作者或开发者信息。
     */
    private String author;

    /**
     * 对插件的简要描述，说明其功能或用途。
     */
    private String description;

    /**
     * 插件的主类或入口点类，用于加载插件。
     */
    private String mainClass;

    /**
     * 如果插件依赖于其他插件或库，可以列出这些依赖项的信息。
     */
    private String dependencies;

    /**
     * 插件的许可证信息。
     */
    private String license;

    /**
     * 插件的图标或标识。
     */
    private String icon;

    /**
     * 插件的官方网站或文档链接。
     */
    private String website;

    /**
     * 插件的源代码仓库链接。
     */
    private String repository;

    /**
     * 关键字或标签，用于分类或搜索插件。
     */
    private String tags;

    /**
     * 指定插件与特定应用程序版本的兼容性信息。
     */
    private String compatibility;

}
