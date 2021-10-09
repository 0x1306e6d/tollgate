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

import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.SessionProtocol;

/**
 * An upstream which forwards {@link HttpRequest}s from client to the specified destination.
 */
public interface Upstream {

    /**
     * Returns a new {@link Upstream} which forwards to the given {@code uri}.
     */
    static Upstream of(String uri) {
        return builder(uri).build();
    }

    /**
     * Returns a new {@link Upstream} which forward to the given {@link URI}.
     */
    static Upstream of(URI uri) {
        return builder(uri).build();
    }

    /**
     * Returns a new {@link Upstream} which forwards to the given {@link EndpointGroup} with the given
     * {@code protocol}.
     */
    static Upstream of(String protocol, EndpointGroup endpointGroup) {
        return builder(protocol, endpointGroup).build();
    }

    /**
     * Returns a new {@link Upstream} which forwards to the given {@link EndpointGroup}  with the given
     * {@code protocol} and {@code path}.
     */
    static Upstream of(String protocol, EndpointGroup endpointGroup, String path) {
        return builder(protocol, endpointGroup, path).build();
    }

    /**
     * Returns a new {@link UpstreamBuilder} based on the given {@code uri}.
     */
    static UpstreamBuilder builder(String uri) {
        return builder(URI.create(requireNonNull(uri, "uri")));
    }

    /**
     * Returns a new {@link UpstreamBuilder} based on the given {@link URI}.
     */
    static UpstreamBuilder builder(URI uri) {
        return new UpstreamBuilder(requireNonNull(uri, "uri"));
    }

    /**
     * Returns a new {@link UpstreamBuilder} based on the given {@link EndpointGroup} with the given
     * {@code protocol}.
     */
    static UpstreamBuilder builder(String protocol, EndpointGroup endpointGroup) {
        return builder(SessionProtocol.of(requireNonNull(protocol, "protocol")), endpointGroup);
    }

    /**
     * Returns a new {@link UpstreamBuilder} based on the given {@link EndpointGroup} with the given
     * {@link SessionProtocol}.
     */
    static UpstreamBuilder builder(SessionProtocol protocol, EndpointGroup endpointGroup) {
        return new UpstreamBuilder(protocol, endpointGroup);
    }

    /**
     * Returns a new {@link UpstreamBuilder} based on the given {@link EndpointGroup} with the given
     * {@code protocol} and {@code path}.
     */
    static UpstreamBuilder builder(String protocol, EndpointGroup endpointGroup, String path) {
        return builder(SessionProtocol.of(requireNonNull(protocol, "protocol")), endpointGroup, path);
    }

    /**
     * Returns a new {@link UpstreamBuilder} based on the given {@link EndpointGroup} with the given
     * {@link SessionProtocol} and {@code path}.
     */
    static UpstreamBuilder builder(SessionProtocol protocol, EndpointGroup endpointGroup, String path) {
        return new UpstreamBuilder(protocol, endpointGroup, path);
    }

    /**
     * Sends the given {@link HttpRequest} to this upstream.
     */
    HttpResponse execute(HttpRequest req);
}
