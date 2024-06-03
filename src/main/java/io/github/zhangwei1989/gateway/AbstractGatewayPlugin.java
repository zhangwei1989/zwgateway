package io.github.zhangwei1989.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * AbstractGatewayPlugin
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/6/2
 */
@Slf4j
public abstract class AbstractGatewayPlugin implements GatewayPlugin {

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean support(ServerWebExchange exchange) {
        return doSupport(exchange);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        boolean supported = doSupport(exchange);
        log.info("======> [ZW Gateway] plugin[{}], support= {}", this.getName(), supported);
        return supported ? doHandle(exchange) : Mono.empty();
    }

    public abstract boolean doSupport(ServerWebExchange exchange);

    public abstract Mono<Void> doHandle(ServerWebExchange exchange);

}
