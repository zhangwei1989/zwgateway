package io.github.zhangwei1989.gateway;

import cn.william.wmrpc.core.api.RegistryCenter;
import cn.william.wmrpc.core.registry.zw.ZwRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

import static io.github.zhangwei1989.gateway.GatewayPlugin.GATEWAY_PREFIX;

/**
 * Description for this class.
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/5/31
 */
@Slf4j
@Configuration
public class GatewayConfig {

    @Bean
    public RegistryCenter rc() {
        return new ZwRegistryCenter();
    }

    @Bean
    ApplicationRunner runner(@Autowired ApplicationContext context) {
        return args -> {
            SimpleUrlHandlerMapping handlerMapping = context.getBean(SimpleUrlHandlerMapping.class);
            Properties mappings = new Properties();
            mappings.put(GATEWAY_PREFIX + "/**", "gatewayWebHandler");
            handlerMapping.setMappings(mappings);
            handlerMapping.initApplicationContext();
            log.info("wmrpc gateway started");
        };
    }

}
