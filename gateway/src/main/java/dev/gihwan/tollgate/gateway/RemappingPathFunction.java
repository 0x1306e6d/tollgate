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

package dev.gihwan.tollgate.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.google.common.base.Splitter;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.RequestHeaders;
import com.linecorp.armeria.server.ServiceRequestContext;

final class RemappingPathFunction implements Function<HttpRequest, HttpRequest> {

    private static final String PATH_SEPARATOR = "/";
    private static final Splitter PATH_SPLITTER = Splitter.on(PATH_SEPARATOR);

    private final String pathPattern;
    private final boolean hasPathParams;

    RemappingPathFunction(String pathPattern) {
        this.pathPattern = pathPattern;
        this.hasPathParams = hasPathParams(pathPattern);
    }

    private static boolean hasPathParams(String pathPattern) {
        for (String segment : PATH_SPLITTER.split(pathPattern)) {
            if (segment.startsWith("{") && segment.endsWith("}")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HttpRequest apply(HttpRequest req) {
        if (hasPathParams) {
            final ServiceRequestContext ctx = ServiceRequestContext.current();
            return req.mapHeaders(headers -> remapPathWithPathParams(ctx, headers));
        } else {
            return req.mapHeaders(this::remapPath);
        }
    }

    private RequestHeaders remapPathWithPathParams(ServiceRequestContext ctx, RequestHeaders headers) {
        final List<String> segments = new ArrayList<>();
        for (String segment : PATH_SPLITTER.split(pathPattern)) {
            if (segment.startsWith("{") && segment.endsWith("}")) {
                final String pathParamName = segment.substring(1, segment.length() - 1);
                final String pathParamValue = ctx.pathParam(pathParamName);
                if (pathParamValue == null) {
                    throw new IllegalStateException("pathParam " + pathParamName + " does not exist");
                }
                segments.add(pathParamValue);
            } else {
                segments.add(segment);
            }
        }

        final String remappedPath = String.join(PATH_SEPARATOR, segments);
        return headers.toBuilder()
                      .path(remappedPath)
                      .build();
    }

    private RequestHeaders remapPath(RequestHeaders headers) {
        return headers.toBuilder()
                      .path(pathPattern)
                      .build();
    }
}
