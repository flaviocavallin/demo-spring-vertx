package com.example.demospringvertx.verticles;

import com.example.demospringvertx.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserVerticle.class);

    @Autowired
    private UserService userService;
    private final ObjectMapper mapper = Json.mapper;

    @Override
    public void start(Promise<Void> promise) {
        LOGGER.info("UserVerticle - START");
        vertx.eventBus()
                .<String>consumer("getAllUsers")
                .handler(getAllArticleService(userService));
        promise.complete();
    }

    private Handler<Message<String>> getAllArticleService(UserService service) {
        LOGGER.info("UserVerticle - getAllArticleService");

        return msg -> vertx.<String>executeBlocking(future -> {
            try {
                future.complete(mapper.writeValueAsString(service.getAllUsers()));
            } catch (JsonProcessingException e) {
                LOGGER.info("UserVerticle - Failed to serialize result");
                future.fail(e);
            }
        }, result -> {
            if (result.succeeded()) {
                LOGGER.info("UserVerticle - succeeded");
                msg.reply(result.result());
            } else {
                LOGGER.info("UserVerticle - ERRRORR");
                msg.reply(result.cause()
                        .toString());
            }
        });
    }
}
