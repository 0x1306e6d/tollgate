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

package dev.gihwan.tollgate.core.upstream;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;

import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.RequestHeaders;
import com.linecorp.armeria.server.ServiceRequestContext;

import dev.gihwan.tollgate.core.service.Service;
import dev.gihwan.tollgate.core.service.ServiceFactory;

public final class DefaultUpstream implements Upstream {

    private static final Logger logger = LoggerFactory.getLogger(DefaultUpstream.class);

    private static final String PATH_SEPARATOR = "/";
    private static final Splitter PATH_SPLITTER = Splitter.on(PATH_SEPARATOR);

    private final UpstreamConfig config;
    private final Service service;

    DefaultUpstream(UpstreamConfig config) {
        this.config = config;
        service = ServiceFactory.instance().get(config.service());
    }

    @Override
    public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) {
        final String path = buildPath(ctx);
        if (path == null) {
            return HttpResponse.of(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return HttpResponse.from(req.aggregate()
                                    .thenApply(aggregated -> buildRequest(aggregated, path))
                                    .thenApply(service::send));
    }

    @Nullable
    private String buildPath(ServiceRequestContext ctx) {
        final StringBuilder sb = new StringBuilder();
        for (String token : PATH_SPLITTER.split(config.endpoint().path())) {
            sb.append(PATH_SEPARATOR);
            if (token.startsWith("{") && token.endsWith("}")) {
                final String pathParamName = token.substring(1, token.length() - 1);
                final String pathParamValue = ctx.pathParam(pathParamName);
                if (pathParamValue == null) {
                    logger.error("Path parameter {} is not found on path {}.", pathParamName, ctx.path());
                    return null;
                }
                sb.append(pathParamValue);
            } else {
                sb.append(token);
            }
        }
        return sb.toString();
    }

    private HttpRequest buildRequest(AggregatedHttpRequest req, String path) {
        final RequestHeaders headers = req.headers().toBuilder()
                                          .method(config.endpoint().method())
                                          .path(path)
                                          .build();
        return HttpRequest.of(headers, req.content(), req.trailers());
    }
}