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

package dev.gihwan.tollgate.hocon;

import java.util.function.Predicate;

import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.ServiceBindingBuilder;

import dev.gihwan.tollgate.gateway.UpstreamBindingBuilder;

public final class HoconUpstreamBindingBuilder extends UpstreamBindingBuilder<HoconGatewayBuilder> {

    HoconUpstreamBindingBuilder(HoconGatewayBuilder tollgateBuilder,
                                ServiceBindingBuilder serviceBindingBuilder) {
        super(tollgateBuilder, serviceBindingBuilder);
    }

    @Override
    public HoconUpstreamBindingBuilder path(String pathPattern) {
        return (HoconUpstreamBindingBuilder) super.path(pathPattern);
    }

    @Override
    public HoconUpstreamBindingBuilder pathPrefix(String prefix) {
        return (HoconUpstreamBindingBuilder) super.pathPrefix(prefix);
    }

    @Override
    public HoconUpstreamBindingBuilder methods(HttpMethod... methods) {
        return (HoconUpstreamBindingBuilder) super.methods(methods);
    }

    @Override
    public HoconUpstreamBindingBuilder methods(Iterable<HttpMethod> methods) {
        return (HoconUpstreamBindingBuilder) super.methods(methods);
    }

    @Override
    public HoconUpstreamBindingBuilder get(String pathPattern) {
        return (HoconUpstreamBindingBuilder) super.get(pathPattern);
    }

    @Override
    public HoconUpstreamBindingBuilder post(String pathPattern) {
        return (HoconUpstreamBindingBuilder) super.post(pathPattern);
    }

    @Override
    public HoconUpstreamBindingBuilder put(String pathPattern) {
        return (HoconUpstreamBindingBuilder) super.put(pathPattern);
    }

    @Override
    public HoconUpstreamBindingBuilder patch(String pathPattern) {
        return (HoconUpstreamBindingBuilder) super.patch(pathPattern);
    }

    @Override
    public HoconUpstreamBindingBuilder delete(String pathPattern) {
        return (HoconUpstreamBindingBuilder) super.delete(pathPattern);
    }

    @Override
    public HoconUpstreamBindingBuilder options(String pathPattern) {
        return (HoconUpstreamBindingBuilder) super.options(pathPattern);
    }

    @Override
    public HoconUpstreamBindingBuilder head(String pathPattern) {
        return (HoconUpstreamBindingBuilder) super.head(pathPattern);
    }

    @Override
    public HoconUpstreamBindingBuilder trace(String pathPattern) {
        return (HoconUpstreamBindingBuilder) super.trace(pathPattern);
    }

    @Override
    public HoconUpstreamBindingBuilder connect(String pathPattern) {
        return (HoconUpstreamBindingBuilder) super.connect(pathPattern);
    }

    @Override
    public HoconUpstreamBindingBuilder consumes(MediaType... consumeTypes) {
        return (HoconUpstreamBindingBuilder) super.consumes(consumeTypes);
    }

    @Override
    public HoconUpstreamBindingBuilder consumes(Iterable<MediaType> consumeTypes) {
        return (HoconUpstreamBindingBuilder) super.consumes(consumeTypes);
    }

    @Override
    public HoconUpstreamBindingBuilder produces(MediaType... produceTypes) {
        return (HoconUpstreamBindingBuilder) super.produces(produceTypes);
    }

    @Override
    public HoconUpstreamBindingBuilder produces(Iterable<MediaType> produceTypes) {
        return (HoconUpstreamBindingBuilder) super.produces(produceTypes);
    }

    @Override
    public HoconUpstreamBindingBuilder matchesParams(String... paramPredicates) {
        return (HoconUpstreamBindingBuilder) super.matchesParams(paramPredicates);
    }

    @Override
    public HoconUpstreamBindingBuilder matchesParams(Iterable<String> paramPredicates) {
        return (HoconUpstreamBindingBuilder) super.matchesParams(paramPredicates);
    }

    @Override
    public HoconUpstreamBindingBuilder matchesParams(String paramName,
                                                     Predicate<? super String> valuePredicate) {
        return (HoconUpstreamBindingBuilder) super.matchesParams(paramName, valuePredicate);
    }

    @Override
    public HoconUpstreamBindingBuilder matchesHeaders(String... headerPredicates) {
        return (HoconUpstreamBindingBuilder) super.matchesHeaders(headerPredicates);
    }

    @Override
    public HoconUpstreamBindingBuilder matchesHeaders(Iterable<String> headerPredicates) {
        return (HoconUpstreamBindingBuilder) super.matchesHeaders(headerPredicates);
    }

    @Override
    public HoconUpstreamBindingBuilder matchesHeaders(CharSequence headerName,
                                                      Predicate<? super String> valuePredicate) {
        return (HoconUpstreamBindingBuilder) super.matchesHeaders(headerName, valuePredicate);
    }
}
