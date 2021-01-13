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

package dev.gihwan.tollgate.example.helloworld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.logging.LogLevel;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.logging.LoggingService;

/**
 * Represents a microservice.
 *
 * +-------------+       +-------------------+       +-------------------+
 * | HTTP Client | <---> | HelloWorldGateway | <---> | HelloWorldService |
 * +-------------+       +-------------------+       +-------------------+
 */
public final class HelloWorldService {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldService.class);

    public static void main(String[] args) {
        final Server server = Server.builder()
                                    .http(9090)
                                    .service("/helloworld", (ctx, req) -> HttpResponse.of("Hello, World!"))
                                    .decorator(LoggingService.builder()
                                                             .logger(logger)
                                                             .requestLogLevel(LogLevel.INFO)
                                                             .successfulResponseLogLevel(LogLevel.INFO)
                                                             .failureResponseLogLevel(LogLevel.INFO)
                                                             .newDecorator())
                                    .build();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop().join();
            logger.info("Stopped helloworld service.");
        }));

        server.start().join();
        logger.info("Started helloworld service at {}.", server.activePort());
    }
}
