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

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.server.ServiceRequestContext;

/**
 * A strategy for remapping {@link HttpRequest}.
 */
@FunctionalInterface
public interface RemappingRequestStrategy {

    /**
     * Returns a new {@link RemappingRequestStrategy} that remaps {@link HttpRequest} path with the given
     * {@code pathPattern}.
     */
    static RemappingRequestStrategy path(String pathPattern) {
        return new RemappingRequestPathStrategy(requireNonNull(pathPattern, "pathPattern"));
    }

    /**
     * Remaps the given {@link HttpRequest}.
     */
    HttpRequest remap(ServiceRequestContext ctx, HttpRequest req);

    /**
     * Returns a new composed {@link RemappingRequestStrategy} that first applies this strategy to its
     * {@link HttpRequest}, and then applies the {@code after} strategy to the result.
     */
    default RemappingRequestStrategy andThen(RemappingRequestStrategy after) {
        requireNonNull(after, "after");
        return (ctx, req) -> after.remap(ctx, remap(ctx, req));
    }
}
