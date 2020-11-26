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

package dev.gihwan.tollgate.core.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.linecorp.armeria.common.HttpMethod;

import dev.gihwan.tollgate.core.client.ServiceConfig;

@SuppressWarnings("ConstantConditions")
class RouteConfigTest {

    @Test
    void of() {
        assertThatThrownBy(() -> RouteConfig.of(null, null, null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> RouteConfig.of(null, "/api", newUpstreamConfig()))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> RouteConfig.of(HttpMethod.GET, null, newUpstreamConfig()))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> RouteConfig.of(HttpMethod.GET, "/api", null))
                .isInstanceOf(NullPointerException.class);

        final RouteConfig config = RouteConfig.of(HttpMethod.GET, "/api", newUpstreamConfig());
        assertThat(config.method()).isEqualTo(HttpMethod.GET);
        assertThat(config.path()).isEqualTo("/api");
        assertThat(config.upstream()).isEqualTo(newUpstreamConfig());
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    void equals() {
        final RouteConfig config = RouteConfig.of(HttpMethod.GET, "/api", newUpstreamConfig());

        final RouteConfig same = RouteConfig.of(HttpMethod.GET, "/api", newUpstreamConfig());
        final RouteConfig differentMethod = RouteConfig.of(HttpMethod.POST, "/api", newUpstreamConfig());
        final RouteConfig differentPath = RouteConfig.of(HttpMethod.GET, "/api/v1", newUpstreamConfig());
        final RouteConfig differentUpstream =
                RouteConfig.of(HttpMethod.GET, "/api", newUpstreamConfig("http://example.org"));

        assertThat(config.equals(same)).isTrue();
        assertThat(config.equals(differentMethod)).isFalse();
        assertThat(config.equals(differentPath)).isFalse();
        assertThat(config.equals(differentUpstream)).isFalse();

        assertThat(config.equals(null)).isFalse();
        assertThat(config.equals(new Dummy())).isFalse();
    }

    private static UpstreamConfig newUpstreamConfig() {
        return newUpstreamConfig("http://example.com");
    }

    private static UpstreamConfig newUpstreamConfig(String uri) {
        return UpstreamConfig.of(ServiceConfig.of(uri),
                                 UpstreamEndpointConfig.of(HttpMethod.GET, "/api"));
    }

    private static class Dummy {}
}
