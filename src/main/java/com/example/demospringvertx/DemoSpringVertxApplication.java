package com.example.demospringvertx;

import com.example.demospringvertx.verticles.LauncherVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoSpringVertxApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoSpringVertxApplication.class);

    @Autowired
    private LauncherVerticle launcherVerticle;

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringVertxApplication.class, args);
    }


    //	@EventListener(ApplicationReadyEvent.class)
    //	public void deployVerticle() {

    @Bean
    public Vertx vertx() {
        final Promise<Void> promise = Promise.promise();
        final Vertx vertx = Vertx.vertx();
//      VertxOptions options = new VertxOptions();
//		// check for blocked threads every 10s
//		options.setBlockedThreadCheckInterval(10000);
//
//		// warn if an event loop thread handler took more than 100ms to execute
//		options.setMaxEventLoopExecuteTime(100);
//
//		// warn if an worker thread handler took more than 10s to execute
//		options.setMaxWorkerExecuteTime(10000);
//
//		// log the stack trace if an event loop or worker handler took more than 20s to execute
//		options.setWarningExceptionTime(20000);
//
//		final Vertx vertx = Vertx.vertx(options);
//		vertx.deployVerticle(serverVerticle);
//		vertx.deployVerticle(userVerticle);
//		// create a thread pool with 20 threads, set blocked thread warning threshold to 10 seconds
//		// WorkerExecutor executor = vertx.createSharedWorkerExecutor("mypool", 20, 10, TimeUnit.SECONDS);
//
//		//https://servicesblog.redhat.com/2019/08/13/troubleshooting-the-performance-of-vert-x-applications-part-iii-troubleshooting-event-loop-delays/

        vertx.deployVerticle(launcherVerticle, res -> {
            if (res.succeeded()) {
                LOGGER.info("Deployment id is: {} ", res.result());
                promise.complete();
            } else {
                LOGGER.error("Deployment failed!");
                promise.fail("Deployment failed");
            }
        });
        while (!promise.future().isComplete()) {
            try {
                LOGGER.info("Waiting 500ml until vertx starts....");
                Thread.sleep(500l);
            } catch (Exception e) {
                LOGGER.error("Error trying to get vertx bean", e);
            }
        }
        return vertx;
    }
}
