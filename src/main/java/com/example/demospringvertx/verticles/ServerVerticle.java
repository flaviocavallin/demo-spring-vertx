package com.example.demospringvertx.verticles;

import com.example.demospringvertx.ApplicationConfiguration;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Override
    public void start(Promise<Void> promise) {
        LOGGER.info("ServerVerticle - START");

        Router router = Router.router(vertx);
        router.get("/api/user").handler(this::getAllUsersHandler);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(applicationConfiguration.httpPort(), result -> {
                    if (result.succeeded()) {
                        LOGGER.info("HTTP server running on port={}", applicationConfiguration.httpPort());
                        promise.complete();
                    } else {
                        LOGGER.error("HTTP Server not started!!!", result.cause());
                        promise.fail(result.cause());
                    }
                });
    }

    private void getAllUsersHandler(RoutingContext routingContext) {
        LOGGER.info("ServerVerticle - getAllUsersHandler");

        vertx.eventBus()
                .<String>send("getAllUsers", "", result -> {
                    if (result.succeeded()) {
                        LOGGER.info("ServerVerticle - succeeded");
                        routingContext.response()
                                .putHeader("content-type", "application/json")
                                .setStatusCode(200)
                                .end(result.result()
                                        .body());
                    } else {
                        LOGGER.info("ServerVerticle - ERRRORRRRR");
                        routingContext.response()
                                .setStatusCode(500)
                                .end();
                    }
                });
    }
}
