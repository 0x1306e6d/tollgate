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

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.function.Function;

import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.WebClientBuilder;
import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.common.SessionProtocol;

public final class UpstreamBuilder {

    private final WebClientBuilder clientBuilder;

    UpstreamBuilder(URI uri) {
        clientBuilder = WebClient.builder(requireNonNull(uri, "uri"));
    }

    UpstreamBuilder(SessionProtocol protocol, EndpointGroup endpointGroup) {
        clientBuilder = WebClient.builder(requireNonNull(protocol, "protocol"),
                                          requireNonNull(endpointGroup, "endpointGroup"));
    }

    UpstreamBuilder(SessionProtocol protocol, EndpointGroup endpointGroup, String path) {
        clientBuilder = WebClient.builder(requireNonNull(protocol, "protocol"),
                                          requireNonNull(endpointGroup, "endpointGroup"),
                                          requireNonNull(path, "path"));
    }

    public UpstreamBuilder decorator(Function<? super HttpClient, ? extends HttpClient> decorator) {
        clientBuilder.decorator(requireNonNull(decorator, "decorator"));
        return this;
    }

    public Upstream build() {
        return new DefaultUpstream(clientBuilder.build());
    }
}
