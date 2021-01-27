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

package dev.gihwan.tollgate.hocon;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.testing.junit5.server.ServerExtension;

import dev.gihwan.tollgate.gateway.Gateway;

class HoconGatewayBuilderTest {

    @RegisterExtension
    static final ServerExtension serviceServer = new ServerExtension() {
        @Override
        protected void configure(ServerBuilder sb) {
            sb.service("/foo", (ctx, req) -> HttpResponse.of("bar"));
        }
    };

    @Test
    void build() {
        final Config config =
                ConfigFactory.load()
                             .withValue("tollgate.routing.foo.upstream.uri",
                                        ConfigValueFactory.fromAnyRef(serviceServer.httpUri().toString()));

        final Gateway gateway = HoconGatewayBuilder.of().build(config);
        try {
            gateway.start().join();
            assertThat(gateway.activeLocalPort()).isPositive();

            final WebClient client = WebClient.of("http://127.0.0.1:" + gateway.activeLocalPort());

            AggregatedHttpResponse res = client.get("/health").aggregate().join();
            assertThat(res.status()).isEqualTo(HttpStatus.OK);

            res = client.get("/foo").aggregate().join();
            assertThat(res.status()).isEqualTo(HttpStatus.OK);
            assertThat(res.contentUtf8()).isEqualTo("bar");
        } finally {
            gateway.stop().join();
        }
    }
}
