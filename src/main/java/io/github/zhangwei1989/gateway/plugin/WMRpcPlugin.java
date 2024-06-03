package io.github.zhangwei1989.gateway.plugin;

import cn.william.wmrpc.core.api.LoadBalancer;
import cn.william.wmrpc.core.api.RegistryCenter;
import cn.william.wmrpc.core.cluster.RoundRibbonLoadBalancer;
import cn.william.wmrpc.core.meta.InstanceMeta;
import cn.william.wmrpc.core.meta.ServiceMeta;
import io.github.zhangwei1989.gateway.AbstractGatewayPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * WMRpcPlugin
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/6/2
 */
@Slf4j
@Component("wmrpc")
public class WMRpcPlugin extends AbstractGatewayPlugin {

    public static final String NAME = "wmrpc";

    private String prefix = GATEWAY_PREFIX + "/" + NAME + "/";

    @Autowired
    RegistryCenter rc;

    LoadBalancer<InstanceMeta> loadBalancer = new RoundRibbonLoadBalancer();

    @Override
    public boolean doSupport(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(prefix);
    }

    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange) {
        log.info("======>[ZW Gateway] WMRpcPlugin ......");
        String service = exchange.getRequest().getPath().value().substring(prefix.length());
        ServiceMeta serviceMeta = ServiceMeta.builder()
                .name(service)
                .app("app1")
                .env("dev")
                .namespace("public")
                .version("1.0")
                .build();
        List<InstanceMeta> instanceMetas = rc.fetchAll(serviceMeta);
        InstanceMeta instanceMeta = loadBalancer.choose(instanceMetas);

        String url = instanceMeta.http();

        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();

        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);
        Mono<String> body = entity.map(ResponseEntity::getBody);

        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("zw.gw.version", "v1.0.0");

        return body.flatMap(x -> exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))));
    }

    @Override
    public String getName() {
        return NAME;
    }

}
