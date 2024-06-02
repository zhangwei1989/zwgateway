package io.github.zhangwei1989.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * GatewayPreWebFilter
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/6/2
 */
@Slf4j
@Component
public class GatewayPreWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("======> [ZW Gateway] web pre filter ......");

        if (exchange.getRequest().getQueryParams().getFirst("mock") == null) {
            return chain.filter(exchange);
        }

        String mock = """
                {
                    "result" : "mock"
                }
                """;

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
    }

}
