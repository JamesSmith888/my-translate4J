package org.jim.mytranslate4j.translate.baidu;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jim
 */
@Data
@Accessors(chain = true)
public class TransResultItem {
    private String src;

    private String dst;
}
