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

import dev.gihwan.tollgate.gateway.Gateway;
import dev.gihwan.tollgate.gateway.Upstream;

/**
 * Represents a microservice.
 *
 * +-------------+       +-------------------+       +-------------------+
 * | HTTP Client | <---> | HelloWorldGateway | <---> | HelloWorldService |
 * +-------------+       +-------------------+       +-------------------+
 */
public final class HelloWorldGateway {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldGateway.class);

    public static void main(String[] args) {
        final Gateway gateway = Gateway.builder()
                                       .http(8080)
                                       .upstream("/helloworld", Upstream.of("http://localhost:9090"))
                                       .build();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            gateway.stop();
            logger.info("Stopped helloworld gateway.");
        }));

        gateway.start();
        logger.info("Started helloworld gateway at {}.", gateway.activePort());
    }
}
