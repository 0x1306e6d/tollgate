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

package dev.gihwan.tollgate.core;

import java.util.function.Predicate;

import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.ServiceBindingBuilder;

public final class DefaultUpstreamBindingBuilder extends UpstreamBindingBuilder<DefaultTollgateBuilder> {

    DefaultUpstreamBindingBuilder(DefaultTollgateBuilder tollgateBuilder,
                                  ServiceBindingBuilder serviceBindingBuilder) {
        super(tollgateBuilder, serviceBindingBuilder);
    }

    @Override
    public DefaultUpstreamBindingBuilder path(String pathPattern) {
        return (DefaultUpstreamBindingBuilder) super.path(pathPattern);
    }

    @Override
    public DefaultUpstreamBindingBuilder pathPrefix(String prefix) {
        return (DefaultUpstreamBindingBuilder) super.pathPrefix(prefix);
    }

    @Override
    public DefaultUpstreamBindingBuilder methods(HttpMethod... methods) {
        return (DefaultUpstreamBindingBuilder) super.methods(methods);
    }

    @Override
    public DefaultUpstreamBindingBuilder methods(Iterable<HttpMethod> methods) {
        return (DefaultUpstreamBindingBuilder) super.methods(methods);
    }

    @Override
    public DefaultUpstreamBindingBuilder get(String pathPattern) {
        return (DefaultUpstreamBindingBuilder) super.get(pathPattern);
    }

    @Override
    public DefaultUpstreamBindingBuilder post(String pathPattern) {
        return (DefaultUpstreamBindingBuilder) super.post(pathPattern);
    }

    @Override
    public DefaultUpstreamBindingBuilder put(String pathPattern) {
        return (DefaultUpstreamBindingBuilder) super.put(pathPattern);
    }

    @Override
    public DefaultUpstreamBindingBuilder patch(String pathPattern) {
        return (DefaultUpstreamBindingBuilder) super.patch(pathPattern);
    }

    @Override
    public DefaultUpstreamBindingBuilder delete(String pathPattern) {
        return (DefaultUpstreamBindingBuilder) super.delete(pathPattern);
    }

    @Override
    public DefaultUpstreamBindingBuilder options(String pathPattern) {
        return (DefaultUpstreamBindingBuilder) super.options(pathPattern);
    }

    @Override
    public DefaultUpstreamBindingBuilder head(String pathPattern) {
        return (DefaultUpstreamBindingBuilder) super.head(pathPattern);
    }

    @Override
    public DefaultUpstreamBindingBuilder trace(String pathPattern) {
        return (DefaultUpstreamBindingBuilder) super.trace(pathPattern);
    }

    @Override
    public DefaultUpstreamBindingBuilder connect(String pathPattern) {
        return (DefaultUpstreamBindingBuilder) super.connect(pathPattern);
    }

    @Override
    public DefaultUpstreamBindingBuilder consumes(MediaType... consumeTypes) {
        return (DefaultUpstreamBindingBuilder) super.consumes(consumeTypes);
    }

    @Override
    public DefaultUpstreamBindingBuilder consumes(Iterable<MediaType> consumeTypes) {
        return (DefaultUpstreamBindingBuilder) super.consumes(consumeTypes);
    }

    @Override
    public DefaultUpstreamBindingBuilder produces(MediaType... produceTypes) {
        return (DefaultUpstreamBindingBuilder) super.produces(produceTypes);
    }

    @Override
    public DefaultUpstreamBindingBuilder produces(Iterable<MediaType> produceTypes) {
        return (DefaultUpstreamBindingBuilder) super.produces(produceTypes);
    }

    @Override
    public DefaultUpstreamBindingBuilder matchesParams(String... paramPredicates) {
        return (DefaultUpstreamBindingBuilder) super.matchesParams(paramPredicates);
    }

    @Override
    public DefaultUpstreamBindingBuilder matchesParams(Iterable<String> paramPredicates) {
        return (DefaultUpstreamBindingBuilder) super.matchesParams(paramPredicates);
    }

    @Override
    public DefaultUpstreamBindingBuilder matchesParams(String paramName,
                                                       Predicate<? super String> valuePredicate) {
        return (DefaultUpstreamBindingBuilder) super.matchesParams(paramName, valuePredicate);
    }

    @Override
    public DefaultUpstreamBindingBuilder matchesHeaders(String... headerPredicates) {
        return (DefaultUpstreamBindingBuilder) super.matchesHeaders(headerPredicates);
    }

    @Override
    public DefaultUpstreamBindingBuilder matchesHeaders(Iterable<String> headerPredicates) {
        return (DefaultUpstreamBindingBuilder) super.matchesHeaders(headerPredicates);
    }

    @Override
    public DefaultUpstreamBindingBuilder matchesHeaders(CharSequence headerName,
                                                        Predicate<? super String> valuePredicate) {
        return (DefaultUpstreamBindingBuilder) super.matchesHeaders(headerName, valuePredicate);
    }
}
