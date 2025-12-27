package org.dromara.compliance.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAI Embedding API配置
 *
 * @author compliance
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "compliance.openai")
public class OpenAIEmbeddingConfig {

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 向量模型
     */
    private String embeddingModel = "text-embedding-3-small";

    /**
     * API地址
     */
    private String apiUrl = "https://api.openai.com/v1/embeddings";

    /**
     * 超时时间（毫秒）
     */
    private Integer timeout = 30000;
}
