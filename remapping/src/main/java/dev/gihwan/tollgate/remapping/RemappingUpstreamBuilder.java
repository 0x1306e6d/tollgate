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

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import javax.annotation.Nullable;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;

import dev.gihwan.tollgate.gateway.Upstream;

/**
 * A builder for {@link RemappingUpstream}.
 */
public final class RemappingUpstreamBuilder {

    @Nullable
    private RemappingRequestStrategy requestStrategy;
    @Nullable
    private RemappingResponseStrategy responseStrategy;

    RemappingUpstreamBuilder() {}

    /**
     * Adds a new {@link RemappingRequestStrategy} that remaps {@link HttpRequest} path with the given
     * {@code pathPattern}.
     *
     * @see RemappingRequestPathStrategy#path(String)
     * @see RemappingUpstreamBuilder#requestStrategy(RemappingRequestStrategy)
     */
    public RemappingUpstreamBuilder requestPath(String pathPattern) {
        return requestStrategy(RemappingRequestStrategy.path(pathPattern));
    }

    /**
     * Adds the given {@link RemappingRequestStrategy}.
     */
    public RemappingUpstreamBuilder requestStrategy(RemappingRequestStrategy requestStrategy) {
        requireNonNull(requestStrategy, "requestStrategy");
        if (this.requestStrategy == null) {
            this.requestStrategy = requestStrategy;
        } else {
            this.requestStrategy = this.requestStrategy.andThen(requestStrategy);
        }
        return this;
    }

    /**
     * Adds a new {@link RemappingResponseStrategy} remaps {@link HttpStatus} of {@link HttpResponse} with the
     * given {@link HttpStatusFunction}.
     *
     * @see RemappingResponseStrategy#status(HttpStatusFunction)
     * @see RemappingUpstreamBuilder#responseStrategy(RemappingResponseStrategy)
     */
    public RemappingUpstreamBuilder responseStatus(HttpStatusFunction statusFunction) {
        return responseStrategy(RemappingResponseStrategy.status(statusFunction));
    }

    /**
     * Adds the given {@link RemappingResponseStrategy}.
     */
    public RemappingUpstreamBuilder responseStrategy(RemappingResponseStrategy responseStrategy) {
        requireNonNull(responseStrategy, "responseStrategy");
        if (this.responseStrategy == null) {
            this.responseStrategy = responseStrategy;
        } else {
            this.responseStrategy = this.responseStrategy.andThen(responseStrategy);
        }
        return this;
    }

    /**
     * Builds a new {@link RemappingUpstream} decorator based on the properties of this builder.
     */
    public Function<? super Upstream, RemappingUpstream> newDecorator() {
        return this::build;
    }

    /**
     * Builds a new {@link RemappingUpstream} based on the properties of this builder.
     */
    public RemappingUpstream build(Upstream delegate) {
        checkArgument(requestStrategy != null || responseStrategy != null,
                      "Must set at lease one request or response strategy");
        return new RemappingUpstream(delegate, requestStrategy, responseStrategy);
    }
}
