/*
 * MIT License
 *
 * Copyright (c) 2020 Gihwan Kim
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

package dev.gihwan.tollgate.core.upstream;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.testing.junit5.server.ServerExtension;

import dev.gihwan.tollgate.core.service.ServiceConfig;

class DefaultUpstreamTest {

    private static final AtomicReference<AggregatedHttpRequest> reqCapture = new AtomicReference<>();

    @RegisterExtension
    static final ServerExtension serviceServer = new ServerExtension() {
        @Override
        protected void configure(ServerBuilder sb) {
            sb.service("/path", (ctx, req) -> HttpResponse.from(req.aggregate()
                                                                   .thenApply(aggregated -> {
                                                                       reqCapture.set(aggregated);
                                                                       return HttpResponse.of(HttpStatus.OK);
                                                                   })));
        }
    };

    @RegisterExtension
    static final ServerExtension tollgateServer = new ServerExtension() {
        @Override
        protected void configure(ServerBuilder sb) {
            sb.decorator(LoggingService.newDecorator());

            configureGetService(sb);
            configurePostService(sb);
            configurePutService(sb);
            configureDeleteService(sb);
        }

        private void configureGetService(ServerBuilder sb) {
            final ServiceConfig service = ServiceConfig.of(serviceServer.httpUri().toString());
            final UpstreamEndpointConfig endpoint = UpstreamEndpointConfig.of(HttpMethod.GET, "/path");
            final UpstreamConfig config = UpstreamConfig.of(service, endpoint);
            sb.service("/get", new DefaultUpstream(config));
        }

        private void configurePostService(ServerBuilder sb) {
            final ServiceConfig service = ServiceConfig.of(serviceServer.httpUri().toString());
            final UpstreamEndpointConfig endpoint = UpstreamEndpointConfig.of(HttpMethod.POST, "/path");
            final UpstreamConfig config = UpstreamConfig.of(service, endpoint);
            sb.service("/post", new DefaultUpstream(config));
        }

        private void configurePutService(ServerBuilder sb) {
            final ServiceConfig service = ServiceConfig.of(serviceServer.httpUri().toString());
            final UpstreamEndpointConfig endpoint = UpstreamEndpointConfig.of(HttpMethod.PUT, "/path");
            final UpstreamConfig config = UpstreamConfig.of(service, endpoint);
            sb.service("/put", new DefaultUpstream(config));
        }

        private void configureDeleteService(ServerBuilder sb) {
            final ServiceConfig service = ServiceConfig.of(serviceServer.httpUri().toString());
            final UpstreamEndpointConfig endpoint = UpstreamEndpointConfig.of(HttpMethod.DELETE, "/path");
            final UpstreamConfig config = UpstreamConfig.of(service, endpoint);
            sb.service("/delete", new DefaultUpstream(config));
        }
    };

    @Test
    void proxyGet() {
        final WebClient webClient = WebClient.builder(tollgateServer.httpUri()).build();

        final AggregatedHttpResponse res = webClient.get("/get").aggregate().join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);

        final AggregatedHttpRequest req = reqCapture.get();
        assertThat(req.method()).isEqualTo(HttpMethod.GET);
        assertThat(req.path()).isEqualTo("/path");
    }

    @Test
    void proxyPost() {
        final WebClient webClient = WebClient.builder(tollgateServer.httpUri()).build();

        final AggregatedHttpResponse res = webClient.post("/post", "Hello, World!").aggregate().join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);

        final AggregatedHttpRequest req = reqCapture.get();
        assertThat(req.method()).isEqualTo(HttpMethod.POST);
        assertThat(req.path()).isEqualTo("/path");
        assertThat(req.contentUtf8()).isEqualTo("Hello, World!");
    }

    @Test
    void proxyPut() {
        final WebClient webClient = WebClient.builder(tollgateServer.httpUri()).build();

        final AggregatedHttpResponse res = webClient.post("/put", "Hello, World!").aggregate().join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);

        final AggregatedHttpRequest req = reqCapture.get();
        assertThat(req.method()).isEqualTo(HttpMethod.PUT);
        assertThat(req.path()).isEqualTo("/path");
        assertThat(req.contentUtf8()).isEqualTo("Hello, World!");
    }

    @Test
    void proxyDelete() {
        final WebClient webClient = WebClient.builder(tollgateServer.httpUri()).build();

        final AggregatedHttpResponse res = webClient.get("/delete").aggregate().join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);

        final AggregatedHttpRequest req = reqCapture.get();
        assertThat(req.method()).isEqualTo(HttpMethod.DELETE);
        assertThat(req.path()).isEqualTo("/path");
    }
}
