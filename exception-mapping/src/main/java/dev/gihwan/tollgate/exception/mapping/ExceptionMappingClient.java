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

package dev.gihwan.tollgate.exception.mapping;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.linecorp.armeria.client.ClientRequestContext;
import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.client.SimpleDecoratingHttpClient;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;

/**
 * A {@link HttpClient} decorator which maps a {@link Throwable} to a {@link HttpResponse}.
 */
public final class ExceptionMappingClient extends SimpleDecoratingHttpClient {

    /**
     * Returns a new {@link HttpClient} decorator which maps a {@link Throwable} to a {@link HttpResponse}
     * using the default {@link ExceptionMappingFunction}.
     */
    public static Function<? super HttpClient, ExceptionMappingClient> newDecorator() {
        return newDecorator(ExceptionMappingFunction.ofDefault());
    }

    /**
     * Returns a new {@link HttpClient} decorator which maps a {@link Throwable} to a {@link HttpResponse}
     * using the given {@link ExceptionMappingFunction}.
     */
    public static Function<? super HttpClient, ExceptionMappingClient> newDecorator(
            ExceptionMappingFunction mappingFunction) {
        requireNonNull(mappingFunction, "mappingFunction");
        return delegate -> new ExceptionMappingClient(delegate, mappingFunction);
    }

    private final ExceptionMappingFunction mappingFunction;

    ExceptionMappingClient(HttpClient delegate, ExceptionMappingFunction mappingFunction) {
        super(delegate);
        this.mappingFunction = mappingFunction;
    }

    @Override
    public HttpResponse execute(ClientRequestContext ctx, HttpRequest req) throws Exception {
        final CompletableFuture<HttpResponse> resFuture = new CompletableFuture<>();
        final HttpResponse res = HttpResponse.from(resFuture);
        unwrap().execute(ctx, req).aggregate().handleAsync((aggregated, cause) -> {
            if (cause != null) {
                resFuture.complete(mappingFunction.apply(cause));
                return null;
            }

            resFuture.complete(aggregated.toHttpResponse());
            return null;
        }, ctx.eventLoop());
        return res;
    }
}
