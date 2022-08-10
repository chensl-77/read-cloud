package top.csl.read.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 17:08
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "system.properties")
public class SystemPropertiesConfig {

    /** 请求白名单 */
    private String whitelist;
}
