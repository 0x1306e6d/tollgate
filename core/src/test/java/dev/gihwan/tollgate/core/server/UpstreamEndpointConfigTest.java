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

import dev.gihwan.tollgate.core.server.UpstreamEndpointConfig;

@SuppressWarnings("ConstantConditions")
class UpstreamEndpointConfigTest {

    @Test
    void of() {
        assertThatThrownBy(() -> UpstreamEndpointConfig.of(null, null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> UpstreamEndpointConfig.of(HttpMethod.GET, null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> UpstreamEndpointConfig.of(null, "/api"))
                .isInstanceOf(NullPointerException.class);

        final UpstreamEndpointConfig config = UpstreamEndpointConfig.of(HttpMethod.GET, "/api");
        assertThat(config.method()).isEqualTo(HttpMethod.GET);
        assertThat(config.path()).isEqualTo("/api");
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    void equals() {
        final UpstreamEndpointConfig config = UpstreamEndpointConfig.of(HttpMethod.GET, "/api");

        final UpstreamEndpointConfig same = UpstreamEndpointConfig.of(HttpMethod.GET, "/api");
        final UpstreamEndpointConfig differentMethod = UpstreamEndpointConfig.of(HttpMethod.POST, "/api");
        final UpstreamEndpointConfig differentPath = UpstreamEndpointConfig.of(HttpMethod.GET, "/api/v1");

        assertThat(config.equals(same)).isTrue();
        assertThat(config.equals(differentMethod)).isFalse();
        assertThat(config.equals(differentPath)).isFalse();

        assertThat(config.equals(null)).isFalse();
        assertThat(config.equals(new Dummy())).isFalse();
    }

    private static class Dummy {}
}
