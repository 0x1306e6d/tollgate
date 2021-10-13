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

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import com.linecorp.armeria.common.HttpStatus;

/**
 * A builder for {@link HttpStatusFunction}.
 */
public final class HttpStatusFunctionBuilder {

    private final Predicate<HttpStatus> predicate;

    HttpStatusFunctionBuilder(Predicate<HttpStatus> predicate) {
        this.predicate = predicate;
    }

    /**
     * Returns a new {@link HttpStatusFunction} which returns the {@code to} {@link HttpStatus} if a given
     * {@link HttpStatus} satisfies the specified {@link Predicate}.
     */
    public HttpStatusFunction to(HttpStatus to) {
        requireNonNull(to, "to");
        return new DefaultHttpStatusFunction(predicate, to);
    }
}
