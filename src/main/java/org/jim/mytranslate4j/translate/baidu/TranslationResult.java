package org.jim.mytranslate4j.translate.baidu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author jim
 */
@Data
@Accessors(chain = true)
public class TranslationResult {
    private String from;

    private String to;


    /**
     * trans_result
     */
    @JsonProperty(value = "trans_result")
    private List<TransResultItem> transResult;

}
