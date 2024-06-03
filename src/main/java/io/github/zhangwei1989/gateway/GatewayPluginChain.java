package io.github.zhangwei1989.gateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * GatewayPluginChain
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/6/3
 */
public interface GatewayPluginChain {

    Mono<Void> handle(ServerWebExchange exchange);

}
