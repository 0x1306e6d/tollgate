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

package dev.gihwan.lith.example.pokeapi.machine;

import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.file.AggregatedHttpFile;
import com.linecorp.armeria.server.file.HttpFile;
import com.linecorp.armeria.server.healthcheck.HealthCheckService;
import com.linecorp.armeria.server.healthcheck.SettableHealthChecker;
import com.linecorp.armeria.server.logging.LoggingService;

public final class MachineService {

    private static final Logger logger = LoggerFactory.getLogger(MachineService.class);

    public static void main(String[] args) {
        final HttpData data = HttpFile.of(MachineService.class.getClassLoader(), "machine.json")
                                      .aggregate(Executors.newSingleThreadExecutor())
                                      .thenApply(AggregatedHttpFile::content)
                                      .join();
        final Server server = Server.builder()
                                    .http(8080)
                                    .service("/health", HealthCheckService.of(new SettableHealthChecker()))
                                    .service("/machine/1",
                                             (ctx, req) -> HttpResponse.of(HttpStatus.OK, MediaType.JSON, data))
                                    .decorator(LoggingService.newDecorator())
                                    .build();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop().join();
            logger.info("Stopped machine service.");
        }));

        server.start().join();
        logger.info("Started machine service at {}.", server.activePort());
    }
}
