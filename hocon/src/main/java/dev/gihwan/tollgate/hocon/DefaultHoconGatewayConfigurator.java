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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;

import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.common.HttpMethod;

import dev.gihwan.tollgate.gateway.GatewayBuilder;
import dev.gihwan.tollgate.gateway.Upstream;
import dev.gihwan.tollgate.gateway.UpstreamBuilder;
import dev.gihwan.tollgate.remapping.RemappingRequestUpstream;
import dev.gihwan.tollgate.remapping.RemappingRequestUpstreamBuilder;

enum DefaultHoconGatewayConfigurator implements HoconGatewayConfigurator {

    INSTANCE;

    @Override
    public void configure(GatewayBuilder builder, Config config) {
        if (config.hasPath("tollgate.port")) {
            builder.http(config.getInt("tollgate.port"));
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

    private static void configureRouteConfig(GatewayBuilder builder, Config routeConfig) {
        checkArgument(routeConfig.hasPath("method"), "Route config must have method.");
        checkArgument(routeConfig.hasPath("path"), "Route config must have path.");
        checkArgument(routeConfig.hasPath("upstream"), "Route config must have upstream.");

        builder.route()
               .methods(routeConfig.getEnum(HttpMethod.class, "method"))
               .path(routeConfig.getString("path"))
               .build(configureUpstreamConfig(routeConfig.getObject("upstream").toConfig()));
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

        if (upstreamConfig.hasPath("remapping")) {
            final Config remappingConfig = upstreamConfig.getObject("remapping").toConfig();
            final RemappingRequestUpstreamBuilder remappingBuilder = RemappingRequestUpstream.builder();
            if (remappingConfig.hasPath("path")) {
                remappingBuilder.path(remappingConfig.getString("path"));
            }
            builder.decorator(remappingBuilder.newDecorator());
        }

        return builder.build();
    }
}
