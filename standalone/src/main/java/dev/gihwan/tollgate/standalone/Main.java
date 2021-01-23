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

package dev.gihwan.tollgate.standalone;

import java.time.Duration;
import java.time.Instant;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import dev.gihwan.tollgate.gateway.Gateway;
import dev.gihwan.tollgate.hocon.HoconGatewayBuilder;

public final class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Nullable
    private Gateway gateway;
    @Nullable
    private Instant startAt;

    private void start() {
        startAt = Instant.now();
        logger.info("Starting the Tollgate standalone at {}.", startAt);

        final Config config = ConfigFactory.load();
        logger.debug("Loaded configuration: {}", config);

        gateway = HoconGatewayBuilder.of().build(config);
        gateway.start();

        assert startAt != null;
        logger.info("The Tollgate standalone is started. ({} used to start)",
                    Duration.between(startAt, Instant.now()));
    }

    private void stop() {
        final Instant stopAt = Instant.now();
        logger.info("Stopping the Tollgate standalone at {}.", stopAt);

        if (gateway != null) {
            gateway.stop();
        }

        assert startAt != null;
        logger.info("The Tollgate standalone has served during {}.", Duration.between(startAt, stopAt));
        logger.info("The Tollgate standalone is stopped. ({} used to stop)",
                    Duration.between(startAt, Instant.now()));
    }

    public static void main(String[] args) {
        final Main main = new Main();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                main.stop();
            } catch (Exception e) {
                logger.error("Failed to stop the Tollgate standalone.", e);
            }
        }));

        try {
            main.start();
        } catch (Exception e) {
            logger.error("Failed to start the Tollgate standalone.", e);
            System.exit(-1);
        }
    }
}
