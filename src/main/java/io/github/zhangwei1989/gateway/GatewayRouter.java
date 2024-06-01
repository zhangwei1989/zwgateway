package io.github.zhangwei1989.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * GatewayRouter
 *
 * @Author : zhangwei(331874675@qq.com)
 * @Create : 2024/6/1
 */
@Component
public class GatewayRouter {

    @Autowired
    GatewayHandler gatewayHandler;

    @Bean
    public RouterFunction<?> gatewayRouterFunction() {
        return route(GET("/gw").or(POST("/gw/**")),
                gatewayHandler::handle);
    }

}
