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

package dev.gihwan.lith.core.service;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;

public final class ServiceFactory {

    private static final ServiceFactory INSTANCE = new ServiceFactory();

    public static ServiceFactory instance() {
        return INSTANCE;
    }

    private final Map<ServiceConfig, Service> services = new HashMap<>();

    private ServiceFactory() {}

    public Service get(ServiceConfig config) {
        requireNonNull(config, "config");

        final Service service = services.get(config);
        if (service != null) {
            return service;
        }

        final Service newService = Service.of(config);
        services.put(config, newService);
        return newService;
    }
}
