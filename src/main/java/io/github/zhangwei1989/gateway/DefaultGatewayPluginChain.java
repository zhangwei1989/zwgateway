package io.github.zhangwei1989.gateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * DefaultGatewayPluginChain
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/6/3
 */
public class DefaultGatewayPluginChain implements GatewayPluginChain{

    List<GatewayPlugin> plugins;

    int index = 0;

    public DefaultGatewayPluginChain(List<GatewayPlugin> plugins) {
        this.plugins = plugins;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            if (index < plugins.size()) {
                return plugins.get(index++).handle(exchange, this);
            }

            return Mono.empty();
        });
    }

}
