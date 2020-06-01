package com.example.demospringvertx.verticles;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LauncherVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(LauncherVerticle.class);

    @Autowired
    private ServerVerticle serverVerticle;

    @Autowired
    private UserVerticle userVerticle;

    @Override
    public void start(Promise<Void> future) {
        CompositeFuture.all(deployHelper(vertx, serverVerticle),
                deployHelper(vertx, userVerticle))
                .onComplete(result -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                });
    }

    public static Future<Void> deployHelper(final Vertx vertx, final Verticle verticle) {
        final Promise<Void> promise = Promise.promise();
        vertx.deployVerticle(verticle, res -> {
            if (res.failed()) {
                LOGGER.error("Failed to deploy verticle!", verticle);
                promise.fail(res.cause());
            } else {
                LOGGER.info("{} verticle deployed!", verticle);
                promise.complete();
            }
        });

        return promise.future();
    }
}
