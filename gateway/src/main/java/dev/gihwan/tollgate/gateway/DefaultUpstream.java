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

import java.util.Set;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.HttpHeaderNames;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.RequestHeaders;
import com.linecorp.armeria.common.RequestHeadersBuilder;
import com.linecorp.armeria.common.ResponseHeaders;
import com.linecorp.armeria.common.ResponseHeadersBuilder;

import io.netty.util.AsciiString;

/**
 * The default implementation of {@link Upstream}.
 */
final class DefaultUpstream implements Upstream {

    private final WebClient client;

    private final Set<AsciiString> disallowedRequestHeaders;
    private final Set<AsciiString> disallowedResponseHeaders;

    DefaultUpstream(WebClient client,
                    Set<AsciiString> disallowedRequestHeaders,
                    Set<AsciiString> disallowedResponseHeaders) {
        this.client = client;
        this.disallowedRequestHeaders = disallowedRequestHeaders;
        this.disallowedResponseHeaders = disallowedResponseHeaders;
    }

    @Override
    public HttpResponse execute(HttpRequest req) {
        final HttpRequest newReq;
        if (disallowedRequestHeaders.isEmpty()) {
            newReq = req;
        } else {
            newReq = req.mapHeaders(this::disallowRequestHeaders);
        }

        final HttpResponse res = client.execute(newReq);
        final HttpResponse newRes;
        if (disallowedResponseHeaders.isEmpty()) {
            newRes = res;
        } else {
            newRes = res.mapHeaders(this::disallowResponseHeaders);
        }
        return newRes;
    }

    private RequestHeaders disallowRequestHeaders(RequestHeaders headers) {
        final RequestHeadersBuilder builder = headers.toBuilder();
        for (AsciiString disallowed : disallowedRequestHeaders) {
            builder.remove(disallowed);
        }
        return builder.build();
    }

    private ResponseHeaders disallowResponseHeaders(ResponseHeaders headers) {
        final ResponseHeadersBuilder builder = headers.toBuilder();
        for (AsciiString disallowed : disallowedResponseHeaders) {
            builder.remove(disallowed);
        }
        return builder.build();
    }
}
