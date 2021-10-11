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

package dev.gihwan.tollgate.example.pokeapi.gateway;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.common.logging.LogLevel;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.logging.LoggingService;

import dev.gihwan.tollgate.gateway.Gateway;
import dev.gihwan.tollgate.gateway.GatewayBuilder;
import dev.gihwan.tollgate.gateway.Upstream;

public final class GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayService.class);

    public static void main(String[] args) {
        final GatewayBuilder builder = Gateway.builder();
        builder.server(GatewayService::configureServer);
        configureBerryUpstream(builder);
        configureContestUpstream(builder);
        configureEncounterUpstream(builder);
        configureEvolutionUpstream(builder);
        configureGameUpstream(builder);
        configureItemUpstream(builder);
        configureLocationUpstream(builder);
        configureMachineUpstream(builder);
        configureMoveUpstream(builder);
        configurePokemonUpstream(builder);
        final Gateway gateway = builder.build();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            gateway.stop().join();
            logger.info("Stopped gateway");
        }));

        gateway.start().join();
        logger.info("Started gateway at {}.", gateway.activePort());
    }

    private static void configureServer(ServerBuilder builder) {
        builder.http(8080);
    }

    private static void configureBerryUpstream(GatewayBuilder builder) {
        builder.route()
               .get("/api/v2/berry-firmness/{idOrName}")
               .decorator(loggingService("BerryLogger"))
               .build(Upstream.builder(SessionProtocol.HTTP, Endpoint.of("berry", 8080))
                              .path("/berry-firmness/{idOrName}")
                              .decorator(loggingClient("BerryLogger"))
                              .build());
        builder.route()
               .get("/api/v2/berry-flavor/{idOrName}")
               .decorator(loggingService("BerryLogger"))
               .build(Upstream.builder(SessionProtocol.HTTP, Endpoint.of("berry", 8080))
                              .path("/berry-flavor/{idOrName}")
                              .decorator(loggingClient("BerryLogger"))
                              .build());
    }

    private static void configureContestUpstream(GatewayBuilder builder) {
        builder.route()
               .get("/api/v2/contest-type/{idOrName}")
               .decorator(loggingService("ContestLogger"))
               .build(Upstream.builder("http://contest:8080")
                              .path("/contest-type/{idOrName}")
                              .decorator(loggingClient("ContestLogger"))
                              .build());
    }

    private static void configureEncounterUpstream(GatewayBuilder builder) {
        builder.route()
               .get("/api/v2/encounter-method/{idOrName}")
               .decorator(loggingService("EncounterLogger"))
               .build(Upstream.builder("http://encounter:8080")
                              .path("/encounter-method/{idOrName}")
                              .decorator(loggingClient("EncounterLogger"))
                              .build());
    }

    private static void configureEvolutionUpstream(GatewayBuilder builder) {
        builder.route()
               .get("/api/v2/evolution-chain/{idOrName}")
               .decorator(loggingService("EvolutionLogger"))
               .build(Upstream.builder("http://evolution:8080")
                              .path("/evolution-chain/{idOrName}")
                              .decorator(loggingClient("EvolutionLogger"))
                              .build());
    }

    private static void configureGameUpstream(GatewayBuilder builder) {
        builder.route()
               .get("/api/v2/generation/{idOrName}")
               .decorator(loggingService("GameLogger"))
               .build(Upstream.builder("http://game:8080")
                              .path("/generation/{idOrName}")
                              .decorator(loggingClient("GameLogger"))
                              .build());
    }

    private static void configureItemUpstream(GatewayBuilder builder) {
        builder.route()
               .get("/api/v2/item/{idOrName}")
               .decorator(loggingService("ItemLogger"))
               .build(Upstream.builder("http://item:8080")
                              .path("/item/{idOrName}")
                              .decorator(loggingClient("ItemLogger"))
                              .build());
    }

    private static void configureLocationUpstream(GatewayBuilder builder) {
        builder.route()
               .get("/api/v2/location/{idOrName}")
               .decorator(loggingService("LocationLogger"))
               .build(Upstream.builder("http://location:8080")
                              .path("/location/{idOrName}")
                              .decorator(loggingClient("LocationLogger"))
                              .build());
    }

    private static void configureMachineUpstream(GatewayBuilder builder) {
        builder.route()
               .get("/api/v2/machine/{idOrName}")
               .decorator(loggingService("MachineLogger"))
               .build(Upstream.builder("http://machine:8080")
                              .path("/machine/{idOrName}")
                              .decorator(loggingClient("MachineLogger"))
                              .build());
    }

    private static void configureMoveUpstream(GatewayBuilder builder) {
        builder.route()
               .get("/api/v2/move/{idOrName}")
               .decorator(loggingService("MoveLogger"))
               .build(Upstream.builder("http://move:8080")
                              .path("/move/{idOrName}")
                              .decorator(loggingClient("MoveLogger"))
                              .build());
    }

    private static void configurePokemonUpstream(GatewayBuilder builder) {
        builder.route()
               .get("/api/v2/ability/{idOrName}")
               .decorator(loggingService("PokemonLogger"))
               .build(Upstream.builder("http://pokemon:8080")
                              .path("/ability/{idOrName}")
                              .decorator(loggingClient("PokemonLogger"))
                              .build());
    }

    private static Function<? super HttpService, LoggingService> loggingService(String loggerName) {
        return LoggingService.builder()
                             .logger(loggerName)
                             .requestLogLevel(LogLevel.INFO)
                             .successfulResponseLogLevel(LogLevel.INFO)
                             .failureResponseLogLevel(LogLevel.WARN)
                             .newDecorator();
    }

    private static Function<? super HttpClient, LoggingClient> loggingClient(String loggerName) {
        return LoggingClient.builder()
                            .logger(loggerName)
                            .requestLogLevel(LogLevel.INFO)
                            .successfulResponseLogLevel(LogLevel.INFO)
                            .failureResponseLogLevel(LogLevel.WARN)
                            .newDecorator();
    }
}
