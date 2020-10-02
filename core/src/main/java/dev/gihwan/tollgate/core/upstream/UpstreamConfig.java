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

package dev.gihwan.tollgate.core.upstream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import dev.gihwan.tollgate.core.service.ServiceConfig;

public final class UpstreamConfig {

    private final ServiceConfig service;
    private final UpstreamEndpointConfig endpoint;

    @JsonCreator
    private UpstreamConfig(@JsonProperty("service") ServiceConfig service,
                           @JsonProperty("endpoint") UpstreamEndpointConfig endpoint) {
        this.service = service;
        this.endpoint = endpoint;
    }

    public ServiceConfig service() {
        return service;
    }

    public UpstreamEndpointConfig endpoint() {
        return endpoint;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("service", service)
                          .add("endpoint", endpoint)
                          .toString();
    }
}
