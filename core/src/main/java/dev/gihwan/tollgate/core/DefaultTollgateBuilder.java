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

package dev.gihwan.tollgate.core;

import com.linecorp.armeria.server.healthcheck.HealthCheckService;

public final class DefaultTollgateBuilder extends TollgateBuilder {

    DefaultTollgateBuilder() {}

    @Override
    public DefaultTollgateBuilder http(int port) {
        return (DefaultTollgateBuilder) super.http(port);
    }

    @Override
    public DefaultTollgateBuilder healthCheck(String healthCheckPath) {
        return (DefaultTollgateBuilder) super.healthCheck(healthCheckPath);
    }

    @Override
    public DefaultTollgateBuilder healthCheck(String healthCheckPath, HealthCheckService healthCheckService) {
        return (DefaultTollgateBuilder) super.healthCheck(healthCheckPath, healthCheckService);
    }

    @Override
    public DefaultUpstreamBindingBuilder route() {
        return new DefaultUpstreamBindingBuilder(this, serverRoute());
    }

    @Override
    public DefaultTollgateBuilder upstream(String pathPattern, Upstream upstream) {
        return (DefaultTollgateBuilder) super.upstream(pathPattern, upstream);
    }
}
