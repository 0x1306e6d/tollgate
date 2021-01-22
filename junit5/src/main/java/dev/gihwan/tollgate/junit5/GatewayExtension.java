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

package dev.gihwan.tollgate.junit5;

import java.net.URI;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.testing.junit5.common.AbstractAllOrEachExtension;

import dev.gihwan.tollgate.gateway.Gateway;
import dev.gihwan.tollgate.gateway.GatewayBuilder;
import dev.gihwan.tollgate.testing.TestGateway;

/**
 * An {@link Extension} which starts and stops the {@link Gateway} following test lifecycle.
 */
public abstract class GatewayExtension extends AbstractAllOrEachExtension {

    private final TestGateway delegate;

    protected GatewayExtension() {
        delegate = TestGateway.of(GatewayExtension.this::configure);
    }

    /**
     * Configures the {@link Gateway} with the specified {@link GatewayBuilder}.
     */
    protected abstract void configure(GatewayBuilder builder);

    @Override
    public final void before(ExtensionContext context) throws Exception {
        delegate.start();
    }

    @Override
    public final void after(ExtensionContext context) throws Exception {
        delegate.stop();
    }

    /**
     * @see TestGateway#httpPort()
     */
    public int httpPort() {
        return delegate.httpPort();
    }

    /**
     * @see TestGateway#port(SessionProtocol)
     */
    public int port(SessionProtocol protocol) {
        return delegate.port(protocol);
    }

    /**
     * @see TestGateway#httpUri()
     */
    public URI httpUri() {
        return delegate.httpUri();
    }
}
