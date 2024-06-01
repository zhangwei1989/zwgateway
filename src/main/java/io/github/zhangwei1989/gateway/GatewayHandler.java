package io.github.zhangwei1989.gateway;

import cn.william.wmrpc.core.api.LoadBalancer;
import cn.william.wmrpc.core.api.RegistryCenter;
import cn.william.wmrpc.core.cluster.RoundRibbonLoadBalancer;
import cn.william.wmrpc.core.meta.InstanceMeta;
import cn.william.wmrpc.core.meta.ServiceMeta;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * gateway handler
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/5/31
 */
@Component
public class GatewayHandler {

    @Autowired
    RegistryCenter rc;

    LoadBalancer<InstanceMeta> loadBalancer = new RoundRibbonLoadBalancer();

    Mono<ServerResponse> handle(ServerRequest request) {
        String service = request.path().substring(4);
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

        Mono<String> requestMono = request.bodyToMono(String.class);
        return requestMono.flatMap(x -> invokeFromRegistry(x, url));
    }

    private static Mono<ServerResponse> invokeFromRegistry(String x, String url) {
        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .bodyValue(x).retrieve().toEntity(String.class);
        return ServerResponse.ok()
                .header("Content-Type", "application/json")
                .header("zw.gw.version", "v1.0.0")
                .body(entity.map(ResponseEntity::getBody), String.class);
    }

}
