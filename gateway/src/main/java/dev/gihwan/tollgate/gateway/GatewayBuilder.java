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

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.healthcheck.HealthCheckService;

public final class GatewayBuilder {

    private final ServerBuilder serverBuilder = Server.builder();

    protected GatewayBuilder() {}

    /**
     * @see ServerBuilder#http(int)
     */
    public GatewayBuilder http(int port) {
        serverBuilder.http(port);
        return this;
    }

    public GatewayBuilder healthCheck(String healthCheckPath) {
        return healthCheck(healthCheckPath, HealthCheckService.of());
    }

    public GatewayBuilder healthCheck(String healthCheckPath, HealthCheckService healthCheckService) {
        serverBuilder.service(healthCheckPath, healthCheckService);
        return this;
    }

    public UpstreamBindingBuilder route() {
        return new UpstreamBindingBuilder(this, serverBuilder.route());
    }

    public GatewayBuilder upstream(String pathPattern, Upstream upstream) {
        return route().path(pathPattern).build(upstream);
    }

    public final Gateway build() {
        return new Gateway(serverBuilder.build());
    }
}
