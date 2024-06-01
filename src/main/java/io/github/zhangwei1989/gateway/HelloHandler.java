package io.github.zhangwei1989.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * hello handler
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/5/31
 */
@Component
public class HelloHandler {

    Mono<ServerResponse> handle(ServerRequest request) {
        String url = "http://localhost:8081/wmrpc";
        String requestJSON = """
                {
                    "service":"cn.william.wmrpc.demo.api.UserService",
                    "methodSign":"findById@1_int",
                    "args":[100]
                }
                """;

        WebClient client = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = client.post()
                .header("Content-Type", "application/json")
                .bodyValue(requestJSON).retrieve().toEntity(String.class);

        return ServerResponse.ok()
                .header("Content-Type", "application/json")
                .header("zw.gw.version", "v1.0.0")
                .body(entity.map(ResponseEntity::getBody), String.class);
    }

}
