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

import com.linecorp.armeria.common.HttpHeaderNames;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.RequestHeaders;

class DisallowRequestHeadersFunctionTest {
    @Test
    void ofSetShouldDisallowSpecifiedRequestHeaders() {
        final DisallowRequestHeadersFunction function =
                DisallowRequestHeadersFunction.ofSet(Set.of(HttpHeaderNames.of("foo")));

        final HttpRequest req = HttpRequest.of(RequestHeaders.builder(HttpMethod.GET, "/")
                                                             .add("foo", "this is foo")
                                                             .add("bar", "this is bar")
                                                             .add("baz", "this is baz")
                                                             .build());
        final HttpRequest applied = function.apply(req);
        final RequestHeaders headers = applied.headers();
        assertThat(headers.get("foo")).isNull();
        assertThat(headers.get("bar")).isEqualTo("this is bar");
        assertThat(headers.get("baz")).isEqualTo("this is baz");
    }

    @Test
    void ofSetShouldDisallowSpecifiedRequestHeadersWithMultiValues() {
        final DisallowRequestHeadersFunction function =
                DisallowRequestHeadersFunction.ofSet(Set.of(HttpHeaderNames.of("foo")));

        final HttpRequest req = HttpRequest.of(RequestHeaders.builder(HttpMethod.GET, "/")
                                                             .add("foo", "this is first foo")
                                                             .add("foo", "this is second foo")
                                                             .add("bar", "this is first bar")
                                                             .add("bar", "this is second bar")
                                                             .add("baz", "this is first baz")
                                                             .add("baz", "this is second baz")
                                                             .build());
        final HttpRequest applied = function.apply(req);
        final RequestHeaders headers = applied.headers();
        assertThat(headers.getAll("foo")).isEmpty();
        assertThat(headers.getAll("bar")).containsExactlyInAnyOrder("this is first bar", "this is second bar");
        assertThat(headers.getAll("baz")).containsExactlyInAnyOrder("this is first baz", "this is second baz");
    }

    @Test
    void ofSetShouldDisallowAllSpecifiedRequestHeaders() {
        final DisallowRequestHeadersFunction function =
                DisallowRequestHeadersFunction.ofSet(Set.of(HttpHeaderNames.of("foo"),
                                                            HttpHeaderNames.of("bar")));

        final HttpRequest req = HttpRequest.of(RequestHeaders.builder(HttpMethod.GET, "/")
                                                             .add("foo", "this is foo")
                                                             .add("bar", "this is bar")
                                                             .add("baz", "this is baz")
                                                             .build());
        final HttpRequest applied = function.apply(req);
        final RequestHeaders headers = applied.headers();
        assertThat(headers.get("foo")).isNull();
        assertThat(headers.get("bar")).isNull();
        assertThat(headers.get("baz")).isEqualTo("this is baz");
    }
}
