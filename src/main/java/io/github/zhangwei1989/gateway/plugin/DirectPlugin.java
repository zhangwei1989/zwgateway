package io.github.zhangwei1989.gateway.plugin;

import io.github.zhangwei1989.gateway.AbstractGatewayPlugin;
import io.github.zhangwei1989.gateway.GatewayPluginChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * DirectPlugin
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/6/3
 */
@Slf4j
@Component("direct")
public class DirectPlugin extends AbstractGatewayPlugin {

    public static final String NAME = "direct";

    private String prefix = GATEWAY_PREFIX + "/" + NAME + "/";

    @Override
    public boolean doSupport(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(prefix);
    }

    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain) {
        log.info("======>[ZW Gateway] DirectPlugin ......");
        String backend = exchange.getRequest().getQueryParams().getFirst("backend");
        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();

        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("zw.gw.version", "v1.0.0");
        exchange.getResponse().getHeaders().add("zw.gw.plugin", getName());

        if (backend == null || backend.isEmpty()) {
            return requestBody.flatMap(x -> exchange.getResponse().writeWith(Mono.just(x))).then(chain.handle(exchange));
        }

        WebClient client = WebClient.create(backend);
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);
        Mono<String> body = entity.map(ResponseEntity::getBody);

        return body.flatMap(x -> exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))))
                .then(chain.handle(exchange));
    }

    @Override
    public String getName() {
        return NAME;
    }
}
