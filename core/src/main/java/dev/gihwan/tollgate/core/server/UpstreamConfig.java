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

package dev.gihwan.tollgate.core.server;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import dev.gihwan.tollgate.core.client.ServiceConfig;

public final class UpstreamConfig {

    public static UpstreamConfig of(ServiceConfig service, UpstreamEndpointConfig endpoint) {
        return new UpstreamConfig(service, endpoint);
    }

    private final ServiceConfig service;
    private final UpstreamEndpointConfig endpoint;

    private UpstreamConfig(ServiceConfig service, UpstreamEndpointConfig endpoint) {
        this.service = requireNonNull(service, "service");
        this.endpoint = requireNonNull(endpoint, "endpoint");
    }

    public ServiceConfig service() {
        return service;
    }

    public UpstreamEndpointConfig endpoint() {
        return endpoint;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof UpstreamConfig)) {
            return false;
        }

        final UpstreamConfig that = (UpstreamConfig) o;
        return Objects.equal(service, that.service) &&
               Objects.equal(endpoint, that.endpoint);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(service, endpoint);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("service", service)
                          .add("endpoint", endpoint)
                          .toString();
    }
}
