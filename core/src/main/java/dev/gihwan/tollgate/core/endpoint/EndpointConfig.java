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

package dev.gihwan.tollgate.core.endpoint;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import com.linecorp.armeria.common.HttpMethod;

import dev.gihwan.tollgate.core.upstream.UpstreamConfig;

public final class EndpointConfig {

    public static EndpointConfig of(HttpMethod method, String path, UpstreamConfig upstream) {
        return new EndpointConfig(method, path, upstream);
    }

    private final HttpMethod method;
    private final String path;
    private final UpstreamConfig upstream;

    private EndpointConfig(HttpMethod method, String path, UpstreamConfig upstream) {
        this.method = requireNonNull(method, "method");
        this.path = requireNonNull(path, "path");
        this.upstream = requireNonNull(upstream, "upstream");
    }

    public HttpMethod method() {
        return method;
    }

    public String path() {
        return path;
    }

    public UpstreamConfig upstream() {
        return upstream;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof EndpointConfig)) {
            return false;
        }

        final EndpointConfig that = (EndpointConfig) o;
        return Objects.equal(method, that.method) &&
               Objects.equal(path, that.path) &&
               Objects.equal(upstream, that.upstream);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(method, path, upstream);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("method", method)
                          .add("path", path)
                          .add("upstream", upstream)
                          .toString();
    }
}
