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

import static dev.gihwan.tollgate.testing.TestGateway.withTestGateway;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.Queue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.testing.junit5.server.ServerExtension;

import dev.gihwan.tollgate.testing.TestGateway;

class UpstreamBindingBuilderTest {

    @RegisterExtension
    static final ServerExtension serviceServer = new ServerExtension() {
        @Override
        protected void configure(ServerBuilder sb) {
            sb.service("/foo", (ctx, req) -> HttpResponse.of("Hello, World!"));
        }
    };

    @Test
    void decorator() {
        final Queue<String> queue = new ArrayDeque<>();

        try (TestGateway gateway = withTestGateway(builder -> {
            builder.route()
                   .path("/foo")
                   .decorator(delegate -> {
                       queue.add("decorator1");
                       return delegate;
                   })
                   .decorator(delegate -> {
                       queue.add("decorator2");
                       return delegate;
                   })
                   .build(Upstream.of(serviceServer.httpUri()));
        })) {
            final WebClient client = WebClient.of(gateway.httpUri());
            client.get("/foo").aggregate().join();

            assertThat(queue).containsExactly("decorator1", "decorator2");
        }
    }

    @Test
    void decorators() {
        final Queue<String> queue = new ArrayDeque<>();

        try (TestGateway gateway = withTestGateway(builder -> {
            builder.route()
                   .path("/foo")
                   .decorators(delegate -> {
                       queue.add("decorator1");
                       return delegate;
                   }, delegate -> {
                       queue.add("decorator2");
                       return delegate;
                   })
                   .build(Upstream.of(serviceServer.httpUri()));
        })) {
            final WebClient client = WebClient.of(gateway.httpUri());
            client.get("/foo").aggregate().join();

            assertThat(queue).containsExactly("decorator1", "decorator2");
        }
    }

    @Test
    void armeriaDecorator() {
        final Queue<String> queue = new ArrayDeque<>();

        try (TestGateway gateway = withTestGateway(builder -> {
            builder.route()
                   .path("/foo")
                   .armeriaDecorator(delegate -> {
                       queue.add("decorator1");
                       return delegate;
                   })
                   .armeriaDecorator(delegate -> {
                       queue.add("decorator2");
                       return delegate;
                   })
                   .build(Upstream.of(serviceServer.httpUri()));
        })) {
            final WebClient client = WebClient.of(gateway.httpUri());
            client.get("/foo").aggregate().join();

            assertThat(queue).containsExactly("decorator1", "decorator2");
        }
    }

    @Test
    void armeriaDecorators() {
        final Queue<String> queue = new ArrayDeque<>();

        try (TestGateway gateway = withTestGateway(builder -> {
            builder.route()
                   .path("/foo")
                   .armeriaDecorators(delegate -> {
                       queue.add("decorator1");
                       return delegate;
                   }, delegate -> {
                       queue.add("decorator2");
                       return delegate;
                   })
                   .build(Upstream.of(serviceServer.httpUri()));
        })) {
            final WebClient client = WebClient.of(gateway.httpUri());
            client.get("/foo").aggregate().join();

            assertThat(queue).containsExactly("decorator1", "decorator2");
        }
    }

    @Test
    void decoratorAndArmeriaDecorator() {
        final Queue<String> queue = new ArrayDeque<>();

        try (TestGateway gateway = withTestGateway(builder -> {
            builder.route()
                   .path("/foo")
                   .decorator(delegate -> {
                       queue.add("decorator1");
                       return delegate;
                   })
                   .armeriaDecorator(delegate -> {
                       queue.add("decorator2");
                       return delegate;
                   })
                   .build(Upstream.of(serviceServer.httpUri()));
        })) {
            final WebClient client = WebClient.of(gateway.httpUri());
            client.get("/foo").aggregate().join();

            assertThat(queue).containsExactly("decorator1", "decorator2");
        }
    }

    @Test
    void armeriaDecoratorAndDecorator() {
        final Queue<String> queue = new ArrayDeque<>();

        try (TestGateway gateway = withTestGateway(builder -> {
            builder.route()
                   .path("/foo")
                   .armeriaDecorator(delegate -> {
                       queue.add("decorator1");
                       return delegate;
                   })
                   .decorator(delegate -> {
                       queue.add("decorator2");
                       return delegate;
                   })
                   .build(Upstream.of(serviceServer.httpUri()));
        })) {
            final WebClient client = WebClient.of(gateway.httpUri());
            client.get("/foo").aggregate().join();

            assertThat(queue).containsExactly("decorator2", "decorator1"); // decorates Armeria decorator later
        }
    }
}
