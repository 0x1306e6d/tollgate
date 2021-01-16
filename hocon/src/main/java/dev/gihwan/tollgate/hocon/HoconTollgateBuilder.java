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

import static dev.gihwan.tollgate.hocon.HoconTollgateConfigurators.configureWithHoconConfig;
import static java.util.Objects.requireNonNull;

import com.typesafe.config.Config;

import com.linecorp.armeria.server.healthcheck.HealthCheckService;

import dev.gihwan.tollgate.core.TollgateBuilder;
import dev.gihwan.tollgate.core.Upstream;

public final class HoconTollgateBuilder extends TollgateBuilder {

    public static HoconTollgateBuilder of(Config config) {
        return new HoconTollgateBuilder(requireNonNull(config, "config"));
    }

    HoconTollgateBuilder(Config config) {
        super();
        configureWithHoconConfig(this, config);
    }

    @Override
    public HoconTollgateBuilder http(int port) {
        return (HoconTollgateBuilder) super.http(port);
    }

    @Override
    public HoconTollgateBuilder healthCheck(String healthCheckPath) {
        return (HoconTollgateBuilder) super.healthCheck(healthCheckPath);
    }

    @Override
    public HoconTollgateBuilder healthCheck(String healthCheckPath, HealthCheckService healthCheckService) {
        return (HoconTollgateBuilder) super.healthCheck(healthCheckPath, healthCheckService);
    }

    @Override
    public HoconUpstreamBindingBuilder route() {
        return new HoconUpstreamBindingBuilder(this, serverRoute());
    }

    @Override
    public HoconTollgateBuilder upstream(String pathPattern, Upstream upstream) {
        return (HoconTollgateBuilder) super.upstream(pathPattern, upstream);
    }
}
