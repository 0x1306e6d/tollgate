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

import static java.util.Objects.requireNonNull;

import com.typesafe.config.Config;

import dev.gihwan.tollgate.gateway.Gateway;
import dev.gihwan.tollgate.gateway.GatewayBuilder;

/**
 * A builder for {@link Gateway} using a {@link Config}.
 */
public final class HoconGatewayBuilder {

    /**
     * Returns a new {@link HoconGatewayBuilder}.
     */
    public static HoconGatewayBuilder of() {
        return new HoconGatewayBuilder(Gateway.builder());
    }

    /**
     * Returns a new {@link HoconGatewayBuilder} which builds a {@link Gateway} from the given
     * {@link GatewayBuilder}.
     */
    public static HoconGatewayBuilder of(GatewayBuilder delegate) {
        return new HoconGatewayBuilder(requireNonNull(delegate, "delegate"));
    }

    private HoconGatewayConfigurator gatewayConfigurator = HoconGatewayConfigurator.ofDefault();

    private final GatewayBuilder delegate;

    HoconGatewayBuilder(GatewayBuilder delegate) {
        this.delegate = delegate;
    }

    /**
     * Sets the {@link HoconGatewayConfigurator} which customizes how to build a {@link Gateway} using the
     * specified {@link Config}. {@link HoconGatewayConfigurator#ofDefault()} is used by default.
     */
    public HoconGatewayBuilder gatewayConfigurator(HoconGatewayConfigurator gatewayConfigurator) {
        this.gatewayConfigurator = requireNonNull(gatewayConfigurator, "gatewayConfigurator");
        return this;
    }

    /**
     * Returns a new {@link Gateway} using the given {@link Config} based on the properties of this builder.
     */
    public Gateway build(Config config) {
        gatewayConfigurator.configure(delegate, requireNonNull(config, "config"));
        return delegate.build();
    }
}
