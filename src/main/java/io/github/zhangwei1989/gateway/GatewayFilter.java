package io.github.zhangwei1989.gateway;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * GatewayFilter
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/6/3
 */
public interface GatewayFilter {

    Mono<Void> filter(ServerWebExchange exchange);

}
