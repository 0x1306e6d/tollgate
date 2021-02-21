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
import java.util.function.Predicate;

import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceBindingBuilder;

public final class UpstreamBindingBuilder {

    private final GatewayBuilder gatewayBuilder;
    private final ServiceBindingBuilder serviceBindingBuilder;

    UpstreamBindingBuilder(GatewayBuilder gatewayBuilder, ServiceBindingBuilder serviceBindingBuilder) {
        this.gatewayBuilder = gatewayBuilder;
        this.serviceBindingBuilder = serviceBindingBuilder;
    }

    /**
     * @see ServiceBindingBuilder#path(String)
     */
    public UpstreamBindingBuilder path(String pathPattern) {
        serviceBindingBuilder.path(requireNonNull(pathPattern, "pathPattern"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#pathPrefix(String)
     */
    public UpstreamBindingBuilder pathPrefix(String prefix) {
        serviceBindingBuilder.pathPrefix(requireNonNull(prefix, "prefix"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#methods(HttpMethod...)
     */
    public UpstreamBindingBuilder methods(HttpMethod... methods) {
        serviceBindingBuilder.methods(requireNonNull(methods, "methods"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#methods(Iterable)
     */
    public UpstreamBindingBuilder methods(Iterable<HttpMethod> methods) {
        serviceBindingBuilder.methods(requireNonNull(methods, "methods"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#get(String)
     */
    public UpstreamBindingBuilder get(String pathPattern) {
        serviceBindingBuilder.get(requireNonNull(pathPattern, "pathPattern"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#post(String)
     */
    public UpstreamBindingBuilder post(String pathPattern) {
        serviceBindingBuilder.post(requireNonNull(pathPattern, "pathPattern"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#put(String)
     */
    public UpstreamBindingBuilder put(String pathPattern) {
        serviceBindingBuilder.put(requireNonNull(pathPattern, "pathPattern"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#patch(String)
     */
    public UpstreamBindingBuilder patch(String pathPattern) {
        serviceBindingBuilder.patch(requireNonNull(pathPattern, "pathPattern"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#delete(String)
     */
    public UpstreamBindingBuilder delete(String pathPattern) {
        serviceBindingBuilder.delete(requireNonNull(pathPattern, "pathPattern"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#options(String)
     */
    public UpstreamBindingBuilder options(String pathPattern) {
        serviceBindingBuilder.options(requireNonNull(pathPattern, "pathPattern"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#head(String)
     */
    public UpstreamBindingBuilder head(String pathPattern) {
        serviceBindingBuilder.head(requireNonNull(pathPattern, "pathPattern"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#trace(String)
     */
    public UpstreamBindingBuilder trace(String pathPattern) {
        serviceBindingBuilder.trace(requireNonNull(pathPattern, "pathPattern"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#connect(String)
     */
    public UpstreamBindingBuilder connect(String pathPattern) {
        serviceBindingBuilder.connect(requireNonNull(pathPattern, "pathPattern"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#consumes(MediaType...)
     */
    public UpstreamBindingBuilder consumes(MediaType... consumeTypes) {
        serviceBindingBuilder.consumes(requireNonNull(consumeTypes, "consumeTypes"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#consumes(Iterable)
     */
    public UpstreamBindingBuilder consumes(Iterable<MediaType> consumeTypes) {
        serviceBindingBuilder.consumes(requireNonNull(consumeTypes, "consumeTypes"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#produces(MediaType...)
     */
    public UpstreamBindingBuilder produces(MediaType... produceTypes) {
        serviceBindingBuilder.produces(requireNonNull(produceTypes, "produceTypes"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#produces(Iterable)
     */
    public UpstreamBindingBuilder produces(Iterable<MediaType> produceTypes) {
        serviceBindingBuilder.produces(requireNonNull(produceTypes, "produceTypes"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#matchesParams(String...)
     */
    public UpstreamBindingBuilder matchesParams(String... paramPredicates) {
        serviceBindingBuilder.matchesParams(requireNonNull(paramPredicates, "paramPredicates"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#matchesParams(Iterable)
     */
    public UpstreamBindingBuilder matchesParams(Iterable<String> paramPredicates) {
        serviceBindingBuilder.matchesParams(requireNonNull(paramPredicates, "paramPredicates"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#matchesParams(String, Predicate)
     */
    public UpstreamBindingBuilder matchesParams(String paramName,
                                                Predicate<? super String> valuePredicate) {
        serviceBindingBuilder.matchesParams(requireNonNull(paramName, "paramName"),
                                            requireNonNull(valuePredicate, "valuePredicate"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#matchesHeaders(String...)
     */
    public UpstreamBindingBuilder matchesHeaders(String... headerPredicates) {
        serviceBindingBuilder.matchesHeaders(requireNonNull(headerPredicates, "headerPredicates"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#matchesHeaders(Iterable)
     */
    public UpstreamBindingBuilder matchesHeaders(Iterable<String> headerPredicates) {
        serviceBindingBuilder.matchesHeaders(requireNonNull(headerPredicates, "headerPredicates"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#matchesHeaders(CharSequence, Predicate)
     */
    public UpstreamBindingBuilder matchesHeaders(CharSequence headerName,
                                                 Predicate<? super String> valuePredicate) {
        serviceBindingBuilder.matchesHeaders(requireNonNull(headerName, "headerName"),
                                             requireNonNull(valuePredicate, "valuePredicate"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#decorator(Function)
     */
    public UpstreamBindingBuilder decorator(Function<? super HttpService, ? extends HttpService> decorator) {
        serviceBindingBuilder.decorator(requireNonNull(decorator, "decorator"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#decorators(Function[])
     */
    @SafeVarargs
    public final UpstreamBindingBuilder decorators(
            Function<? super HttpService, ? extends HttpService>... decorators) {
        serviceBindingBuilder.decorators(requireNonNull(decorators, "decorators"));
        return this;
    }

    /**
     * @see ServiceBindingBuilder#decorators(Iterable)
     */
    public UpstreamBindingBuilder decorators(
            Iterable<? extends Function<? super HttpService, ? extends HttpService>> decorators) {
        serviceBindingBuilder.decorators(requireNonNull(decorators, "decorators"));
        return this;
    }

    public final GatewayBuilder build(Upstream upstream) {
        serviceBindingBuilder.build(new UpstreamHttpService(requireNonNull(upstream, "upstream")));
        return gatewayBuilder;
    }
}
