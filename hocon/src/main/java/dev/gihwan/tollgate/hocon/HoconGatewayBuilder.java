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

import dev.gihwan.tollgate.gateway.GatewayBuilder;
import dev.gihwan.tollgate.gateway.Upstream;

public final class HoconGatewayBuilder extends GatewayBuilder {

    public static HoconGatewayBuilder of(Config config) {
        return new HoconGatewayBuilder(requireNonNull(config, "config"));
    }

    HoconGatewayBuilder(Config config) {
        super();
        configureWithHoconConfig(this, config);
    }

    @Override
    public HoconGatewayBuilder http(int port) {
        return (HoconGatewayBuilder) super.http(port);
    }

    @Override
    public HoconGatewayBuilder healthCheck(String healthCheckPath) {
        return (HoconGatewayBuilder) super.healthCheck(healthCheckPath);
    }

    @Override
    public HoconGatewayBuilder healthCheck(String healthCheckPath, HealthCheckService healthCheckService) {
        return (HoconGatewayBuilder) super.healthCheck(healthCheckPath, healthCheckService);
    }

    @Override
    public HoconUpstreamBindingBuilder route() {
        return new HoconUpstreamBindingBuilder(this, serverRoute());
    }

    @Override
    public HoconGatewayBuilder upstream(String pathPattern, Upstream upstream) {
        return (HoconGatewayBuilder) super.upstream(pathPattern, upstream);
    }
}
