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

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;

/**
 * The default implementation of {@link Upstream}.
 */
final class DefaultUpstream implements Upstream {

    private final WebClient client;

    private final Function<HttpRequest, HttpRequest> requestFunction;
    private final Function<HttpResponse, HttpResponse> responseFunction;

    DefaultUpstream(WebClient client,
                    Function<HttpRequest, HttpRequest> requestFunction,
                    Function<HttpResponse, HttpResponse> responseFunction) {
        this.client = client;
        this.requestFunction = requestFunction;
        this.responseFunction = responseFunction;
    }

    @Override
    public HttpResponse execute(HttpRequest req) {
        final HttpRequest newReq = requestFunction.apply(req);
        requireNonNull(newReq, "transformed request should not be null");

        final HttpResponse res = client.execute(newReq);
        final HttpResponse newRes = responseFunction.apply(res);
        requireNonNull(newRes, "transformed response should not be null");
        return newRes;
    }
}
