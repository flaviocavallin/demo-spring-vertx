package com.example.demospringvertx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ApplicationConfiguration {

    @Autowired
    private Environment environment;

    public int httpPort() {
        return environment.getProperty("server.port", Integer.class);
    }
}
