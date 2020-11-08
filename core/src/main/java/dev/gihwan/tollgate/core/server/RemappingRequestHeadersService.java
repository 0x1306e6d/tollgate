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

package dev.gihwan.tollgate.core.server;

import static dev.gihwan.tollgate.core.server.AttributeKeys.REQUEST_HEADERS;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.RequestHeaders;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.SimpleDecoratingHttpService;

public final class RemappingRequestHeadersService extends SimpleDecoratingHttpService {

    private static final Logger logger = LoggerFactory.getLogger(RemappingRequestHeadersService.class);

    private static final String PATH_SEPARATOR = "/";
    private static final Splitter PATH_SPLITTER = Splitter.on(PATH_SEPARATOR);

    public static Function<? super HttpService, RemappingRequestHeadersService>
    newDecorator(String pathPattern) {
        requireNonNull(pathPattern, "pathPattern");
        return delegate -> new RemappingRequestHeadersService(delegate, pathPattern);
    }

    private final String pathPattern;

    private RemappingRequestHeadersService(HttpService delegate, String pathPattern) {
        super(delegate);
        this.pathPattern = pathPattern;
    }

    @Override
    public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
        final List<String> segments = new ArrayList<>();
        for (String segment : PATH_SPLITTER.split(pathPattern)) {
            if (segment.startsWith("{") && segment.endsWith("}")) {
                final String pathParamName = segment.substring(1, segment.length() - 1);
                final String pathParamValue = ctx.pathParam(pathParamName);
                if (pathParamValue == null) {
                    logger.error("Path parameter {} is not found on path {}.", pathParamName, ctx.path());
                    return HttpResponse.of(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                segments.add(pathParamValue);
            } else {
                segments.add(segment);
            }
        }

        final String remappedPath = String.join(PATH_SEPARATOR, segments);
        final RequestHeaders requestHeaders = defaultIfNull(ctx.attr(REQUEST_HEADERS), req.headers());
        ctx.setAttr(REQUEST_HEADERS, requestHeaders.toBuilder().path(remappedPath).build());
        return unwrap().serve(ctx, req);
    }
}
