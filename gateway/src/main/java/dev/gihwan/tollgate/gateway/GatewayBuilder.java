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

package dev.gihwan.tollgate.gateway;

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.healthcheck.HealthCheckService;

public final class GatewayBuilder {

    private final ServerBuilder serverBuilder = Server.builder();

    GatewayBuilder() {}

    /**
     * Configures a {@link Server} of a {@link Gateway} with the given {@code configurator}.
     *
     * Please note that calling {@link ServerBuilder#build()} inside {@code configurator} does not affect
     * building {@link Gateway}.
     */
    public GatewayBuilder server(Consumer<? super ServerBuilder> configurator) {
        requireNonNull(configurator, "configurator");
        configurator.accept(serverBuilder);
        return this;
    }

    public GatewayBuilder healthCheck(String healthCheckPath) {
        return healthCheck(healthCheckPath, HealthCheckService.of());
    }

    public GatewayBuilder healthCheck(String healthCheckPath, HealthCheckService healthCheckService) {
        serverBuilder.service(requireNonNull(healthCheckPath, "healthCheckPath"),
                              requireNonNull(healthCheckService, "healthCheckService"));
        return this;
    }

    public UpstreamBindingBuilder route() {
        return new UpstreamBindingBuilder(this, serverBuilder.route());
    }

    /**
     * Binds the given {@link Upstream} at the given {@code pathPattern}.
     */
    public GatewayBuilder upstream(String pathPattern, Upstream upstream) {
        return route().path(pathPattern).build(upstream);
    }

    /**
     * Binds the given {@link Upstream} with the given {@code decorators} at the given {@code pathPattern}.
     */
    @SafeVarargs
    public final GatewayBuilder upstream(String pathPattern,
                                         Upstream upstream,
                                         Function<? super HttpService, ? extends HttpService>... decorators) {
        return upstream(pathPattern, upstream, ImmutableList.copyOf(requireNonNull(decorators, "decorators")));
    }

    /**
     * Binds the given {@link Upstream} with the given {@code decorators} at the given {@code pathPattern}.
     */
    public GatewayBuilder upstream(
            String pathPattern,
            Upstream upstream,
            Iterable<? extends Function<? super HttpService, ? extends HttpService>> decorators) {
        return route().path(pathPattern).decorators(decorators).build(upstream);
    }

    public Gateway build() {
        return new Gateway(serverBuilder.build());
    }
}
