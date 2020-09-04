package com.app.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Configuration
// Nasza klasa ma pierwszenstwo przed DefaultErrorWebExceptionHandler
@Order(-2)
@RequiredArgsConstructor
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {

        DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();
        if (throwable instanceof AppException) {
            DataBuffer dataBuffer = null;
            try {
                dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(new AppExceptionResponse(throwable.getMessage())));
            } catch (JsonProcessingException e) {
                dataBuffer = bufferFactory.wrap("".getBytes());
            }
            serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
        }

        serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        DataBuffer dataBuffer = bufferFactory.wrap("Unclassified error!".getBytes());
        return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

}
