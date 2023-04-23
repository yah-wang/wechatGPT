package com.yshines.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信配置
 *
 * @author ashinnotfound
 * @date 2023/03/19
 */
@Data
@Component
@ConfigurationProperties("wechat")
public class WechatConfig {
    private String qrPath;
}
