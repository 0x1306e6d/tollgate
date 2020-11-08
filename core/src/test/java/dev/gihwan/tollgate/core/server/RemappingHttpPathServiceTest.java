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

package dev.gihwan.tollgate.core.server;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.extension.RegisterExtension;

import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.testing.junit5.server.ServerExtension;

import dev.gihwan.tollgate.core.client.ServiceConfig;
import dev.gihwan.tollgate.core.client.UpstreamClient;

class RemappingHttpPathServiceTest {

    private static final AtomicReference<AggregatedHttpRequest> reqCapture = new AtomicReference<>();

    @RegisterExtension
    static final ServerExtension serviceServer = new ServerExtension() {
        @Override
        protected void configure(ServerBuilder sb) {
            sb.service("/bar",
                       (ctx, req) -> HttpResponse.from(req.aggregate().thenApply(aggregated -> {
                           reqCapture.set(aggregated);
                           return HttpResponse.of("baz");
                       })));
        }
    };

    @RegisterExtension
    static final ServerExtension tollgateServer = new ServerExtension() {
        @Override
        protected void configure(ServerBuilder sb) {
            final ServiceConfig serviceConfig = ServiceConfig.of(serviceServer.httpUri().toString());
            final UpstreamClient client = UpstreamClient.of(serviceConfig);

            sb.service("/foo", new DefaultUpstreamService(client)
                    .decorate(RemappingRequestHeadersService.newDecorator("/bar")));
        }
    };

}
