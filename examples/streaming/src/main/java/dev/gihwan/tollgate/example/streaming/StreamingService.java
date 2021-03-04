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

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpHeaderNames;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpResponseWriter;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.ResponseHeaders;
import com.linecorp.armeria.common.ServerCacheControl;
import com.linecorp.armeria.common.logging.LogLevel;
import com.linecorp.armeria.server.AbstractHttpService;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.file.HttpFile;
import com.linecorp.armeria.server.logging.LoggingService;

import io.netty.channel.EventLoop;

/**
 * Represents a backend server with the `AnimationService` of the Armeria's proxy-server example.
 *
 * @see <a href="https://github.com/line/armeria/blob/master/examples/proxy-server/src/main/java/example/armeria/proxy/AnimationService.java">AnimationService</a>
 */
public final class StreamingService {

    private static final Logger logger = LoggerFactory.getLogger(StreamingService.class);

    public static void main(String[] args) {
        final Server server = Server.builder()
                                    .http(9090)
                                    // Disable timeout to serve infinite streaming response.
                                    .requestTimeoutMillis(0)
                                    .service("/",
                                             HttpFile.builder(StreamingService.class.getClassLoader(),
                                                              "index.html")
                                                     .cacheControl(ServerCacheControl.REVALIDATED)
                                                     .build()
                                                     .asService())
                                    .service("/animation", new AnimationService())
                                    .decorator(LoggingService.builder()
                                                             .logger(logger)
                                                             .requestLogLevel(LogLevel.INFO)
                                                             .successfulResponseLogLevel(LogLevel.INFO)
                                                             .failureResponseLogLevel(LogLevel.WARN)
                                                             .newDecorator())
                                    .build();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop().join();
            logger.info("Stopped streaming service.");
        }));

        server.start().join();
        logger.info("Started streaming service at {}.", server.activePort());
    }
}

/**
 * @see <a href="https://github.com/line/armeria/blob/master/examples/proxy-server/src/main/java/example/armeria/proxy/AnimationService.java">AnimationService</a>
 */
final class AnimationService extends AbstractHttpService {

    private static final List<String> frames = List.of("<pre>" +
                                                       "╔════╤╤╤╤════╗\n" +
                                                       "║    │││ \\   ║\n" +
                                                       "║    │││  O  ║\n" +
                                                       "║    OOO     ║" +
                                                       "</pre>",
                                                       "<pre>" +
                                                       "╔════╤╤╤╤════╗\n" +
                                                       "║    ││││    ║\n" +
                                                       "║    ││││    ║\n" +
                                                       "║    OOOO    ║" +
                                                       "</pre>",
                                                       "<pre>" +
                                                       "╔════╤╤╤╤════╗\n" +
                                                       "║   / │││    ║\n" +
                                                       "║  O  │││    ║\n" +
                                                       "║     OOO    ║" +
                                                       "</pre>",
                                                       "<pre>" +
                                                       "╔════╤╤╤╤════╗\n" +
                                                       "║    ││││    ║\n" +
                                                       "║    ││││    ║\n" +
                                                       "║    OOOO    ║" +
                                                       "</pre>");
    private static final int frameIntervalMillis = 500;

    @Override
    protected HttpResponse doGet(ServiceRequestContext ctx, HttpRequest req) {
        final HttpResponseWriter res = HttpResponse.streaming();
        res.write(ResponseHeaders.of(HttpStatus.OK,
                                     HttpHeaderNames.CONTENT_TYPE, MediaType.PLAIN_TEXT_UTF_8));
        res.whenConsumed().thenRun(() -> streamData(ctx.eventLoop(), res, 0));
        return res;
    }

    private void streamData(EventLoop executor, HttpResponseWriter writer, int frameIndex) {
        final int index = frameIndex % frames.size();
        writer.write(HttpData.ofUtf8(frames.get(index)));
        writer.whenConsumed().thenRun(() -> executor.schedule(() -> streamData(executor, writer, index + 1),
                                                              frameIntervalMillis, TimeUnit.MILLISECONDS));
    }
}
