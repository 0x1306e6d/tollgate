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

import static dev.gihwan.tollgate.core.server.AttributeKeys.REQUEST_HEADERS;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;

import com.linecorp.armeria.client.UnprocessedRequestException;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.RequestHeaders;
import com.linecorp.armeria.server.ServiceRequestContext;

import dev.gihwan.tollgate.core.service.Service;

final class DefaultUpstreamHttpService implements UpstreamHttpService {

    private final Service service;

    DefaultUpstreamHttpService(Service service) {
        this.service = requireNonNull(service, "service");
    }

    @Override
    public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) {
        return HttpResponse.from(req.aggregate()
                                    .thenApply(aggregated -> duplicateRequest(ctx, aggregated))
                                    .thenApply(this::sendRequest));
    }

    private HttpRequest duplicateRequest(ServiceRequestContext ctx, AggregatedHttpRequest req) {
        final RequestHeaders requestHeaders = defaultIfNull(ctx.attr(REQUEST_HEADERS), req.headers());
        return HttpRequest.of(requestHeaders, req.content(), req.trailers());
    }

    private HttpResponse sendRequest(HttpRequest req) {
        final CompletableFuture<HttpResponse> responseFuture = new CompletableFuture<>();
        final HttpResponse res = HttpResponse.from(responseFuture);
        service.send(req).aggregate().handle((aggregated, t) -> {
            if (t != null) {
                resolveException(responseFuture, t);
                return null;
            }

            responseFuture.complete(aggregated.toHttpResponse());
            return null;
        });
        return res;
    }

    private void resolveException(CompletableFuture<HttpResponse> responseFuture, Throwable t) {
        if (t instanceof UnprocessedRequestException) {
            resolveException(responseFuture, ((UnprocessedRequestException) t).getCause());
            return;
        }
        if (t instanceof UnknownHostException) {
            responseFuture.complete(HttpResponse.of(HttpStatus.BAD_GATEWAY));
            return;
        }
        responseFuture.completeExceptionally(t);
    }
}
