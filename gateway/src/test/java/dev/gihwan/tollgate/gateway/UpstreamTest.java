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

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpHeaders;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.RequestHeaders;
import com.linecorp.armeria.common.ResponseHeaders;
import com.linecorp.armeria.common.logging.RequestLog;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.logging.ContentPreviewingService;
import com.linecorp.armeria.testing.junit5.server.ServerExtension;

import dev.gihwan.tollgate.junit5.GatewayExtension;
import dev.gihwan.tollgate.testing.TestGateway;

class UpstreamTest {

    private static final AtomicReference<ServiceRequestContext> ctxCapture = new AtomicReference<>();
    private static final AtomicReference<ServiceRequestContext> serviceCtxCapture = new AtomicReference<>();

    @RegisterExtension
    static final ServerExtension serviceServer = new ServerExtension() {
        @Override
        protected void configure(ServerBuilder builder) {
            builder.service("/", (ctx, req) -> HttpResponse.of(HttpStatus.OK));
            builder.service("/body", (ctx, req) -> HttpResponse.from(req.aggregate().thenApply(
                    aggregated -> HttpResponse.of("Hello, " + aggregated.contentUtf8() + "!"))));

            builder.service("/header", (ctx, req) -> HttpResponse.of(
                    ResponseHeaders.of(HttpStatus.OK,
                                       "public", "this is public",
                                       "private", "this is private")));

            builder.decorator(ContentPreviewingService.newDecorator(Integer.MAX_VALUE));
            builder.decorator(((delegate, ctx, req) -> {
                serviceCtxCapture.set(ctx);
                return delegate.serve(ctx, req);
            }));
        }
    };

    @RegisterExtension
    static final GatewayExtension gateway = new GatewayExtension() {
        @Override
        protected void configure(GatewayBuilder builder) {
            builder.upstream("/", Upstream.of(serviceServer.httpUri()));
            builder.upstream("/body", Upstream.of(serviceServer.httpUri()));
            builder.server(serverBuilder -> serverBuilder.decorator(((delegate, ctx, req) -> {
                ctxCapture.set(ctx);
                return delegate.serve(ctx, req);
            })));
        }
    };

    @Test
    void proxy() {
        final WebClient client = WebClient.builder(gateway.httpUri()).build();

        final AggregatedHttpResponse res = client.get("/").aggregate().join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);

        final ServiceRequestContext ctx = ctxCapture.get();
        assertThat(ctx.method()).isEqualTo(HttpMethod.GET);
        assertThat(ctx.path()).isEqualTo("/");

