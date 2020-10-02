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

package dev.gihwan.tollgate.core.service;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.WebClientBuilder;
import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.common.Scheme;

public final class ServiceConfig {

    @Nullable
    private final String uri;
    @Nullable
    private Scheme scheme;
    @Nullable
    private List<Authority> authorities;

    @JsonCreator
    ServiceConfig(@JsonProperty("uri") @Nullable String uri,
                  @JsonProperty("scheme") @Nullable String scheme,
                  @JsonProperty("authorities") @Nullable List<Authority> authorities) {
        if (uri != null) {
            this.uri = uri;
            this.scheme = null;
            this.authorities = null;
        } else {
            requireNonNull(scheme, "scheme");
            requireNonNull(authorities, "authorities");
            checkArgument(!authorities.isEmpty(), "Authorities should not be empty.");
            this.scheme = Scheme.parse(scheme);
            this.authorities = authorities;
            this.uri = null;
        }
    }

    @Nullable
    public String uri() {
        return uri;
    }

    @Nullable
    public Scheme scheme() {
        return scheme;
    }

    @Nullable
    public List<Authority> authorities() {
        return authorities;
    }

    public WebClientBuilder webClientBuilder() {
        if (uri != null) {
            return WebClient.builder(uri);
        } else {
            assert scheme != null;
            assert authorities != null;

            final EndpointGroup endpointGroup = EndpointGroup.of(authorities.stream()
                                                                            .map(Authority::toArmeriaEndpoint)
                                                                            .collect(toUnmodifiableList()));
            return WebClient.builder(scheme.sessionProtocol(), endpointGroup);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("uri", uri)
                          .add("scheme", scheme)
                          .add("authorities", authorities)
                          .toString();
    }
}
