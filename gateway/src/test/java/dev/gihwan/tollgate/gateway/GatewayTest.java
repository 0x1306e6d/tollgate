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
import org.junit.jupiter.api.extension.RegisterExtension;

import com.linecorp.armeria.client.ClientFactory;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.testing.junit5.server.ServerExtension;

class GatewayTest {

    @RegisterExtension
    static final ServerExtension serviceServer = new ServerExtension() {
        @Override
        protected void configure(ServerBuilder sb) {
            sb.service("/foo", (ctx, req) -> HttpResponse.of("Hello, World!"));
        }
    };

    @Test
    void http() {
        final Gateway gateway = Gateway.builder()
                                       .server(builder -> builder.http(0))
                                       .upstream("/foo", Upstream.of(serviceServer.httpUri()))
                                       .build();
        gateway.start().join();

        assertThat(gateway.activeLocalPort(SessionProtocol.HTTP)).isPositive();
        assertThatThrownBy(() -> gateway.activeLocalPort(SessionProtocol.HTTPS))
                .isInstanceOf(IllegalStateException.class);

        final WebClient client = WebClient.of("http://127.0.0.1:" + gateway.activeLocalPort());
        final AggregatedHttpResponse res = client.get("/foo").aggregate().join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);
        assertThat(res.contentUtf8()).isEqualTo("Hello, World!");
    }

    @Test
    void https() {
        final Gateway gateway = Gateway.builder()
                                       .server(builder -> builder.https(0).tlsSelfSigned())
                                       .upstream("/foo", Upstream.of(serviceServer.httpUri()))
                                       .build();
        gateway.start().join();

        assertThatThrownBy(() -> gateway.activeLocalPort(SessionProtocol.HTTP))
                .isInstanceOf(IllegalStateException.class);
        assertThat(gateway.activeLocalPort(SessionProtocol.HTTPS)).isPositive();

        final WebClient client = WebClient.builder("https://127.0.0.1:" + gateway.activeLocalPort())
                                          .factory(ClientFactory.insecure())
                                          .build();
        final AggregatedHttpResponse res = client.get("/foo").aggregate().join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);
        assertThat(res.contentUtf8()).isEqualTo("Hello, World!");
    }
}
