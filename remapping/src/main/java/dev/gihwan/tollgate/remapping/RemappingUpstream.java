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

package dev.gihwan.tollgate.remapping;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import javax.annotation.Nullable;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.ServiceRequestContext;

import dev.gihwan.tollgate.gateway.DecoratingUpstream;
import dev.gihwan.tollgate.gateway.Upstream;

/**
 * A {@link Upstream} decorator for remapping {@link HttpRequest}s.
 */
public final class RemappingUpstream extends DecoratingUpstream {

    /**
     * Returns a new {@link Upstream} decorator which remaps {@link HttpRequest} using the given
     * {@link RemappingRequestStrategy}.
     */
    public static Function<? super Upstream, RemappingUpstream> newDecorator(
            RemappingRequestStrategy requestStrategy) {
        return builder().requestStrategy(requireNonNull(requestStrategy, "requestStrategy")).newDecorator();
    }

    /**
     * Returns a new {@link Upstream} decorator which remaps {@link HttpResponse} using the given
     * {@link RemappingResponseStrategy}.
     */
    public static Function<? super Upstream, RemappingUpstream> newDecorator(
            RemappingResponseStrategy responseStrategy) {
        return builder().responseStrategy(requireNonNull(responseStrategy, "responseStrategy")).newDecorator();
    }

    /**
     * Returns a new {@link Upstream} decorator which remaps {@link HttpRequest} and {@link HttpResponse} using
     * the given {@link RemappingRequestStrategy} and {@link RemappingResponseStrategy} respectively.
     */
    public static Function<? super Upstream, RemappingUpstream> newDecorator(
            RemappingRequestStrategy requestStrategy, RemappingResponseStrategy responseStrategy) {
        return builder().requestStrategy(requireNonNull(requestStrategy, "requestStrategy"))
                        .responseStrategy(requireNonNull(responseStrategy, "responseStrategy"))
                        .newDecorator();
    }

    /**
     * Returns a new {@link RemappingUpstreamBuilder}.
     */
    public static RemappingUpstreamBuilder builder() {
        return new RemappingUpstreamBuilder();
    }

    @Nullable
    private final RemappingRequestStrategy requestStrategy;
    @Nullable
    private final RemappingResponseStrategy responseStrategy;

    RemappingUpstream(Upstream delegate,
                      @Nullable RemappingRequestStrategy requestStrategy,
                      @Nullable RemappingResponseStrategy responseStrategy) {
        super(delegate);
        this.requestStrategy = requestStrategy;
        this.responseStrategy = responseStrategy;
    }

    @Override
    public final HttpResponse forward(ServiceRequestContext ctx, HttpRequest req) throws Exception {
        return remapRes(ctx, unwrap().forward(ctx, remapReq(ctx, req)));
    }

    private HttpRequest remapReq(ServiceRequestContext ctx, HttpRequest req) {
        if (requestStrategy == null) {
            return req;
        }
        return requestStrategy.remap(ctx, req);
    }

    private HttpResponse remapRes(ServiceRequestContext ctx, HttpResponse res) {
        if (responseStrategy == null) {
            return res;
        }
        return responseStrategy.remap(ctx, res);
    }
}
