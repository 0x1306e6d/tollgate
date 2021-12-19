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

package dev.gihwan.tollgate.springframework.boot.autoconfigure;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;

import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.endpoint.EndpointGroup;

import dev.gihwan.tollgate.gateway.GatewayBuilder;
import dev.gihwan.tollgate.gateway.Upstream;

final class GatewayRouteConfigurationUtil {

    static void configureRoute(GatewayBuilder builder, TollgateProperties.Route properties) {
        builder.upstream(properties.getPath(), getUpstream(properties.getUpstream()));
    }

    private static Upstream getUpstream(TollgateProperties.Route.Upstream properties) {
        if (properties.getUri() != null) {
            return Upstream.of(properties.getUri());
        }

        checkArgument(properties.getScheme() != null, "scheme must exist");
        checkArgument(!isEmpty(properties.getEndpoints()), "endpoints must exist");

        final List<Endpoint> endpoints = properties.getEndpoints()
                                                   .stream()
                                                   .map(endpoint -> Endpoint.of(endpoint.getHost(),
                                                                                endpoint.getPort()))
                                                   .collect(Collectors.toUnmodifiableList());
        return Upstream.of(properties.getScheme(), EndpointGroup.of(endpoints));
    }

    private GatewayRouteConfigurationUtil() {}
}
