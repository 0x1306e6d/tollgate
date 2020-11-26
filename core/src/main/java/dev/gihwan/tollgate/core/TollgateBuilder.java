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

import java.util.ArrayList;
import java.util.List;

import dev.gihwan.tollgate.core.server.RouteConfig;

public final class TollgateBuilder {

    private int port = 8080;
    private String healthCheckPath = "/health";
    private final List<RouteConfig> routes = new ArrayList<>();

    TollgateBuilder() {}

    public TollgateBuilder port(int port) {
        this.port = port;
        return this;
    }

    public TollgateBuilder healthCheckPath(String healthCheckPath) {
        requireNonNull(healthCheckPath, "healthCheckPath");
        this.healthCheckPath = healthCheckPath;
        return this;
    }

    public TollgateBuilder route(RouteConfig route) {
        requireNonNull(route, "route");
        routes.add(route);
        return this;
    }

    public Tollgate build() {
        return new Tollgate(buildConfig());
    }

    private TollgateConfig buildConfig() {
        return new TollgateConfig(port, healthCheckPath, routes);
    }
}
