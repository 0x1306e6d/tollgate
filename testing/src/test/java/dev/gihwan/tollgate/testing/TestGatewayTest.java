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

package dev.gihwan.tollgate.testing;

import static dev.gihwan.tollgate.testing.TestGateway.withTestGateway;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpStatus;

import dev.gihwan.tollgate.gateway.Upstream;

class TestGatewayTest {

    @Test
    void port() {
        try (TestGateway gateway = withTestGateway(builder -> {
            builder.server(serverBuilder -> serverBuilder.http(0)
                                                         .https(0).tlsSelfSigned());
            builder.upstream("/foo", Upstream.of("http://127.0.0.1"));
        })) {
            assertThat(gateway.httpPort()).isPositive();
            assertThat(gateway.httpsPort()).isPositive();
        }
    }

    @Test
    void uri() {
        try (TestGateway gateway = withTestGateway(builder -> {
            builder.server(serverBuilder -> serverBuilder.http(0)
                                                         .https(0).tlsSelfSigned());
            builder.upstream("/foo", Upstream.of("http://127.0.0.1"));
        })) {
            assertThat(gateway.httpUri()).isNotNull();
            assertThat(gateway.httpsUri()).isNotNull();
        }
    }

    @Test
    void healthCheck() {
        try (TestGateway gateway = withTestGateway(builder -> {
            builder.healthCheck("/health");
        })) {
            final WebClient webClient = WebClient.of(gateway.httpUri());
            final AggregatedHttpResponse res = webClient.get("/health").aggregate().join();
            assertThat(res.status()).isEqualTo(HttpStatus.OK);
        }
    }
}
