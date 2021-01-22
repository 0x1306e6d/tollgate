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

import javax.annotation.CheckReturnValue;

import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.server.Service;
import com.linecorp.armeria.server.ServiceRequestContext;

public interface Upstream extends Service<HttpRequest, HttpResponse> {

    static Upstream of(String uri) {
        return builder(uri).build();
    }

    static Upstream of(URI uri) {
        return builder(uri).build();
    }

    static Upstream of(String protocol, EndpointGroup endpointGroup) {
        return builder(protocol, endpointGroup).build();
    }

    static Upstream of(String protocol, EndpointGroup endpointGroup, String path) {
        return builder(protocol, endpointGroup, path).build();
    }

    static UpstreamBuilder builder(String uri) {
        return builder(URI.create(requireNonNull(uri, "uri")));
    }

    static UpstreamBuilder builder(URI uri) {
        return new UpstreamBuilder(requireNonNull(uri, "uri"));
    }

    static UpstreamBuilder builder(String protocol, EndpointGroup endpointGroup) {
        return builder(SessionProtocol.of(requireNonNull(protocol, "protocol")), endpointGroup);
    }

    static UpstreamBuilder builder(SessionProtocol protocol, EndpointGroup endpointGroup) {
        return new UpstreamBuilder(protocol, endpointGroup);
    }

    static UpstreamBuilder builder(String protocol, EndpointGroup endpointGroup, String path) {
        return builder(SessionProtocol.of(requireNonNull(protocol, "protocol")), endpointGroup, path);
    }

    static UpstreamBuilder builder(SessionProtocol protocol, EndpointGroup endpointGroup, String path) {
        return new UpstreamBuilder(protocol, endpointGroup, path);
    }

    @CheckReturnValue
    HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception;

    default <T extends Upstream> T decorate(Function<? super Upstream, T> decorator) {
        final T newUpstream = decorator.apply(this);

        if (newUpstream == null) {
            throw new NullPointerException("decorator.apply() returned null: " + decorator);
        }

        return newUpstream;
    }
}
