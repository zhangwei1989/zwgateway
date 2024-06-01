package io.github.zhangwei1989.gateway;

import cn.william.wmrpc.core.api.RegistryCenter;
import cn.william.wmrpc.core.registry.zw.ZwRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GatewayConfig
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/6/1
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RegistryCenter rc() {
        return new ZwRegistryCenter();
    }

}
