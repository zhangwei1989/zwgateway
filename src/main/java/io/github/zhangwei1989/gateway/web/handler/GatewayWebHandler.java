package io.github.zhangwei1989.gateway.web.handler;

import cn.william.wmrpc.core.api.LoadBalancer;
import cn.william.wmrpc.core.api.RegistryCenter;
import cn.william.wmrpc.core.cluster.RoundRibbonLoadBalancer;
import cn.william.wmrpc.core.meta.InstanceMeta;
import cn.william.wmrpc.core.meta.ServiceMeta;
import io.github.zhangwei1989.gateway.DefaultGatewayPluginChain;
import io.github.zhangwei1989.gateway.GatewayPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * gateway web handler
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/6/1
 */
@Slf4j
@Component("gatewayWebHandler")
public class GatewayWebHandler implements WebHandler {

    @Autowired
    List<GatewayPlugin> plugins;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        log.info("======> [ZW Gateway] web handler......");

        if (plugins == null || plugins.isEmpty()) {
            String mock = """
                    {
                        "result" : "no plugin"
                    }
                    """;
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
        }

        return new DefaultGatewayPluginChain(plugins).handle(exchange);
    }

}
