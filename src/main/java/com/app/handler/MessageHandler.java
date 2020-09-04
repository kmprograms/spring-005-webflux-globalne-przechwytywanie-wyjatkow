package com.app.handler;

import com.app.exception.AppException;
import com.app.model.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class MessageHandler {

    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().body(createMessage(), Message.class);
    }

    private Mono<Message> createMessage() {
        if (Math.random() > 0.5) {
            throw new AppException("Random custom exception!");
        }
        return Mono.just(Message
                .builder()
                .text("This is custom message")
                .build());
    }
}
