package com.app.router;

import com.app.handler.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MessageRouter {

    @Bean
    public RouterFunction<ServerResponse> routeCity(MessageHandler messageHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/message")
                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                messageHandler::hello);
    }

}
