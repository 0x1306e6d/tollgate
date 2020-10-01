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

package dev.gihwan.lith.core.route;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public final class Service {

    public static Service of(String uri, ServiceEndpoint endpoint) {
        requireNonNull(uri, "uri");
        requireNonNull(endpoint, "endpoint");
        return new Service(uri, endpoint);
    }

    public static ServiceBuilder builder() {
        return new ServiceBuilder();
    }

    private final String uri;
    private final ServiceEndpoint endpoint;

    @JsonCreator
    Service(@JsonProperty("uri") String uri, @JsonProperty("endpoint") ServiceEndpoint endpoint) {
        this.uri = uri;
        this.endpoint = endpoint;
    }

    public String uri() {
        return uri;
    }

    public ServiceEndpoint endpoint() {
        return endpoint;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("uri", uri)
                          .add("endpoint", endpoint)
                          .toString();
    }
}
