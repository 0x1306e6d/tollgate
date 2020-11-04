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

package dev.gihwan.tollgate.standalone;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;

import com.linecorp.armeria.common.HttpMethod;

import dev.gihwan.tollgate.core.Tollgate;
import dev.gihwan.tollgate.core.TollgateBuilder;
import dev.gihwan.tollgate.core.server.EndpointConfig;
import dev.gihwan.tollgate.core.server.UpstreamConfig;
import dev.gihwan.tollgate.core.server.UpstreamEndpointConfig;
import dev.gihwan.tollgate.core.service.Authority;
import dev.gihwan.tollgate.core.service.ServiceConfig;

public final class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Nullable
    private Tollgate tollgate;
    @Nullable
    private Instant startAt;

    private void start() {
        startAt = Instant.now();
        logger.info("Starting the Tollgate standalone at {}.", startAt);

        final Config config = ConfigFactory.load();
        logger.debug("Loaded configuration: {}", config);

        final TollgateBuilder builder = Tollgate.builder()
                                                .port(config.getInt("tollgate.port"))
                                                .healthCheckPath(config.getString("tollgate.healthCheckPath"));

        final Set<String> endpointNames = config.getObject("tollgate.endpoints").keySet();
        endpointNames.stream()
                     .map(name -> config.getObject("tollgate.endpoints." + name))
                     .map(ConfigObject::toConfig)
                     .forEach(endpointConfig -> buildEndpoint(builder, endpointConfig));

        tollgate = builder.build();
        tollgate.start();

        assert startAt != null;
        logger.info("The Tollgate standalone is started. ({} used to start)",
                    Duration.between(startAt, Instant.now()));
    }

    private void buildEndpoint(TollgateBuilder builder, Config endpointConfig) {
        final Config upstreamServiceConfig = endpointConfig.getObject("upstream.service").toConfig();
        final ServiceConfig service = buildService(upstreamServiceConfig);

        final Config upstreamEndpointConfig = endpointConfig.getObject("upstream.endpoint").toConfig();
        final UpstreamEndpointConfig upstreamEndpoint = buildUpstreamEndpoint(upstreamEndpointConfig);

        builder.endpoint(EndpointConfig.of(endpointConfig.getEnum(HttpMethod.class, "method"),
                                           endpointConfig.getString("path"),
                                           UpstreamConfig.of(service, upstreamEndpoint)));
    }

    private ServiceConfig buildService(Config config) {
        if (config.hasPath("uri")) {
            return ServiceConfig.of(config.getString("uri"));
        } else {
            List<Authority> authorities = config.getObjectList("authorities")
                                                .stream()
                                                .map(ConfigObject::toConfig)
                                                .map(authorityConfig -> {
                                                    final String host = authorityConfig.getString("host");
                                                    final int port = authorityConfig.getInt("port");
                                                    return Authority.of(host, port);
                                                })
                                                .collect(Collectors.toUnmodifiableList());
            return ServiceConfig.of(config.getString("scheme"), authorities);
        }
    }

    private UpstreamEndpointConfig buildUpstreamEndpoint(Config config) {
        return UpstreamEndpointConfig.of(config.getEnum(HttpMethod.class, "method"),
                                         config.getString("path"));
    }

    private void stop() {
        final Instant stopAt = Instant.now();
        logger.info("Stopping the Tollgate standalone at {}.", stopAt);

        if (tollgate != null) {
            tollgate.stop();
        }

        assert startAt != null;
        logger.info("The Tollgate standalone has served during {}.", Duration.between(startAt, stopAt));
        logger.info("The Tollgate standalone is stopped. ({} used to stop)",
                    Duration.between(startAt, Instant.now()));
    }

    public static void main(String[] args) {
        final Main main = new Main();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                main.stop();
            } catch (Exception e) {
                logger.error("Failed to stop the Tollgate standalone.", e);
            }
        }));

        try {
            main.start();
        } catch (Exception e) {
            logger.error("Failed to start the Tollgate standalone.", e);
            System.exit(-1);
        }
    }
}
