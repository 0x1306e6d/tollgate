/*
 * MIT License
 *
 * Copyright (c) 2020 - 2021 Gihwan Kim
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

package dev.gihwan.tollgate.hocon;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;

import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.client.logging.LoggingClientBuilder;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.logging.LogLevel;
import com.linecorp.armeria.common.logging.LoggingDecoratorBuilder;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.server.logging.LoggingServiceBuilder;

import dev.gihwan.tollgate.gateway.GatewayBuilder;
import dev.gihwan.tollgate.gateway.Upstream;
import dev.gihwan.tollgate.gateway.UpstreamBindingBuilder;
import dev.gihwan.tollgate.gateway.UpstreamBuilder;
import dev.gihwan.tollgate.remapping.RemappingClient;
import dev.gihwan.tollgate.remapping.RemappingClientBuilder;

enum DefaultHoconGatewayConfigurator implements HoconGatewayConfigurator {

    INSTANCE;

    private static void configureRouteConfig(GatewayBuilder builder, Config routeConfig) {
        checkArgument(routeConfig.hasPath("method"), "Route config must have method.");
        checkArgument(routeConfig.hasPath("path"), "Route config must have path.");
        checkArgument(routeConfig.hasPath("upstream"), "Route config must have upstream.");

        final UpstreamBindingBuilder routeBuilder =
                builder.route()
                       .methods(routeConfig.getEnum(HttpMethod.class, "method"))
                       .path(routeConfig.getString("path"));

        if (routeConfig.hasPath("logging")) {
            final Config loggingConfig = routeConfig.getObject("logging").toConfig();

            final LoggingServiceBuilder loggingBuilder = LoggingService.builder();
            configureLoggingConfig(loggingBuilder, loggingConfig);

            if (loggingConfig.hasPath("samplingRate")) {
                loggingBuilder.samplingRate((float) loggingConfig.getDouble("samplingRate"));
            }

            routeBuilder.decorator(loggingBuilder.newDecorator());
        }

        routeBuilder.build(configureUpstreamConfig(routeConfig.getObject("upstream").toConfig()));
    }

    private static Upstream configureUpstreamConfig(Config upstreamConfig) {
        final UpstreamBuilder builder;
        if (upstreamConfig.hasPath("uri")) {
            builder = Upstream.builder(upstreamConfig.getString("uri"));
        } else {
            checkArgument(upstreamConfig.hasPath("endpoints"),
                          "Upstream config must have one of uri and endpoints.");
            checkArgument(upstreamConfig.hasPath("scheme"),
                          "Upstream config must have scheme when configure with endpoints.");

            final List<Endpoint> endpoints = upstreamConfig.getObjectList("endpoints")
                                                           .stream()
                                                           .map(ConfigObject::toConfig)
                                                           .map(config -> {
                                                               final String host = config.getString("host");
                                                               final int port = config.getInt("port");
                                                               return Endpoint.of(host, port);
                                                           })
                                                           .collect(Collectors.toUnmodifiableList());
            builder = Upstream.builder(upstreamConfig.getString("scheme"), EndpointGroup.of(endpoints));
        }

        if (upstreamConfig.hasPath("path")) {
            builder.path(upstreamConfig.getString("path"));
        }

        if (upstreamConfig.hasPath("disallowRequestHeaders")) {
            final List<String> disallowRequestHeaders = upstreamConfig.getStringList("disallowRequestHeaders");
            if (!disallowRequestHeaders.isEmpty()) {
                builder.disallowRequestHeaders(disallowRequestHeaders);
            }
        }

        if (upstreamConfig.hasPath("disallowResponseHeaders")) {
            final List<String> disallowResponseHeaders =
                    upstreamConfig.getStringList("disallowResponseHeaders");
            if (!disallowResponseHeaders.isEmpty()) {
                builder.disallowResponseHeaders(disallowResponseHeaders);
            }
        }

        if (upstreamConfig.hasPath("remapping")) {
            final Config remappingConfig = upstreamConfig.getObject("remapping").toConfig();
            final RemappingClientBuilder remappingBuilder = RemappingClient.builder();
            if (remappingConfig.hasPath("path")) {
                remappingBuilder.requestPath(remappingConfig.getString("path"));
            }
            builder.decorator(remappingBuilder.newDecorator());
        }

        if (upstreamConfig.hasPath("logging")) {
            final Config loggingConfig = upstreamConfig.getObject("logging").toConfig();

            final LoggingClientBuilder loggingBuilder = LoggingClient.builder();
            configureLoggingConfig(loggingBuilder, loggingConfig);

            if (loggingConfig.hasPath("samplingRate")) {
                loggingBuilder.samplingRate((float) loggingConfig.getDouble("samplingRate"));
            }

            builder.decorator(loggingBuilder.newDecorator());
        }

        return builder.build();
    }

    private static void configureLoggingConfig(LoggingDecoratorBuilder builder, Config loggingConfig) {
        if (loggingConfig.hasPath("logger")) {
            builder.logger(loggingConfig.getString("logger"));
        }
        if (loggingConfig.hasPath("requestLogLevel")) {
            final LogLevel requestLogLevel = LogLevel.valueOf(loggingConfig.getString("requestLogLevel"));
            builder.requestLogLevel(requestLogLevel);
        }
        if (loggingConfig.hasPath("successfulResponseLogLevel")) {
            final LogLevel successfulResponseLogLevel =
                    LogLevel.valueOf(loggingConfig.getString("successfulResponseLogLevel"));
            builder.successfulResponseLogLevel(successfulResponseLogLevel);
        }
        if (loggingConfig.hasPath("failureResponseLogLevel")) {
            final LogLevel failureResponseLogLevel =
                    LogLevel.valueOf(loggingConfig.getString("failureResponseLogLevel"));
            builder.failureResponseLogLevel(failureResponseLogLevel);
        }
    }

    @Override
    public void configure(GatewayBuilder builder, Config config) {
        requireNonNull(builder, "builder");
        requireNonNull(config, "config");

        if (config.hasPath("tollgate.port")) {
            final int port = config.getInt("tollgate.port");
            builder.server(serverBuilder -> serverBuilder.http(port));
        }
        if (config.hasPath("tollgate.healthCheckPath")) {
            builder.healthCheck(config.getString("tollgate.healthCheckPath"));
        }
        if (config.hasPath("tollgate.routing")) {
            final Set<String> routes = config.getObject("tollgate.routing").keySet();
            routes.stream()
                  .map(route -> config.getObject("tollgate.routing." + route))
                  .map(ConfigObject::toConfig)
                  .forEach(routeConfig -> configureRouteConfig(builder, routeConfig));
        }
    }
}
