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

package dev.gihwan.lith.server;

import dev.gihwan.lith.core.Lith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public final class LithServer {

    private static final Logger logger = LoggerFactory.getLogger(LithServer.class);

    private Lith lith;
    private Instant startAt;

    private void start() {
        startAt = Instant.now();
        logger.info("Starting the Lith server at {}.", startAt);

        lith = Lith.builder()
                .port(8080)
                .build();
        lith.start();

        logger.info("The Lith server is started. ({} used to start)", Duration.between(startAt, Instant.now()));
    }

    private void stop() {
        final Instant stopAt = Instant.now();
        logger.info("Stopping the Lith server at {}.", stopAt);

        lith.stop();

        logger.info("The Lith server has served during {}.", Duration.between(startAt, stopAt));
        logger.info("The Lith server is stopped. ({} used to stop)", Duration.between(startAt, Instant.now()));
    }

    public static void main(String[] args) {
        final LithServer server = new LithServer();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.stop();
            } catch (Exception e) {
                logger.error("Failed to stop the Lith server.", e);
            }
        }));

        try {
            server.start();
        } catch (Exception e) {
            logger.error("Failed to start the Lith server.", e);
            System.exit(-1);
        }
    }
}