        final ServiceRequestContext serviceCtx = serviceCtxCapture.get();
        assertThat(serviceCtx.method()).isEqualTo(HttpMethod.GET);
        assertThat(serviceCtx.path()).isEqualTo("/");
    }

    @Test
    void proxyWithBody() {
        final WebClient client = WebClient.builder(gateway.httpUri()).build();

        final HttpRequest req = HttpRequest.of(HttpMethod.POST, "/body", MediaType.PLAIN_TEXT, "Tollgate");
        final AggregatedHttpResponse res = client.execute(req).aggregate().join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);
        assertThat(res.contentUtf8()).isEqualTo("Hello, Tollgate!");

        final ServiceRequestContext ctx = ctxCapture.get();
        assertThat(ctx.method()).isEqualTo(HttpMethod.POST);
        assertThat(ctx.path()).isEqualTo("/body");

        final ServiceRequestContext serviceCtx = serviceCtxCapture.get();
        assertThat(serviceCtx.method()).isEqualTo(HttpMethod.POST);
        assertThat(serviceCtx.path()).isEqualTo("/body");
        final RequestLog serviceLog = serviceCtx.log().whenComplete().join();
        assertThat(serviceLog.requestContentPreview()).isEqualTo("Tollgate");
        assertThat(serviceLog.responseContentPreview()).isEqualTo("Hello, Tollgate!");
    }

    @Test
    void mapRequest() {
        try (TestGateway gateway = withTestGateway(builder -> {
            builder.upstream("/",
                             Upstream.builder(serviceServer.httpUri())
                                     .mapRequest(req -> req.mapHeaders(headers -> headers.toBuilder()
                                                                                         .add("foo",
                                                                                              "this is foo")
                                                                                         .build()))
                                     .build());
        })) {
            final WebClient client = WebClient.builder(gateway.httpUri()).build();
            final AggregatedHttpResponse res = client.get("/").aggregate().join();
            assertThat(res.status()).isEqualTo(HttpStatus.OK);

            final ServiceRequestContext serviceCtx = serviceCtxCapture.get();
            assertThat(serviceCtx.method()).isEqualTo(HttpMethod.GET);
            final HttpHeaders serviceHeaders = serviceCtx.request().headers();
            assertThat(serviceHeaders.get("foo")).isEqualTo("this is foo");
        }
    }

    @Test
    void mapRequestShouldTransformInOrder() {
        try (TestGateway gateway = withTestGateway(builder -> {
            builder.upstream("/", Upstream.builder(serviceServer.httpUri())
                                          .mapRequest(req -> HttpRequest.of(HttpMethod.POST, "/"))
                                          .mapRequest(req -> HttpRequest.of(HttpMethod.PUT, "/"))
                                          .build());
        })) {
            final WebClient client = WebClient.builder(gateway.httpUri()).build();
            final AggregatedHttpResponse res = client.get("/").aggregate().join();
            assertThat(res.status()).isEqualTo(HttpStatus.OK);

            final ServiceRequestContext serviceCtx = serviceCtxCapture.get();
            assertThat(serviceCtx.method()).isEqualTo(HttpMethod.PUT);
        }
    }

    @Test
    void disallowRequestHeaders() {
        try (TestGateway gateway = withTestGateway(builder -> {
            builder.upstream("/", Upstream.builder(serviceServer.httpUri())
                                          .disallowRequestHeaders("private")
                                          .build());
        })) {
            final WebClient client = WebClient.builder(gateway.httpUri()).build();
            final HttpRequest req = HttpRequest.of(RequestHeaders.of(HttpMethod.GET, "/",
                                                                     "public", "this is public",
                                                                     "private", "this is private"));
            final AggregatedHttpResponse res = client.execute(req).aggregate().join();
            assertThat(res.status()).isEqualTo(HttpStatus.OK);

            final ServiceRequestContext serviceCtx = serviceCtxCapture.get();
            assertThat(serviceCtx.method()).isEqualTo(HttpMethod.GET);
            final HttpHeaders serviceHeaders = serviceCtx.request().headers();
            assertThat(serviceHeaders.get("public")).isEqualTo("this is public");
            assertThat(serviceHeaders.get("private")).isNull();
        }
    }

    @Test
    void mapResponse() {
        try (TestGateway gateway = withTestGateway(builder -> {
            builder.upstream("/",
                             Upstream.builder(serviceServer.httpUri())
                                     .mapResponse(res -> res.mapHeaders(headers -> headers.toBuilder()
                                                                                          .add("foo",
                                                                                               "this is foo")
                                                                                          .build()))
                                     .build());
        })) {
            final WebClient client = WebClient.builder(gateway.httpUri()).build();
            final AggregatedHttpResponse res = client.get("/").aggregate().join();
            assertThat(res.status()).isEqualTo(HttpStatus.OK);
            assertThat(res.headers().get("foo")).isEqualTo("this is foo");
        }
    }

    @Test
    void mapResponseShouldTransformInOrder() {
        try (TestGateway gateway = withTestGateway(builder -> {
            builder.upstream("/", Upstream.builder(serviceServer.httpUri())
                                          .mapResponse(res -> HttpResponse.of(HttpStatus.CREATED))
                                          .mapResponse(res -> HttpResponse.of(HttpStatus.NO_CONTENT))
                                          .build());
        })) {
            final WebClient client = WebClient.builder(gateway.httpUri()).build();
            final AggregatedHttpResponse res = client.get("/").aggregate().join();
            assertThat(res.status()).isEqualTo(HttpStatus.NO_CONTENT);
        }
    }

    @Test
    void disallowResponseHeaders() {
        try (TestGateway gateway = withTestGateway(builder -> {
            builder.upstream("/header", Upstream.builder(serviceServer.httpUri())
                                                .disallowResponseHeaders("private")
                                                .build());
        })) {
            final WebClient client = WebClient.builder(gateway.httpUri()).build();
            final AggregatedHttpResponse res = client.get("/header").aggregate().join();
            assertThat(res.status()).isEqualTo(HttpStatus.OK);
            assertThat(res.headers().get("public")).isEqualTo("this is public");
            assertThat(res.headers().get("private")).isNull();
        }
    }
}
