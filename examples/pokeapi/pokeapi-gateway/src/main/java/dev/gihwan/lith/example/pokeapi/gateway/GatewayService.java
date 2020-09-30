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

package dev.gihwan.lith.example.pokeapi.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.common.HttpMethod;

import dev.gihwan.lith.core.Lith;
import dev.gihwan.lith.core.route.Endpoint;
import dev.gihwan.lith.core.route.Service;

public final class GatewayService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayService.class);

    public static void main(String[] args) {
        final Lith lith = Lith.builder()
                              .port(8080)
                              .endpoint(Endpoint.builder()
                                                .method(HttpMethod.GET)
                                                .path("/api/v2/berry/{idOrName}")
                                                .service(Service.builder()
                                                                .uri("http://berry:9080")
                                                                .endpoint("/berry/{idOrName}")
                                                                .build())
                                                .build())
                              .endpoint(Endpoint.builder()
                                                .method(HttpMethod.GET)
                                                .path("/api/v2/contest-type/{idOrName}")
                                                .service(Service.builder()
                                                                .uri("http://contest:9180")
                                                                .endpoint("/contest-type/{idOrName}")
                                                                .build())
                                                .build())
                              .endpoint(Endpoint.builder()
                                                .method(HttpMethod.GET)
                                                .path("/api/v2/encounter-method/{idOrName}")
                                                .service(Service.builder()
                                                                .uri("http://encounter:9280")
                                                                .endpoint("/encounter-method/{idOrName}")
                                                                .build())
                                                .build())
                              .endpoint(Endpoint.builder()
                                                .method(HttpMethod.GET)
                                                .path("/api/v2/evolution-chain/{idOrName}")
                                                .service(Service.builder()
                                                                .uri("http://evolution:9380")
                                                                .endpoint("/evolution-chain/{idOrName}")
                                                                .build())
                                                .build())
                              .endpoint(Endpoint.builder()
                                                .method(HttpMethod.GET)
                                                .path("/api/v2/generation/{idOrName}")
                                                .service(Service.builder()
                                                                .uri("http://game:9480")
                                                                .endpoint("/generation/{idOrName}")
                                                                .build())
                                                .build())
                              .endpoint(Endpoint.builder()
                                                .method(HttpMethod.GET)
                                                .path("/api/v2/item/{idOrName}")
                                                .service(Service.builder()
                                                                .uri("http://item:9580")
                                                                .endpoint("/item/{idOrName}")
                                                                .build())
                                                .build())
                              .endpoint(Endpoint.builder()
                                                .method(HttpMethod.GET)
                                                .path("/api/v2/location/{idOrName}")
                                                .service(Service.builder()
                                                                .uri("http://location:9680")
                                                                .endpoint("/location/{idOrName}")
                                                                .build())
                                                .build())
                              .endpoint(Endpoint.builder()
                                                .method(HttpMethod.GET)
                                                .path("/api/v2/machine/{idOrName}")
                                                .service(Service.builder()
                                                                .uri("http://machine:9780")
                                                                .endpoint("/machine/{idOrName}")
                                                                .build())
                                                .build())
                              .endpoint(Endpoint.builder()
                                                .method(HttpMethod.GET)
                                                .path("/api/v2/move/{idOrName}")
                                                .service(Service.builder()
                                                                .uri("http://move:9880")
                                                                .endpoint("/move/{idOrName}")
                                                                .build())
                                                .build())
                              .endpoint(Endpoint.builder()
                                                .method(HttpMethod.GET)
                                                .path("/api/v2/ability/{idOrName}")
                                                .service(Service.builder()
                                                                .uri("http://pokemon:9980")
                                                                .endpoint("/ability/{idOrName}")
                                                                .build())
                                                .build())
                              .build();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            lith.stop();
            logger.info("Stopped gateway service.");
        }));

        lith.start();
        logger.info("Started gateway service.");
    }
}
