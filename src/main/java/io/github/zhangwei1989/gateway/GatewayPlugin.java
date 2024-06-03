package io.github.zhangwei1989.gateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * GatewayPlugin
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/6/2
 */
public interface GatewayPlugin {

    String GATEWAY_PREFIX = "/gw";

    void start();

    void stop();

    String getName();

    boolean support(ServerWebExchange exchange);

    Mono<Void> handle(ServerWebExchange exchange);

}
