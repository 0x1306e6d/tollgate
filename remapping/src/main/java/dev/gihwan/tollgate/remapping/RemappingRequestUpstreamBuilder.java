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

import dev.gihwan.tollgate.gateway.Upstream;

/**
 * A builder for {@link RemappingRequestUpstream}.
 */
public final class RemappingRequestUpstreamBuilder {

    @Nullable
    private RemappingRequestStrategy strategy;

    RemappingRequestUpstreamBuilder() {}

    /**
     * Adds a new {@link RemappingRequestStrategy} that remaps {@link HttpRequest} path with the given
     * {@code pathPattern}.
     *
     * @see RemappingRequestPathStrategy#path(String)
     * @see RemappingRequestUpstreamBuilder#strategy(RemappingRequestStrategy)
     */
    public RemappingRequestUpstreamBuilder path(String pathPattern) {
        return strategy(RemappingRequestStrategy.path(pathPattern));
    }

    /**
     * Adds the given {@link RemappingRequestStrategy}.
     */
    public RemappingRequestUpstreamBuilder strategy(RemappingRequestStrategy strategy) {
        requireNonNull(strategy, "strategy");
        if (this.strategy == null) {
            this.strategy = strategy;
        } else {
            this.strategy = this.strategy.andThen(strategy);
        }
        return this;
    }

    /**
     * Builds a new {@link RemappingRequestUpstream} decorator based on the properties of this builder.
     */
    public Function<? super Upstream, RemappingRequestUpstream> newDecorator() {
        return this::build;
    }

    /**
     * Builds a new {@link RemappingRequestUpstream} based on the properties of this builder.
     */
    public RemappingRequestUpstream build(Upstream delegate) {
        checkArgument(strategy != null, "Must set at lease one remapping strategy");
        return new RemappingRequestUpstream(delegate, strategy);
    }
}
