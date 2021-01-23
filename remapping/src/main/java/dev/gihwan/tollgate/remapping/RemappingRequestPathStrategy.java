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

package dev.gihwan.tollgate.remapping;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.RequestHeaders;
import com.linecorp.armeria.server.ServiceRequestContext;

class RemappingRequestPathStrategy implements RemappingRequestStrategy {

    private static final String PATH_SEPARATOR = "/";
    private static final Splitter PATH_SPLITTER = Splitter.on(PATH_SEPARATOR);

    private final String pathPattern;

    RemappingRequestPathStrategy(String pathPattern) {
        this.pathPattern = pathPattern;
    }

    @Override
    public HttpRequest remap(ServiceRequestContext ctx, HttpRequest req) {
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
        final RequestHeaders headers = req.headers().toBuilder().path(remappedPath).build();
        return req.withHeaders(headers);
    }
}
