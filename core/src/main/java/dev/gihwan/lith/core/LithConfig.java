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

package dev.gihwan.lith.core;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import dev.gihwan.lith.core.io.Json;
import dev.gihwan.lith.core.route.Endpoint;

public final class LithConfig {

    public static LithConfig fromResource(Path path) throws IOException {
        requireNonNull(path, "path");
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = loader.getResourceAsStream(path.toString())) {
            checkArgument(is != null, "Resource should exist.");
            return Json.readValue(is, LithConfig.class);
        }
    }

    private final int port;
    private final String healthCheckPath;
    private final List<Endpoint> endpoints;

    @JsonCreator
    LithConfig(@JsonProperty("port") int port,
               @JsonProperty("healthCheckPath") String healthCheckPath,
               @JsonProperty("endpoints") List<Endpoint> endpoints) {
        checkArgument(port >= 0, "port: %s (expected: >= 0)", port);
        checkArgument(port <= 65535, "port: %s (expected: <= 65535)", port);
        this.port = port;
        requireNonNull(healthCheckPath, "healthCheckPath");
        this.healthCheckPath = healthCheckPath;
        requireNonNull(endpoints, "endpoints");
        this.endpoints = endpoints;
    }

    public int port() {
        return port;
    }

    public String healthCheckPath() {
        return healthCheckPath;
    }

    public List<Endpoint> endpoints() {
        return endpoints;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("port", port)
                          .add("healthCheckPath", healthCheckPath)
                          .add("endpoints", endpoints)
                          .toString();
    }
}
