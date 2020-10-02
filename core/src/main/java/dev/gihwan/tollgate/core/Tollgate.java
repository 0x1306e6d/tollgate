/*
 * MIT License
 *
 * Copyright (c) 2020 Gihwan Kim
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.gihwan.tollgate.core;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.server.Route;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.healthcheck.HealthCheckService;
import com.linecorp.armeria.server.healthcheck.HealthChecker;
import com.linecorp.armeria.server.healthcheck.SettableHealthChecker;
import com.linecorp.armeria.server.logging.LoggingService;

import dev.gihwan.tollgate.core.endpoint.Endpoint;

public final class Tollgate {

    private static final Logger logger = LoggerFactory.getLogger(Tollgate.class);

    public static Tollgate of(TollgateConfig config) {
        requireNonNull(config, "config");
        return new Tollgate(config);
    }

    public static TollgateBuilder builder() {
        return new TollgateBuilder();
    }

    @Nullable
    private Server server;
    private final TollgateConfig config;
    private final HealthChecker healthChecker;

    Tollgate(TollgateConfig config) {
        this.config = config;
        healthChecker = new SettableHealthChecker();
    }

    public void start() {
        server = startServer();
    }

    public void stop() {
        stopServer();
    }

    private Server startServer() {
        if (server != null) {
            throw new IllegalStateException("The Tollgate server is already started.");
        }

        logger.info("Starting the Tollgate server.");

        final ServerBuilder builder = Server.builder();

        builder.http(config.port());
        builder.serviceUnder("/docs", DocService.builder().build());
        builder.service(config.healthCheckPath(), HealthCheckService.of(healthChecker));

        config.endpoints().forEach(endpointConfig -> {
            logger.info("Registering endpoint {}.", endpointConfig);

            final Endpoint endpoint = Endpoint.of(endpointConfig);
            builder.service(Route.builder()
                                 .methods(endpointConfig.method())
                                 .path(endpointConfig.path())
                                 .build(),
                            endpoint.upstream()
                                    .decorate(LoggingService.newDecorator()));
        });

        final Server server = builder.build();
        server.start().join();
        logger.info("Started the Tollgate server at {}.", server.activePort());
        return server;
    }

    private void stopServer() {
        if (server == null) {
            return;
        }

        logger.info("Stopping the Tollgate server.");
        server.stop().join();
        logger.info("Stopped the Tollgate server.");
    }
}