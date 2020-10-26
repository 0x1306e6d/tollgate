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

package dev.gihwan.tollgate.core.service;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import com.linecorp.armeria.client.Endpoint;

public final class Authority {

    public static Authority of(String host, int port) {
        return new Authority(host, port);
    }

    private final String host;
    private final int port;

    private Authority(String host, int port) {
        this.host = requireNonNull(host, "host");
        checkArgument(port >= 0, "port: %s (expected: >= 0)", port);
        checkArgument(port <= 65535, "port: %s (expected: <= 65535)", port);
        this.port = port;
    }

    public String host() {
        return host;
    }

    public int port() {
        return port;
    }

    public Endpoint toArmeriaEndpoint() {
        return Endpoint.of(host, port);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Authority)) {
            return false;
        }

        final Authority that = (Authority) o;
        return Objects.equal(host, that.host) &&
               port == that.port;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(host, port);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("host", host)
                          .add("port", port)
                          .toString();
    }
}
