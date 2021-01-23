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

package dev.gihwan.tollgate.testing;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.function.Consumer;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.common.util.SafeCloseable;

import dev.gihwan.tollgate.gateway.Gateway;
import dev.gihwan.tollgate.gateway.GatewayBuilder;

/**
 * A delegate of {@link Gateway} which provides features for testing.
 */
public abstract class TestGateway implements SafeCloseable {

    /**
     * Creates and starts a new {@link Gateway} which configured with the given {@code builderConsumer}.
     * Please note that the returned {@link Gateway} is already started automatically.
     */
    @CheckReturnValue
    public static TestGateway withTestGateway(Consumer<? super GatewayBuilder> builderConsumer) {
        final TestGateway testGateway = of(builderConsumer);
        testGateway.start();
        return testGateway;
    }

    /**
     * Creates a new {@link Gateway} which configured with the given {@code builderConsumer}.
     */
    public static TestGateway of(Consumer<? super GatewayBuilder> builderConsumer) {
        requireNonNull(builderConsumer, "builderConsumer");
        return new TestGateway() {
            @Override
            protected void configure(GatewayBuilder builder) {
                builderConsumer.accept(builder);
            }
        };
    }

    @Nullable
    private Gateway delegate;

    TestGateway() {}

    /**
     * Configures the {@link Gateway} with the specified {@link GatewayBuilder}.
     */
    protected abstract void configure(GatewayBuilder builder);

    /**
     * Starts the {@link Gateway}.
     */
    public void start() {
        final GatewayBuilder builder = Gateway.builder();
        configure(builder);

        delegate = builder.build();
        delegate.start();
    }

    /**
     * Stops the {@link Gateway}.
     */
    public void stop() {
        checkState(delegate != null, "gateway did not start.");
        delegate.stop();
    }

    /**
     * @see TestGateway#stop()
     */
    @Override
    public void close() {
        stop();
    }

    /**
     * Returns the local port which the {@link Gateway} serves HTTP.
     */
    public int httpPort() {
        return port(SessionProtocol.HTTP);
    }

    /**
     * Returns the local port which the {@link Gateway} serves the given {@code protocol}.
     */
    public int port(SessionProtocol protocol) {
        checkState(delegate != null, "gateway did not start.");
        return delegate.activeLocalPort(protocol);
    }

    /**
     * Returns the {@link URI} which the {@link Gateway} serves HTTP.
     */
    public URI httpUri() {
        return URI.create("http://127.0.0.1:" + httpPort());
    }
}
