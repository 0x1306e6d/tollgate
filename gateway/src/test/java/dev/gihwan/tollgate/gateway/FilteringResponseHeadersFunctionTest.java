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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpHeaderNames;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.ResponseHeaders;

class FilteringResponseHeadersFunctionTest {
    @Test
    void allowSpecifiedResponseHeaders() {
        final FilteringResponseHeadersFunction function =
                FilteringResponseHeadersFunction.ofAllowedSet(Set.of(HttpHeaderNames.of("foo")));

        final HttpResponse res = HttpResponse.of(ResponseHeaders.builder(HttpStatus.OK)
                                                                .add("foo", "this is foo")
                                                                .add("bar", "this is bar")
                                                                .add("baz", "this is baz")
                                                                .build());
        final AggregatedHttpResponse applied = function.apply(res).aggregate().join();
        final ResponseHeaders headers = applied.headers();
        assertThat(headers.get("foo")).isEqualTo("this is foo");
        assertThat(headers.get("bar")).isNull();
        assertThat(headers.get("baz")).isNull();
    }

    @Test
    void allowSpecifiedResponseHeadersWithMultiValues() {
        final FilteringResponseHeadersFunction function =
                FilteringResponseHeadersFunction.ofAllowedSet(Set.of(HttpHeaderNames.of("foo")));

        final HttpResponse res = HttpResponse.of(ResponseHeaders.builder(HttpStatus.OK)
                                                                .add("foo", "this is first foo")
                                                                .add("foo", "this is second foo")
                                                                .add("bar", "this is first bar")
                                                                .add("bar", "this is second bar")
                                                                .add("baz", "this is first baz")
                                                                .add("baz", "this is second baz")
                                                                .build());
        final AggregatedHttpResponse applied = function.apply(res).aggregate().join();
        final ResponseHeaders headers = applied.headers();
        assertThat(headers.getAll("foo")).containsExactlyInAnyOrder("this is first foo", "this is second foo");
        assertThat(headers.getAll("bar")).isEmpty();
        assertThat(headers.getAll("baz")).isEmpty();
    }

    @Test
    void allowAllSpecifiedResponseHeaders() {
        final FilteringResponseHeadersFunction function =
                FilteringResponseHeadersFunction.ofAllowedSet(Set.of(HttpHeaderNames.of("foo"),
                                                                     HttpHeaderNames.of("bar")));

        final HttpResponse res = HttpResponse.of(ResponseHeaders.builder(HttpStatus.OK)
                                                                .add("foo", "this is foo")
                                                                .add("bar", "this is bar")
                                                                .add("baz", "this is baz")
                                                                .build());
        final AggregatedHttpResponse applied = function.apply(res).aggregate().join();
        final ResponseHeaders headers = applied.headers();
        assertThat(headers.get("foo")).isEqualTo("this is foo");
        assertThat(headers.get("bar")).isEqualTo("this is bar");
        assertThat(headers.get("baz")).isNull();
    }

    @Test
    void disallowSpecifiedResponseHeaders() {
        final FilteringResponseHeadersFunction function =
                FilteringResponseHeadersFunction.ofDisallowedSet(Set.of(HttpHeaderNames.of("foo")));

        final HttpResponse res = HttpResponse.of(ResponseHeaders.builder(HttpStatus.OK)
                                                                .add("foo", "this is foo")
                                                                .add("bar", "this is bar")
                                                                .add("baz", "this is baz")
                                                                .build());
        final AggregatedHttpResponse applied = function.apply(res).aggregate().join();
        final ResponseHeaders headers = applied.headers();
        assertThat(headers.get("foo")).isNull();
        assertThat(headers.get("bar")).isEqualTo("this is bar");
        assertThat(headers.get("baz")).isEqualTo("this is baz");
    }

    @Test
    void disallowSpecifiedResponseHeadersWithMultiValues() {
        final FilteringResponseHeadersFunction function =
                FilteringResponseHeadersFunction.ofDisallowedSet(Set.of(HttpHeaderNames.of("foo")));

        final HttpResponse res = HttpResponse.of(ResponseHeaders.builder(HttpStatus.OK)
                                                                .add("foo", "this is first foo")
                                                                .add("foo", "this is second foo")
                                                                .add("bar", "this is first bar")
                                                                .add("bar", "this is second bar")
                                                                .add("baz", "this is first baz")
                                                                .add("baz", "this is second baz")
                                                                .build());
        final AggregatedHttpResponse applied = function.apply(res).aggregate().join();
        final ResponseHeaders headers = applied.headers();
        assertThat(headers.getAll("foo")).isEmpty();
        assertThat(headers.getAll("bar")).containsExactlyInAnyOrder("this is first bar", "this is second bar");
        assertThat(headers.getAll("baz")).containsExactlyInAnyOrder("this is first baz", "this is second baz");
    }

    @Test
    void disallowAllSpecifiedResponseHeaders() {
        final FilteringResponseHeadersFunction function =
                FilteringResponseHeadersFunction.ofDisallowedSet(Set.of(HttpHeaderNames.of("foo"),
                                                                        HttpHeaderNames.of("bar")));

        final HttpResponse res = HttpResponse.of(ResponseHeaders.builder(HttpStatus.OK)
                                                                .add("foo", "this is foo")
                                                                .add("bar", "this is bar")
                                                                .add("baz", "this is baz")
                                                                .build());
        final AggregatedHttpResponse applied = function.apply(res).aggregate().join();
        final ResponseHeaders headers = applied.headers();
        assertThat(headers.get("foo")).isNull();
        assertThat(headers.get("bar")).isNull();
        assertThat(headers.get("baz")).isEqualTo("this is baz");
    }

    @Test
    void shouldNotFilterPseudoHeaders() {
        final FilteringResponseHeadersFunction function =
                FilteringResponseHeadersFunction.ofAllowedSet(Set.of(HttpHeaderNames.of("foo")));

        final HttpResponse res = HttpResponse.of(ResponseHeaders.builder(HttpStatus.OK)
                                                                .add("foo", "this is foo")
                                                                .add("bar", "this is bar")
                                                                .build());
        final AggregatedHttpResponse applied = function.apply(res).aggregate().join();
        final ResponseHeaders headers = applied.headers();
        assertThat(headers.get("foo")).isEqualTo("this is foo");
        assertThat(headers.get("bar")).isNull();
        assertThat(headers.status()).isEqualTo(HttpStatus.OK);
    }
}
