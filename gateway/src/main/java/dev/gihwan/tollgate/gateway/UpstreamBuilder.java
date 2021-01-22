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

import java.net.URI;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.WebClientBuilder;
import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.common.SessionProtocol;

import dev.gihwan.tollgate.gateway.remapping.RemappingRule;

public final class UpstreamBuilder {

    @Nullable
    private Function<? super Upstream, ? extends Upstream> decorator;
    @Nullable
    private RemappingRule remappingRule;

    private final WebClientBuilder clientBuilder;

    UpstreamBuilder(URI uri) {
        clientBuilder = WebClient.builder(uri);
    }

    UpstreamBuilder(SessionProtocol protocol, EndpointGroup endpointGroup) {
        clientBuilder = WebClient.builder(protocol, endpointGroup);
    }

    UpstreamBuilder(SessionProtocol protocol, EndpointGroup endpointGroup, String path) {
        clientBuilder = WebClient.builder(protocol, endpointGroup, path);
    }

    public UpstreamBuilder remapping(RemappingRule... remappingRules) {
        return remapping(ImmutableList.copyOf(requireNonNull(remappingRules, "remappingRules")));
    }

    public UpstreamBuilder remapping(Iterable<? extends RemappingRule> remappingRules) {
        if (remappingRule == null) {
            remappingRule = RemappingRule.of(remappingRules);
        } else {
            remappingRule = remappingRule.andThen(RemappingRule.of(remappingRules));
        }
        return this;
    }

    public UpstreamBuilder decorator(Function<? super Upstream, ? extends Upstream> decorator) {
        requireNonNull(decorator, "decorator");
        if (this.decorator == null) {
            this.decorator = decorator;
        } else {
            this.decorator = this.decorator.andThen(decorator);
        }
        return this;
    }

    public Upstream build() {
        Upstream upstream = new DefaultUpstream(clientBuilder.build(), remappingRule);
        if (decorator != null) {
            upstream = upstream.decorate(decorator);
        }
        return upstream;
    }
}
