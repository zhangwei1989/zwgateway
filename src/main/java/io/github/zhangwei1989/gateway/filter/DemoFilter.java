package io.github.zhangwei1989.gateway.filter;

import io.github.zhangwei1989.gateway.GatewayFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Description for this class.
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/6/3
 */
@Slf4j
@Component("demoFilter")
public class DemoFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange) {
        log.info("======> filters: demo filter ......");
        exchange.getRequest().getHeaders().forEach((k, v) -> System.out.println(k + ":" + v));
        return Mono.empty();
    }

}
