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

package dev.gihwan.tollgate.core;

import static dev.gihwan.tollgate.testing.TestingTollgate.withTestingTollgate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.awaitility.Awaitility.await;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.testing.junit5.server.ServerExtension;

import dev.gihwan.tollgate.junit5.TollgateExtension;
import dev.gihwan.tollgate.testing.TestingTollgate;

class DefaultUpstreamTest {

    private static final AtomicReference<AggregatedHttpRequest> reqCapture = new AtomicReference<>();

    @RegisterExtension
    static final ServerExtension serviceServer = new ServerExtension() {
        @Override
        protected void configure(ServerBuilder sb) {
            sb.service("/foo",
                       (ctx, req) -> HttpResponse.from(req.aggregate().thenApply(aggregated -> {
                           reqCapture.set(aggregated);
                           return HttpResponse.of("bar");
                       })));
        }
    };

    @RegisterExtension
    static final TollgateExtension tollgate = new TollgateExtension() {
        @Override
        protected void configure(TollgateBuilder builder) {
            builder.upstream("/foo", Upstream.of(serviceServer.httpUri()));
        }
    };

    @Test
    void proxy() {
        final WebClient webClient = WebClient.builder(tollgate.httpUri()).build();

        final CompletableFuture<AggregatedHttpResponse> future = webClient.get("/foo").aggregate();
        await().atMost(Duration.ofSeconds(1)).until(future::isDone);
        final AggregatedHttpResponse res = future.join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);
        assertThat(res.contentUtf8()).isEqualTo("bar");

        final AggregatedHttpRequest req = reqCapture.get();
        assertThat(req.method()).isEqualTo(HttpMethod.GET);
        assertThat(req.path()).isEqualTo("/foo");
    }

    @Test
    void proxyWithBody() {
        final WebClient webClient = WebClient.builder(tollgate.httpUri()).build();

        final CompletableFuture<AggregatedHttpResponse> future = webClient.post("/foo", "qux").aggregate();
        await().atMost(Duration.ofSeconds(1)).until(future::isDone);
        final AggregatedHttpResponse res = future.join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);
        assertThat(res.contentUtf8()).isEqualTo("bar");

        final AggregatedHttpRequest req = reqCapture.get();
        assertThat(req.method()).isEqualTo(HttpMethod.POST);
        assertThat(req.path()).isEqualTo("/foo");
        assertThat(req.contentUtf8()).isEqualTo("qux");
    }

    @Test
    void respondServiceUnavailableWhenUnProcessedRequestException() {
        try (TestingTollgate tollgate = withTestingTollgate(builder -> {
            final int unusedPort;
            try (ServerSocket ss = new ServerSocket(0)) {
                unusedPort = ss.getLocalPort();
            } catch (IOException e) {
                fail(e.getMessage());
                return;
            }

            builder.upstream("/refused", Upstream.of("http", Endpoint.of("127.0.0.1", unusedPort)));
        })) {
            final WebClient webClient = WebClient.builder(tollgate.httpUri()).build();

            final AggregatedHttpResponse res = webClient.get("/refused").aggregate().join();
            assertThat(res.status()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
