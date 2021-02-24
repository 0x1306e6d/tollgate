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

package dev.gihwan.tollgate.exception.mapping;

import static dev.gihwan.tollgate.testing.TestGateway.withTestGateway;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.jupiter.api.Test;

import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpStatus;

import dev.gihwan.tollgate.gateway.Upstream;
import dev.gihwan.tollgate.testing.TestGateway;

class ExceptionMappingClientTest {

    @Test
    void mapException() {
        try (TestGateway gateway = withTestGateway(builder -> {
            final int unusedPort;
            try (ServerSocket ss = new ServerSocket(0)) {
                unusedPort = ss.getLocalPort();
            } catch (IOException e) {
                fail(e.getMessage());
                return;
            }

            builder.upstream("/refused", Upstream.builder("http", Endpoint.of("127.0.0.1", unusedPort))
                                                 .decorator(ExceptionMappingClient.newDecorator())
                                                 .build());
        })) {
            final WebClient webClient = WebClient.builder(gateway.httpUri()).build();

            final AggregatedHttpResponse res = webClient.get("/refused").aggregate().join();
            assertThat(res.status()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
