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

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import com.linecorp.armeria.common.HttpStatus;

/**
 * A {@link FunctionalInterface} for mapping a {@link HttpStatus} to another {@link HttpStatus}.
 */
@FunctionalInterface
public interface HttpStatusFunction extends Function<HttpStatus, HttpStatus> {

    /**
     * Returns a new {@link HttpStatusFunctionBuilder} which produces only if a {@link HttpStatus} argument is
     * in the given {@code from} {@link HttpStatus}es.
     */
    static HttpStatusFunctionBuilder from(HttpStatus... from) {
        requireNonNull(from, "from");
        return from(ImmutableSet.copyOf(from));
    }

    /**
     * Returns a new {@link HttpStatusFunctionBuilder} which produces only if a {@link HttpStatus} argument is
     * in the given {@code from} {@link HttpStatus}es.
     */
    static HttpStatusFunctionBuilder from(Iterable<HttpStatus> from) {
        requireNonNull(from, "from");
        checkArgument(!Iterables.isEmpty(from), "from should not be empty");
        return when(new ContainsHttpStatusPredicate(ImmutableSet.copyOf(from)));
    }

    /**
     * Returns a new {@link HttpStatusFunctionBuilder} with the given {@link Predicate}.
     */
    static HttpStatusFunctionBuilder when(Predicate<HttpStatus> predicate) {
        requireNonNull(predicate, "predicate");
        return new HttpStatusFunctionBuilder(predicate);
    }

    /**
     * Maps the given {@link HttpStatus} to another {@link HttpStatus}.
     */
    @Override
    HttpStatus apply(HttpStatus status);
}
