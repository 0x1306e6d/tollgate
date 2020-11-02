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

package dev.gihwan.tollgate.example.pokeapi.berry;

import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpHeaders;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.ResponseHeaders;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.file.AggregatedHttpFile;
import com.linecorp.armeria.server.file.HttpFile;
import com.linecorp.armeria.server.healthcheck.HealthCheckService;
import com.linecorp.armeria.server.healthcheck.SettableHealthChecker;
import com.linecorp.armeria.server.logging.LoggingService;

public final class BerryService {

    private static final Logger logger = LoggerFactory.getLogger(BerryService.class);

    public static void main(String[] args) {
        final HttpHeaders headers = ResponseHeaders.builder(HttpStatus.OK)
                                                   .contentType(MediaType.JSON)
                                                   .build();

        final HttpData berry = HttpFile.of(BerryService.class.getClassLoader(), "berry.json")
                                       .aggregate(Executors.newSingleThreadExecutor())
                                       .thenApply(AggregatedHttpFile::content)
                                       .join();
        final HttpData berryFirmness = HttpFile.of(BerryService.class.getClassLoader(), "berry-firmness.json")
                                               .aggregate(Executors.newSingleThreadExecutor())
                                               .thenApply(AggregatedHttpFile::content)
                                               .join();
        final HttpData berryFlavor = HttpFile.of(BerryService.class.getClassLoader(), "berry-flavor.json")
                                             .aggregate(Executors.newSingleThreadExecutor())
                                             .thenApply(AggregatedHttpFile::content)
                                             .join();

        final Server server = Server.builder()
                                    .http(8080)
                                    .service("/health", HealthCheckService.of(new SettableHealthChecker()))
                                    .service("/berry/1", (ctx, req) -> HttpResponse.of(headers, berry))
                                    .service("/berry-firmness/1",
                                             (ctx, req) -> HttpResponse.of(headers, berryFirmness))
                                    .service("/berry-flavor/1",
                                             (ctx, req) -> HttpResponse.of(headers, berryFlavor))
                                    .decorator(LoggingService.newDecorator())
                                    .build();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop().join();
            logger.info("Stopped berry service.");
        }));

        server.start().join();
        logger.info("Started berry service at {}.", server.activePort());
    }
}
