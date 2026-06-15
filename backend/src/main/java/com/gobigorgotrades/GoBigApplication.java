package com.gobigorgotrades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point.
 *
 * <p>{@code @SpringBootApplication} bundles three things:
 * component scanning (picks up our @Service/@Repository/@RestController beans
 * in this package and below), auto-configuration (wires up the web server,
 * the datasource, JPA, Flyway from the dependencies on the classpath), and
 * the configuration-properties machinery.
 */
@SpringBootApplication
public class GoBigApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoBigApplication.class, args);
    }
}
