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

import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.ResponseHeaders;
import com.linecorp.armeria.common.ResponseHeadersBuilder;

import io.netty.util.AsciiString;

final class FilteringResponseHeadersFunction implements Function<HttpResponse, HttpResponse> {

    static FilteringResponseHeadersFunction ofAllowedSet(Set<AsciiString> allowedResponseHeaders) {
        return new FilteringResponseHeadersFunction((name, value) -> !allowedResponseHeaders.contains(name));
    }

    static FilteringResponseHeadersFunction ofDisallowedSet(Set<AsciiString> disallowedResponseHeaders) {
        return new FilteringResponseHeadersFunction((name, value) -> disallowedResponseHeaders.contains(name));
    }

    private final BiPredicate<AsciiString, String> predicate;

    private FilteringResponseHeadersFunction(BiPredicate<AsciiString, String> predicate) {
        this.predicate = predicate;
    }

    @Override
    public HttpResponse apply(HttpResponse res) {
        return res.mapHeaders(this::filterResponseHeaders);
    }

    private ResponseHeaders filterResponseHeaders(ResponseHeaders headers) {
        final ResponseHeadersBuilder builder = headers.toBuilder();
        headers.forEach((name, value) -> {
            if (predicate.test(name, value)) {
                builder.remove(name);
            }
        });
        return builder.build();
    }
}
