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

import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.RequestHeaders;
import com.linecorp.armeria.common.RequestHeadersBuilder;

import io.netty.util.AsciiString;

final class DisallowRequestHeadersFunction implements Function<HttpRequest, HttpRequest> {

    static DisallowRequestHeadersFunction ofSet(Set<AsciiString> disallowedRequestHeaders) {
        checkArgument(!disallowedRequestHeaders.isEmpty(), "disallowed request headers should not be empty");
        return new DisallowRequestHeadersFunction((name, value) -> disallowedRequestHeaders.contains(name));
    }

    private final BiPredicate<AsciiString, String> predicate;

    private DisallowRequestHeadersFunction(BiPredicate<AsciiString, String> predicate) {
        this.predicate = predicate;
    }

    @Override
    public HttpRequest apply(HttpRequest req) {
        return req.mapHeaders(this::disallowRequestHeaders);
    }

    private RequestHeaders disallowRequestHeaders(RequestHeaders headers) {
        final RequestHeadersBuilder builder = headers.toBuilder();
        headers.forEach((name, value) -> {
            if (predicate.test(name, value)) {
                builder.remove(name);
            }
        });
        return builder.build();
    }
}
