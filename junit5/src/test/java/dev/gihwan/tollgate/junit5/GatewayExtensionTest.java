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

package dev.gihwan.tollgate.junit5;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpStatus;

import dev.gihwan.tollgate.gateway.GatewayBuilder;

class GatewayExtensionTest {

    @RegisterExtension
    static GatewayExtension gateway = new GatewayExtension() {
        @Override
        public void configure(GatewayBuilder builder) {
            builder.server(serverBuilder -> serverBuilder.http(0)
                                                         .https(0).tlsSelfSigned());
            builder.healthCheck("/health");
        }
    };

    @Test
    void port() {
        assertThat(gateway.httpPort()).isPositive();
        assertThat(gateway.httpsPort()).isPositive();
    }

    @Test
    void uri() {
        assertThat(gateway.httpUri()).isNotNull();
        assertThat(gateway.httpsUri()).isNotNull();
    }

    @Test
    void healthCheck() {
        final WebClient webClient = WebClient.of(gateway.httpUri());
        final AggregatedHttpResponse res = webClient.get("/health").aggregate().join();
        assertThat(res.status()).isEqualTo(HttpStatus.OK);
    }
}
