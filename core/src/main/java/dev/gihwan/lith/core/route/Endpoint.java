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

import com.linecorp.armeria.common.HttpMethod;

public final class Endpoint {

    public static Endpoint of(HttpMethod method, String path, Service service) {
        requireNonNull(method, "method");
        requireNonNull(path, "path");
        requireNonNull(service, "service");
        return new Endpoint(method, path, service);
    }

    public static EndpointBuilder builder() {
        return new EndpointBuilder();
    }

    private final HttpMethod method;
    private final String path;
    private final Service service;

    @JsonCreator
    Endpoint(@JsonProperty("method") HttpMethod method,
             @JsonProperty("path") String path,
             @JsonProperty("service") Service service) {
        this.method = method;
        this.path = path;
        this.service = service;
    }

    public HttpMethod method() {
        return method;
    }

    public String path() {
        return path;
    }

    public Service service() {
        return service;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("method", method)
                          .add("path", path)
                          .add("service", service)
                          .toString();
    }
}
