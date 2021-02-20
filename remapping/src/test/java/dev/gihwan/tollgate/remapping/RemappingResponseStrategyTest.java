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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.Queue;

import org.junit.jupiter.api.Test;

import com.linecorp.armeria.client.ClientRequestContext;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;

class RemappingResponseStrategyTest {

    @Test
    void andThen() {
        final Queue<String> queue = new ArrayDeque<>();

        final RemappingResponseStrategy strategy1 = (ctx, res) -> {
            queue.add("strategy1");
            return res;
        };
        final RemappingResponseStrategy strategy2 = (ctx, res) -> {
            queue.add("strategy2");
            return res;
        };

        final HttpRequest req = HttpRequest.of(HttpMethod.GET, "/foo");
        final ClientRequestContext ctx = ClientRequestContext.of(req);
        final HttpResponse res = HttpResponse.of("Hello, World!");

        final RemappingResponseStrategy strategy1AndRule2 = strategy1.andThen(strategy2);
        HttpResponse remapped = strategy1AndRule2.remap(ctx, res);
        assertThat(remapped).isEqualTo(res);
        assertThat(queue).containsExactly("strategy1", "strategy2");

        queue.clear();
        final RemappingResponseStrategy strategy2AndRule1 = strategy2.andThen(strategy1);
        remapped = strategy2AndRule1.remap(ctx, res);
        assertThat(remapped).isEqualTo(res);
        assertThat(queue).containsExactly("strategy2", "strategy1");
    }
}
