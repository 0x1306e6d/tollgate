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

import java.util.concurrent.CompletableFuture;

import com.linecorp.armeria.client.UnprocessedRequestException;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;

/**
 * A {@link HttpService} which forwards {@link HttpRequest}s to the specified {@link Upstream}.
 */
final class UpstreamHttpService implements HttpService {

    private final Upstream upstream;

    UpstreamHttpService(Upstream upstream) {
        this.upstream = upstream;
    }

    @Override
    public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
        final CompletableFuture<HttpResponse> resFuture = new CompletableFuture<>();
        final HttpResponse res = HttpResponse.from(resFuture);
        upstream.client().execute(req).aggregate().handleAsync((aggregated, cause) -> {
            if (cause != null) {
                resolveException(resFuture, cause);
                return null;
            }

            resFuture.complete(aggregated.toHttpResponse());
            return null;
        }, ctx.eventLoop());
        return res;
    }

    private static void resolveException(CompletableFuture<HttpResponse> responseFuture, Throwable t) {
        if (t instanceof UnprocessedRequestException) {
            responseFuture.complete(HttpResponse.of(HttpStatus.SERVICE_UNAVAILABLE));
            return;
        }
        responseFuture.completeExceptionally(t);
    }
}
