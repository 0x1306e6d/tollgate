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

package dev.gihwan.tollgate.gateway.remapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.server.ServiceRequestContext;

class RemappingRuleTest {

    @Test
    void of() {
        final Queue<String> queue = new ArrayDeque<>();

        final AtomicReference<HttpRequest> rule1Capture = new AtomicReference<>();
        final RemappingRule rule1 = (ctx, req) -> {
            queue.add("rule1");
            rule1Capture.set(req);
            return req;
        };

        final AtomicReference<HttpRequest> rule2Capture = new AtomicReference<>();
        final RemappingRule rule2 = (ctx, req) -> {
            queue.add("rule2");
            rule2Capture.set(req);
            return req;
        };

        final HttpRequest req = HttpRequest.of(HttpMethod.GET, "/foo");
        final ServiceRequestContext ctx = ServiceRequestContext.of(req);

        final RemappingRule rule = RemappingRule.of(rule1, rule2);
        final HttpRequest remapped = rule.remap(ctx, req);
        assertThat(remapped).isEqualTo(req);
        assertThat(rule1Capture).hasValue(req);
        assertThat(rule2Capture).hasValue(req);
        assertThat(queue).containsExactly("rule1", "rule2");
    }

    @Test
    void andThen() {
        final Queue<String> queue = new ArrayDeque<>();

        final RemappingRule rule1 = (ctx, req) -> {
            queue.add("rule1");
            return req;
        };
        final RemappingRule rule2 = (ctx, req) -> {
            queue.add("rule2");
            return req;
        };

        final HttpRequest req = HttpRequest.of(HttpMethod.GET, "/foo");
        final ServiceRequestContext ctx = ServiceRequestContext.of(req);

        final RemappingRule rule1AndRule2 = rule1.andThen(rule2);
        HttpRequest remapped = rule1AndRule2.remap(ctx, req);
        assertThat(remapped).isEqualTo(req);
        assertThat(queue).containsExactly("rule1", "rule2");

        queue.clear();
        final RemappingRule rule2AndRule1 = rule2.andThen(rule1);
        remapped = rule2AndRule1.remap(ctx, req);
        assertThat(remapped).isEqualTo(req);
        assertThat(queue).containsExactly("rule2", "rule1");
    }
}
