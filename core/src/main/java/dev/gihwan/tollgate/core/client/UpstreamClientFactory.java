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

package dev.gihwan.tollgate.core.client;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;

public final class UpstreamClientFactory {

    private static final UpstreamClientFactory INSTANCE = new UpstreamClientFactory();

    public static UpstreamClientFactory instance() {
        return INSTANCE;
    }

    private final Map<ServiceConfig, UpstreamClient> services = new HashMap<>();

    private UpstreamClientFactory() {}

    public UpstreamClient get(ServiceConfig config) {
        requireNonNull(config, "config");

        final UpstreamClient upstreamClient = services.get(config);
        if (upstreamClient != null) {
            return upstreamClient;
        }

        final UpstreamClient newUpstreamClient = UpstreamClient.of(config);
        services.put(config, newUpstreamClient);
        return newUpstreamClient;
    }
}
