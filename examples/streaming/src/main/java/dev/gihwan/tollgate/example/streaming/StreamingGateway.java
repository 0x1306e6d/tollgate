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

package dev.gihwan.tollgate.example.streaming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.common.logging.LogLevel;
import com.linecorp.armeria.server.logging.LoggingService;

import dev.gihwan.tollgate.gateway.Gateway;
import dev.gihwan.tollgate.gateway.Upstream;

/**
 * Represents a proxy server with the `ProxyServer` of the Armeria's proxy-server example.
 *
 * @see <a href="https://github.com/line/armeria/blob/master/examples/proxy-server/src/main/java/example/armeria/proxy/ProxyService.java">ProxyService</a>
 */
public final class StreamingGateway {

    private static final Logger logger = LoggerFactory.getLogger(StreamingGateway.class);

    public static void main(String[] args) {
        final Gateway gateway =
                Gateway.builder()
                       .server(builder -> builder.http(8080)
                                                 // Disable timeout to serve infinite streaming response.
                                                 .requestTimeoutMillis(0))
                       .route()
                       .path("/").path("/animation")
                       .decorator(LoggingService.builder()
                                                .requestLogLevel(LogLevel.INFO)
                                                .successfulResponseLogLevel(LogLevel.INFO)
                                                .failureResponseLogLevel(LogLevel.WARN)
                                                .newDecorator())
                       .build(Upstream.builder("http://localhost:9090")
                                      // Disable timeout to serve infinite streaming response.
                                      .client(builder -> builder.responseTimeoutMillis(0))
                                      .decorator(LoggingClient.builder()
                                                              .requestLogLevel(LogLevel.INFO)
                                                              .successfulResponseLogLevel(LogLevel.INFO)
                                                              .failureResponseLogLevel(LogLevel.WARN)
                                                              .newDecorator())
                                      .build())
                       .build();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            gateway.stop().join();
            logger.info("Stopped streaming gateway.");
        }));

        gateway.start().join();
        logger.info("Started streaming gateway at {}.", gateway.activePort());
    }
}
