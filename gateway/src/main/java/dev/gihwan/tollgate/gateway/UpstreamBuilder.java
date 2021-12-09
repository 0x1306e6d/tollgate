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

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;

import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.WebClientBuilder;
import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.common.HttpHeaderNames;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.SessionProtocol;

import io.netty.util.AsciiString;

/**
 * A builder for {@link Upstream}.
 */
public final class UpstreamBuilder {

    private final WebClientBuilder clientBuilder;

    private Function<HttpRequest, HttpRequest> requestFunction = Function.identity();
    private Function<HttpResponse, HttpResponse> responseFunction = Function.identity();

    UpstreamBuilder(URI uri) {
        clientBuilder = WebClient.builder(requireNonNull(uri, "uri"));
    }

    UpstreamBuilder(SessionProtocol protocol, EndpointGroup endpointGroup) {
        clientBuilder = WebClient.builder(requireNonNull(protocol, "protocol"),
                                          requireNonNull(endpointGroup, "endpointGroup"));
    }

    UpstreamBuilder(SessionProtocol protocol, EndpointGroup endpointGroup, String path) {
        clientBuilder = WebClient.builder(requireNonNull(protocol, "protocol"),
                                          requireNonNull(endpointGroup, "endpointGroup"),
                                          requireNonNull(path, "path"));
    }

    /**
     * Configures a {@link WebClient} of this {@link Upstream} with the given {@code configurator}.
     *
     * Please note that calling {@link WebClientBuilder#build()} inside {@code configurator} does not affect
     * building {@link Upstream}.
     */
    public UpstreamBuilder client(Consumer<? super WebClientBuilder> configurator) {
        requireNonNull(configurator, "configurator");
        configurator.accept(clientBuilder);
        return this;
    }

    /**
     * Transforms a request from a user using the given {@link Function}.
     */
    public UpstreamBuilder mapRequest(Function<? super HttpRequest, ? extends HttpRequest> function) {
        requestFunction = requestFunction.andThen(function);
        return this;
    }

    /**
     * Sets request path to this upstream with the given {@code pathPattern}.
     */
    public UpstreamBuilder path(String pathPattern) {
        requireNonNull(pathPattern, "pathPattern");
        return mapRequest(new RemappingPathFunction(pathPattern));
    }

    public UpstreamBuilder allowRequestHeaders(CharSequence... headers) {
        return allowRequestHeaders(ImmutableList.copyOf(requireNonNull(headers, "headers")));
    }

    public UpstreamBuilder allowRequestHeaders(Iterable<? extends CharSequence> headers) {
        requireNonNull(headers, "headers");
        final Set<AsciiString> allowedRequestHeaders = Streams.stream(headers)
                                                              .map(HttpHeaderNames::of)
                                                              .collect(Collectors.toUnmodifiableSet());
        checkArgument(!allowedRequestHeaders.isEmpty(), "allowed request headers should not be empty");
        return mapRequest(FilteringRequestHeadersFunction.ofAllowedSet(allowedRequestHeaders));
    }

    /**
     * Disallows the given {@code headers} to be included in a request from a user to the upstream server.
     */
    public UpstreamBuilder disallowRequestHeaders(CharSequence... headers) {
        return disallowRequestHeaders(ImmutableList.copyOf(requireNonNull(headers, "headers")));
    }

    /**
     * Disallows the given {@code headers} to be included in a request from a user to the upstream server.
     */
    public UpstreamBuilder disallowRequestHeaders(Iterable<? extends CharSequence> headers) {
        requireNonNull(headers, "headers");
        final Set<AsciiString> disallowedRequestHeaders = Streams.stream(headers)
                                                                 .map(HttpHeaderNames::of)
                                                                 .collect(Collectors.toUnmodifiableSet());
        checkArgument(!disallowedRequestHeaders.isEmpty(), "disallowed request headers should not be empty");
        return mapRequest(FilteringRequestHeadersFunction.ofDisallowedSet(disallowedRequestHeaders));
    }

    /**
     * Transforms a response from the upstream server using the given {@link Function}.
     */
    public UpstreamBuilder mapResponse(Function<? super HttpResponse, ? extends HttpResponse> function) {
        responseFunction = responseFunction.andThen(function);
        return this;
    }

    /**
     * Sets response status from this upstream with the given {@link HttpStatusFunction}.
     */
    public UpstreamBuilder status(HttpStatusFunction statusFunction) {
        requireNonNull(statusFunction, "statusFunction");
        return mapResponse(new RemappingStatusFunction(statusFunction));
    }

    public UpstreamBuilder allowResponseHeaders(CharSequence... headers) {
        return allowResponseHeaders(ImmutableList.copyOf(requireNonNull(headers, "headers")));
    }

    public UpstreamBuilder allowResponseHeaders(Iterable<? extends CharSequence> headers) {
        requireNonNull(headers, "headers");
        final Set<AsciiString> allowedResponseHeaders = Streams.stream(headers)
                                                               .map(HttpHeaderNames::of)
                                                               .collect(Collectors.toUnmodifiableSet());
        checkArgument(!allowedResponseHeaders.isEmpty(), "allowed response headers should not be empty");
        return mapResponse(FilteringResponseHeadersFunction.ofAllowedSet(allowedResponseHeaders));
    }

    /**
     * Disallows the given {@code headers} to be included in a response from the upstream server to a user.
     */
    public UpstreamBuilder disallowResponseHeaders(CharSequence... headers) {
        return disallowResponseHeaders(ImmutableList.copyOf(requireNonNull(headers, "headers")));
    }

    /**
     * Disallows the given {@code headers} to be included in a response from the upstream server to a user.
     */
    public UpstreamBuilder disallowResponseHeaders(Iterable<? extends CharSequence> headers) {
        requireNonNull(headers, "headers");
        final Set<AsciiString> disallowedResponseHeaders = Streams.stream(headers)
                                                                  .map(HttpHeaderNames::of)
                                                                  .collect(Collectors.toUnmodifiableSet());
        checkArgument(!disallowedResponseHeaders.isEmpty(), "disallowed response headers should not be empty");
        return mapResponse(FilteringResponseHeadersFunction.ofDisallowedSet(disallowedResponseHeaders));
    }

    /**
     * Decorates a {@link WebClient} of this {@link Upstream} with the given {@code decorator}.
     *
     * This method is a shortcut for {@link UpstreamBuilder#client(Consumer)} with a {@code configurator}
     * which calls {@link WebClientBuilder#decorator(Function)}.
     */
    public UpstreamBuilder decorator(Function<? super HttpClient, ? extends HttpClient> decorator) {
        clientBuilder.decorator(requireNonNull(decorator, "decorator"));
        return this;
    }

    /**
     * Builds a new {@link Upstream} based on the properties of this builder.
     */
    public Upstream build() {
        return new DefaultUpstream(clientBuilder.build(), requestFunction, responseFunction);
    }
}
