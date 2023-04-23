package com.yshines.config;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import com.yshines.handler.WechatMessageHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import cn.zhouyafeng.itchat4j.Wechat;

/**
 * bot配置
 *
 * @author ashinnotfound
 * @date 2023/02/13
 */
@Slf4j
@Data
@Component
public class BotConfig {
    @Resource
    ProxyConfig proxyConfig;
    @Resource
    WechatConfig wechatConfig;
    @Resource
    ChatgptConfig chatgptConfig;

    @Resource
    private WechatMessageHandler wechatMessageHandler;

    private List<OpenAiService> openAiServiceList;
    private ChatMessage basicPrompt;
    private Integer maxToken;
    private Double temperature;
    private String model;

    @PostConstruct
    public void init() {
        //配置代理
        if (null != proxyConfig.getHost() && !"".equals(proxyConfig.getHost())) {
            System.setProperty("http.proxyHost", proxyConfig.getHost());
            System.setProperty("https.proxyHost", proxyConfig.getHost());
        }
        if (null != proxyConfig.getPort() && !"".equals(proxyConfig.getPort())) {
            System.setProperty("http.proxyPort", proxyConfig.getPort());
            System.setProperty("https.proxyPort", proxyConfig.getPort());
        }

        //ChatGPT
        model = "gpt-3.5-turbo";
//        model = "gpt-3.5-turbo-0301" 这是快照版本
        maxToken = 2048;
        temperature = 0.8;
//        你可以通过设定basicPrompt来指定人格
//        basicPrompt = new ChatMessage("system", "接下来在我向你陈述一件事情时，你只需要回答：“典”");
        openAiServiceList = new ArrayList<>();
        for (String apiKey : chatgptConfig.getApiKey()) {
            apiKey = apiKey.trim();
            if (!"".equals(apiKey)) {
                openAiServiceList.add(new OpenAiService(apiKey, Duration.ofSeconds(1000)));
                log.info("apiKey为 {} 的账号初始化成功", apiKey);
            }
        }

        //微信
        log.info("正在登录微信,请按提示操作：");
        Wechat wechatBot = new Wechat(wechatMessageHandler, wechatConfig.getQrPath());
        wechatBot.start();
    }
}
