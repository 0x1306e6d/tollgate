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

import java.io.File;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.function.Consumer;

import javax.annotation.Nullable;
import javax.net.ssl.KeyManagerFactory;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.healthcheck.HealthCheckService;

import io.netty.handler.ssl.SslContextBuilder;

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

    /**
     * @see ServerBuilder#http(InetSocketAddress)
     */
    public GatewayBuilder http(InetSocketAddress localAddress) {
        serverBuilder.http(localAddress);
        return this;
    }

    /**
     * @see ServerBuilder#https(int)
     */
    public GatewayBuilder https(int port) {
        serverBuilder.https(port);
        return this;
    }

    /**
     * @see ServerBuilder#https(InetSocketAddress)
     */
    public GatewayBuilder https(InetSocketAddress localAddress) {
        serverBuilder.https(localAddress);
        return this;
    }

    /**
     * @see ServerBuilder#tls(File, File)
     */
    public GatewayBuilder tls(File keyCertChainFile, File keyFile) {
        serverBuilder.tls(keyCertChainFile, keyFile);
        return this;
    }

    /**
     * @see ServerBuilder#tls(File, File, String)
     */
    public GatewayBuilder tls(File keyCertChainFile, File keyFile, @Nullable String keyPassword) {
        serverBuilder.tls(keyCertChainFile, keyFile, keyPassword);
        return this;
    }

    /**
     * @see ServerBuilder#tls(InputStream, InputStream)
     */
    public GatewayBuilder tls(InputStream keyCertChainInputStream, InputStream keyInputStream) {
        serverBuilder.tls(keyCertChainInputStream, keyInputStream);
        return this;
    }

    /**
     * @see ServerBuilder#tls(InputStream, InputStream, String)
     */
    public GatewayBuilder tls(InputStream keyCertChainInputStream,
                              InputStream keyInputStream,
                              @Nullable String keyPassword) {
        serverBuilder.tls(keyCertChainInputStream, keyInputStream, keyPassword);
        return this;
    }

    /**
     * @see ServerBuilder#tls(PrivateKey, X509Certificate...)
     */
    public GatewayBuilder tls(PrivateKey key, X509Certificate... keyCertChain) {
        serverBuilder.tls(key, keyCertChain);
        return this;
    }

    /**
     * @see ServerBuilder#tls(PrivateKey, Iterable)
     */
    public GatewayBuilder tls(PrivateKey key, Iterable<? extends X509Certificate> keyCertChain) {
        serverBuilder.tls(key, keyCertChain);
        return this;
    }

    /**
     * @see ServerBuilder#tls(PrivateKey, String, X509Certificate...)
     */
    public GatewayBuilder tls(PrivateKey key, @Nullable String keyPassword, X509Certificate... keyCertChain) {
        serverBuilder.tls(key, keyPassword, keyCertChain);
        return this;
    }

    /**
     * @see ServerBuilder#tls(PrivateKey, String, Iterable)
     * @return
     */
    public GatewayBuilder tls(PrivateKey key,
                              @Nullable String keyPassword,
                              Iterable<? extends X509Certificate> keyCertChain) {
        serverBuilder.tls(key, keyPassword, keyCertChain);
        return this;
    }

    /**
     * @see ServerBuilder#tls(KeyManagerFactory)
     */
    public GatewayBuilder tls(KeyManagerFactory keyManagerFactory) {
        serverBuilder.tls(keyManagerFactory);
        return this;
    }

    /**
     * @see ServerBuilder#tlsSelfSigned()
     */
    public GatewayBuilder tlsSelfSigned() {
        serverBuilder.tlsSelfSigned();
        return this;
    }

    /**
     * @see ServerBuilder#tlsSelfSigned(boolean)
     */
    public GatewayBuilder tlsSelfSigned(boolean tlsSelfSigned) {
        serverBuilder.tlsSelfSigned(tlsSelfSigned);
        return this;
    }

    /**
     * @see ServerBuilder#tlsCustomizer(Consumer)
     */
    public GatewayBuilder tlsCustomizer(Consumer<? super SslContextBuilder> tlsCustomizer) {
        serverBuilder.tlsCustomizer(tlsCustomizer);
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

    /**
     * Binds the given {@link Upstream} at the given {@code pathPattern}.
     */
    public GatewayBuilder upstream(String pathPattern, Upstream upstream) {
        return route().path(pathPattern).build(upstream);
    }

    public final Gateway build() {
        return new Gateway(serverBuilder.build());
    }
}
