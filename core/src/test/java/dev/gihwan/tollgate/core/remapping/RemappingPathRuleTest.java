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

package dev.gihwan.tollgate.core.remapping;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.server.RoutingResult;
import com.linecorp.armeria.server.ServiceRequestContext;

class RemappingPathRuleTest {

    @Test
    void remappingPath() {
        final RemappingPathRule rule = new RemappingPathRule("/foo/bar");

        final HttpRequest req = HttpRequest.of(HttpMethod.GET, "/baz/qux/quux");
        final ServiceRequestContext ctx = ServiceRequestContext.of(req);

        final HttpRequest remappedReq = rule.remap(ctx, req);
        assertThat(remappedReq.path()).isEqualTo("/foo/bar");
    }

    @Test
    void remappingPathParam() {
        final RemappingPathRule rule = new RemappingPathRule("/foo/{bar}");

        final HttpRequest req = HttpRequest.of(HttpMethod.GET, "/foo/baz/qux");
        final ServiceRequestContext ctx =
                ServiceRequestContext.builder(req)
                                     .routingResult(RoutingResult.builder()
                                                                 .path(req.path())
                                                                 .decodedParam("bar", "qux")
                                                                 .build())
                                     .build();

        final HttpRequest remappedReq = rule.remap(ctx, req);
        assertThat(remappedReq.path()).isEqualTo("/foo/qux");
    }
}
