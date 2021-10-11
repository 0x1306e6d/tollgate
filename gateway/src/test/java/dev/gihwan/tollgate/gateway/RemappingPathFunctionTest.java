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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.util.SafeCloseable;
import com.linecorp.armeria.server.RoutingResult;
import com.linecorp.armeria.server.ServiceRequestContext;

class RemappingPathFunctionTest {
    @Test
    void remapPath() {
        final RemappingPathFunction function = new RemappingPathFunction("/foo/bar");

        final HttpRequest req = HttpRequest.of(HttpMethod.GET, "/");
        final HttpRequest applied = function.apply(req);
        assertThat(applied.path()).isEqualTo("/foo/bar");
    }

    @Test
    void remapPathWithPathParams() {
        final RemappingPathFunction function = new RemappingPathFunction("/foo/{bar}");

        final HttpRequest req = HttpRequest.of(HttpMethod.GET, "/baz/qux");
        final ServiceRequestContext ctx =
                ServiceRequestContext.builder(req)
                                     .routingResult(RoutingResult.builder()
                                                                 .path(req.path())
                                                                 .decodedParam("bar", "qux")
                                                                 .build())
                                     .build();
        try (SafeCloseable ignored = ctx.push()) {
            final HttpRequest applied = function.apply(req);
            assertThat(applied.path()).isEqualTo("/foo/qux");
        }
    }

    @Test
    void shouldThrowIllegalStateExceptionIfPathParamDoesNotExist() {
        final RemappingPathFunction function = new RemappingPathFunction("/foo/{bar}");

        final HttpRequest req = HttpRequest.of(HttpMethod.GET, "/baz/qux");
        final ServiceRequestContext ctx =
                ServiceRequestContext.builder(req)
                                     .routingResult(RoutingResult.builder()
                                                                 .path(req.path())
                                                                 .decodedParam("foo", "baz")
                                                                 .build())
                                     .build();
        try (SafeCloseable ignored = ctx.push()) {
            assertThatThrownBy(() -> function.apply(req))
                    .isInstanceOf(IllegalStateException.class);
        }
    }
}
