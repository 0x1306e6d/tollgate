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

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.ServiceRequestContext;

import dev.gihwan.tollgate.gateway.DecoratingUpstream;
import dev.gihwan.tollgate.gateway.Upstream;

/**
 * A {@link Upstream} decorator for remapping {@link HttpRequest}s.
 */
public final class RemappingRequestUpstream extends DecoratingUpstream {

    /**
     * Returns a new {@link Upstream} decorator which remaps the {@link HttpRequest} using the given
     * {@link RemappingRequestStrategy}.
     */
    public static Function<? super Upstream, RemappingRequestUpstream> newDecorator(
            RemappingRequestStrategy strategy) {
        return builder().strategy(requireNonNull(strategy, "strategy")).newDecorator();
    }

    /**
     * Returns a new {@link RemappingRequestUpstreamBuilder}.
     */
    public static RemappingRequestUpstreamBuilder builder() {
        return new RemappingRequestUpstreamBuilder();
    }

    private final RemappingRequestStrategy strategy;

    RemappingRequestUpstream(Upstream delegate, RemappingRequestStrategy strategy) {
        super(delegate);
        this.strategy = strategy;
    }

    @Override
    public final HttpResponse forward(ServiceRequestContext ctx, HttpRequest req) throws Exception {
        return unwrap().forward(ctx, strategy.remap(ctx, req));
    }
}
