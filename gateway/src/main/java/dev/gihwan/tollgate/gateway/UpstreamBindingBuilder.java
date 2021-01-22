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

import java.util.function.Predicate;

import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.ServiceBindingBuilder;

public abstract class UpstreamBindingBuilder<T extends GatewayBuilder> {

    private final T gatewayBuilder;
    private final ServiceBindingBuilder serviceBindingBuilder;

    protected UpstreamBindingBuilder(T gatewayBuilder, ServiceBindingBuilder serviceBindingBuilder) {
        this.gatewayBuilder = gatewayBuilder;
        this.serviceBindingBuilder = serviceBindingBuilder;
    }

    /**
     * @see ServiceBindingBuilder#path(String)
     */
    public UpstreamBindingBuilder<T> path(String pathPattern) {
        serviceBindingBuilder.path(pathPattern);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#pathPrefix(String)
     */
    public UpstreamBindingBuilder<T> pathPrefix(String prefix) {
        serviceBindingBuilder.pathPrefix(prefix);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#methods(HttpMethod...)
     */
    public UpstreamBindingBuilder<T> methods(HttpMethod... methods) {
        serviceBindingBuilder.methods(methods);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#methods(Iterable)
     */
    public UpstreamBindingBuilder<T> methods(Iterable<HttpMethod> methods) {
        serviceBindingBuilder.methods(methods);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#get(String)
     */
    public UpstreamBindingBuilder<T> get(String pathPattern) {
        serviceBindingBuilder.get(pathPattern);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#post(String)
     */
    public UpstreamBindingBuilder<T> post(String pathPattern) {
        serviceBindingBuilder.post(pathPattern);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#put(String)
     */
    public UpstreamBindingBuilder<T> put(String pathPattern) {
        serviceBindingBuilder.put(pathPattern);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#patch(String)
     */
    public UpstreamBindingBuilder<T> patch(String pathPattern) {
        serviceBindingBuilder.patch(pathPattern);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#delete(String)
     */
    public UpstreamBindingBuilder<T> delete(String pathPattern) {
        serviceBindingBuilder.delete(pathPattern);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#options(String)
     */
    public UpstreamBindingBuilder<T> options(String pathPattern) {
        serviceBindingBuilder.options(pathPattern);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#head(String)
     */
    public UpstreamBindingBuilder<T> head(String pathPattern) {
        serviceBindingBuilder.head(pathPattern);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#trace(String)
     */
    public UpstreamBindingBuilder<T> trace(String pathPattern) {
        serviceBindingBuilder.trace(pathPattern);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#connect(String)
     */
    public UpstreamBindingBuilder<T> connect(String pathPattern) {
        serviceBindingBuilder.connect(pathPattern);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#consumes(MediaType...)
     */
    public UpstreamBindingBuilder<T> consumes(MediaType... consumeTypes) {
        serviceBindingBuilder.consumes(consumeTypes);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#consumes(Iterable)
     */
    public UpstreamBindingBuilder<T> consumes(Iterable<MediaType> consumeTypes) {
        serviceBindingBuilder.consumes(consumeTypes);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#produces(MediaType...)
     */
    public UpstreamBindingBuilder<T> produces(MediaType... produceTypes) {
        serviceBindingBuilder.produces(produceTypes);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#produces(Iterable)
     */
    public UpstreamBindingBuilder<T> produces(Iterable<MediaType> produceTypes) {
        serviceBindingBuilder.produces(produceTypes);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#matchesParams(String...)
     */
    public UpstreamBindingBuilder<T> matchesParams(String... paramPredicates) {
        serviceBindingBuilder.matchesParams(paramPredicates);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#matchesParams(Iterable)
     */
    public UpstreamBindingBuilder<T> matchesParams(Iterable<String> paramPredicates) {
        serviceBindingBuilder.matchesParams(paramPredicates);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#matchesParams(String, Predicate)
     */
    public UpstreamBindingBuilder<T> matchesParams(String paramName,
                                                   Predicate<? super String> valuePredicate) {
        serviceBindingBuilder.matchesParams(paramName, valuePredicate);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#matchesHeaders(String...)
     */
    public UpstreamBindingBuilder<T> matchesHeaders(String... headerPredicates) {
        serviceBindingBuilder.matchesHeaders(headerPredicates);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#matchesHeaders(Iterable)
     */
    public UpstreamBindingBuilder<T> matchesHeaders(Iterable<String> headerPredicates) {
        serviceBindingBuilder.matchesHeaders(headerPredicates);
        return this;
    }

    /**
     * @see ServiceBindingBuilder#matchesHeaders(CharSequence, Predicate)
     */
    public UpstreamBindingBuilder<T> matchesHeaders(CharSequence headerName,
                                                    Predicate<? super String> valuePredicate) {
        serviceBindingBuilder.matchesHeaders(headerName, valuePredicate);
        return this;
    }

    public final T build(Upstream upstream) {
        serviceBindingBuilder.build(new UpstreamHttpService(upstream));
        return gatewayBuilder;
    }
}
