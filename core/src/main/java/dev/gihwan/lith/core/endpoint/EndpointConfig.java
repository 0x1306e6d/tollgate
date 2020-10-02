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

package dev.gihwan.lith.core.endpoint;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import com.linecorp.armeria.common.HttpMethod;

import dev.gihwan.lith.core.upstream.UpstreamConfig;

public final class EndpointConfig {

    private final HttpMethod method;
    private final String path;
    private final UpstreamConfig upstream;

    @JsonCreator
    private EndpointConfig(@JsonProperty("method") HttpMethod method,
                           @JsonProperty("path") String path,
                           @JsonProperty("upstream") UpstreamConfig upstream) {
        this.method = method;
        this.path = path;
        this.upstream = upstream;
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
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("method", method)
                          .add("path", path)
                          .add("upstream", upstream)
                          .toString();
    }
}
