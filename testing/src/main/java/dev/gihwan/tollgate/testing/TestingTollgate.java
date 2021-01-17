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

import dev.gihwan.tollgate.core.DefaultTollgateBuilder;
import dev.gihwan.tollgate.core.Tollgate;
import dev.gihwan.tollgate.core.TollgateBuilder;

/**
 * A delegate of {@link Tollgate} which provides features for testing.
 */
public abstract class TestingTollgate implements SafeCloseable {

    /**
     * Creates and starts a new {@link Tollgate} which configured with the given {@code builderConsumer}.
     * Please note that the returned {@link Tollgate} is already started automatically.
     */
    @CheckReturnValue
    public static TestingTollgate withTestingTollgate(Consumer<? super TollgateBuilder> builderConsumer) {
        final TestingTollgate tollgate = of(builderConsumer);
        tollgate.start();
        return tollgate;
    }

    /**
     * Creates a new {@link Tollgate} which configured with the given {@code builderConsumer}.
     */
    public static TestingTollgate of(Consumer<? super TollgateBuilder> builderConsumer) {
        requireNonNull(builderConsumer, "builderConsumer");
        return new TestingTollgate() {
            @Override
            protected void configure(TollgateBuilder builder) {
                builderConsumer.accept(builder);
            }
        };
    }

    @Nullable
    private Tollgate delegate;

    TestingTollgate() {}

    /**
     * Configures the {@link Tollgate} with the specified {@link TollgateBuilder}.
     */
    protected abstract void configure(TollgateBuilder builder);

    /**
     * Starts the {@link Tollgate}.
     */
    public void start() {
        final DefaultTollgateBuilder builder = Tollgate.builder();
        configure(builder);

        delegate = builder.build();
        delegate.start();
    }

    /**
     * Stops the {@link Tollgate}.
     */
    public void stop() {
        checkState(delegate != null, "Tollgate did not start.");
        delegate.stop();
    }

    /**
     * @see TestingTollgate#stop()
     */
    @Override
    public void close() {
        stop();
    }

    /**
     * Returns the local port which the {@link Tollgate} serves HTTP.
     */
    public int httpPort() {
        return port(SessionProtocol.HTTP);
    }

    /**
     * Returns the local port which the {@link Tollgate} serves the given {@code protocol}.
     */
    public int port(SessionProtocol protocol) {
        checkState(delegate != null, "Tollgate did not start.");
        return delegate.activeLocalPort(protocol);
    }

    /**
     * Returns the {@link URI} which the {@link Tollgate} serves HTTP.
     */
    public URI httpUri() {
        return URI.create("http://127.0.0.1:" + httpPort());
    }
}
